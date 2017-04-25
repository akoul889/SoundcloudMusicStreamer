package com.quintype.musicstreaming.notificationmanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.models.Audio;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class StreamService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = StreamService.class.getSimpleName();

    private static final int NOTIFY_ID = 1;
    private final float MAX_VOLUME = 1.0f;


    public static final String ACTION_PLAY = "com.quintype.musicstreaming.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.quintype.musicstreaming.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.quintype.musicstreaming.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.quintype.musicstreaming.ACTION_NEXT";
    public static final String ACTION_STOP = "com.quintype.musicstreaming.ACTION_STOP";

    public static final String STREAM_DONE_LOADING_INTENT = "stream_done_loading_intent";
    public static final String STREAM_DONE_LOADING_SUCCESS = "stream_done_loading_success";
    public static final String TIMER_UPDATE_INTENT = "timer_update_intent";
    public static final String TIMER_UPDATE_VALUE = "timer_update_value";
    public static final String TIMER_DONE_INTENT = "timer_done_intent";


    //MediaSession
    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;
    private AudioManager mAudioManager;


    public enum State {PAUSED, STOPPED, PLAYING, PREPARING}

    private State state = State.STOPPED;

    private final IBinder streamBinder = new StreamBinder();
    private MediaPlayer player;
    private Audio currentStream;
    private LocalBroadcastManager broadcastManager;
    private CountDownTimer countDownTimer;

    private static final int STOP_DELAY = 30000;

    public void onCreate() {
        super.onCreate();
        try {
            initMediaSession();
        } catch (RemoteException e) {
            Timber.e("RemoteException", e);
        }
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    private final DelayedStopHandler mDelayedStopHandler = new DelayedStopHandler(this);

    public class StreamBinder extends Binder {
        public StreamService getService() {
            return StreamService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        toBackground(true);
        return streamBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        toBackground(true);
        super.onRebind(intent);
    }

    private void toBackground(boolean removeNotification) {
        stopForeground(removeNotification);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (player.isPlaying() || state == State.PREPARING) {
            toForeground();
        }
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Run the service in the foreground
     * and show a notification
     */
//    private void toForeground() {
//
//
//        //Todo fix the notification logic
//        RemoteViews notificationView = new RemoteViews(getPackageName(),
//                R.layout.notification);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(android.R.drawable.stat_sys_headset)
//                .setShowWhen(false)
//                .setContent(notificationView);
//
//
//        notificationView.setImageViewResource(R.id.streamIcon, R.mipmap.ic_launcher_round);
//        notificationView.setTextViewText(R.id.titleTxt, getString(R.string.app_name));
//        notificationView.setTextViewText(R.id.descTxt, currentStream.getTitle());
//
//        Intent closeIntent = new Intent(getApplicationContext(), StreamService.class);
//        closeIntent.setAction(ACTION_STOP);
//        PendingIntent pendingCloseIntent = PendingIntent.getService(getApplicationContext(), 1,
//                closeIntent, 0);
//
//        notificationView.setOnClickPendingIntent(R.id.closeStream, pendingCloseIntent);
//
//        Intent resultIntent = new Intent(this, MainActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent
//                .FLAG_ONE_SHOT);
//        builder.setContentIntent(resultPendingIntent);
//
//        Notification notification = builder.build();
//        startForeground(NOTIFY_ID, notification);
//
//
//    }
    public void toForeground() {

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (state == State.PLAYING) {
            notificationAction = android.R.drawable.ic_media_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (state == State.PAUSED) {
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }
        Intent closeIntent = new Intent(getApplicationContext(), StreamService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pendingCloseIntent = PendingIntent.getService(getApplicationContext(), 1,
                closeIntent, 0);


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher); //replace with your own image

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(pendingCloseIntent))
                // Set the Notification color
                .setColor(getResources().getColor(R.color.colorPrimary))
                // Set the large and small icons
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)

                // Set Notification content information
                .setContentText(currentStream.getArtist())
                .setContentTitle(currentStream.getGenre())
                .setContentInfo(currentStream.getTitle())
                // Add playback actions
                .addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2));

//        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify
//                (NOTIFICATION_ID, notificationBuilder.build());
        Notification notification = notificationBuilder.build();
        startForeground(NOTIFY_ID, notification);
    }

    /**
     * Handle intent from notification
     *
     * @param playbackAction intent to add pending intent to
     */
    private void handleIntent(Intent playbackAction) {
        Timber.d("handleIncomingActions");
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            Log.i(TAG, "handleIntent: stopping stream from notification");

            stopStreaming();
            stopSleepTimer();
            toBackground(true);
            stopSelf();
        }
    }

    public State getState() {

        return state;
    }

    /**
     * Start play a stream
     */
    public void playStream(Audio stream) {

        int status = mAudioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);


        mDelayedStopHandler.removeCallbacksAndMessages(null);
        if (status != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return;
        }

        // If a stream was already running stop it and reset
        if (player.isPlaying()) {
            player.stop();
        }

        player.reset();

        try {
            state = State.PREPARING;

//            List<String> keys = Arrays.asList(getResources().getStringArray(R.array.api_keys));
//            String key = keys.get((new Random()).nextInt(keys.size()));

            player.setDataSource(this, Uri.parse(stream.getStreamUrl()));
//            player.setDataSource(this, Uri.parse(String.format("%s?client_id=%s", stream
//                    .getStreamUrl(), getString(R.string.soundcloud_client_id))));
            player.setLooping(true);
            player.setVolume(MAX_VOLUME, MAX_VOLUME);
            this.currentStream = stream;
        } catch (Exception e) {
            Log.e(TAG, "playStream: ", e);
        }
        player.prepareAsync();
    }

    public void pauseStream() {

        if (state == State.PLAYING) {

            mDelayedStopHandler.removeCallbacksAndMessages(null);
            mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);
            player.pause();
            stopSleepTimer();
            state = State.PAUSED;
        }
    }

    public void resumeStream() {

        if (state == State.PAUSED) {

            mDelayedStopHandler.removeCallbacksAndMessages(null);
            player.start();
            state = State.PLAYING;
        }
    }

    /**
     * Stop the MediaPlayer if something is streaming
     */
    public void stopStreaming() {

        if (state == State.PLAYING || state == State.PAUSED) {
            mDelayedStopHandler.removeCallbacksAndMessages(null);
            mDelayedStopHandler.sendEmptyMessageDelayed(0, STOP_DELAY);
            player.stop();
            player.reset();
            state = State.STOPPED;
        }
    }

    /**
     * Get the stream that is playing right now, if any
     *
     * @return the playing stream or null
     */
    public Audio getPlayingStream() {

        if (state == State.PLAYING || state == State.PAUSED) {
            return currentStream;
        }
        return null;
    }


    /**
     * Get the current position of stream
     *
     * @return the playing stream or null
     */
    public int getCurrentStreamPosition() {

        if (state == State.PLAYING || state == State.PAUSED) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    /**
     * Set a sleep timer
     *
     * @param milliseconds to wait before sleep
     */
    public void setSleepTimer(int milliseconds) {
        Log.i(TAG, "setSleepTimer: setting sleep timer for " + milliseconds + "ms");

        stopSleepTimer();

        if (milliseconds != 0) {

            countDownTimer = new CountDownTimer(milliseconds, 1000) {

                public void onTick(long millisUntilFinished) {
                    Intent intent = new Intent(TIMER_UPDATE_INTENT);
                    intent.putExtra(TIMER_UPDATE_VALUE, (int) millisUntilFinished);
                    broadcastManager.sendBroadcast(intent);
                    if (millisUntilFinished < TimeUnit.SECONDS.toMillis(30)) {
                        //lower the volume by respective step
                        lowerVolume((int) ((int) millisUntilFinished / TimeUnit.SECONDS.toMillis
                                (1)));
                    }
                }

                public void onFinish() {
                    stopStreaming();
                    stopSleepTimer();
                    timerDoneBroadcast();
                    toBackground(true);
                }

            }.start();
        }
    }

    /**
     * Lowers the volume of the stream to a step
     *
     * @param step out of a max of 30
     */
    private void lowerVolume(int step) {

        float voulme = ((float) step) / 30f;
        player.setVolume(voulme, voulme);
    }

    /**
     * Stop the sleep timer and restore volume to max
     */
    private void stopSleepTimer() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        player.setVolume(MAX_VOLUME, MAX_VOLUME);
    }

    private void timerDoneBroadcast() {
        Log.i(TAG, "setSleepTimer: sleep timer is done, notifying bindings.");

        Intent intent = new Intent(TIMER_DONE_INTENT);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i(TAG, "onError: " + what + ", " + extra);

        mp.reset();
        notifyStreamLoaded(false);
        state = State.STOPPED;
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        notifyStreamLoaded(true);
        mp.start();
        state = State.PLAYING;
    }

    /**
     * Send out a broadcast indicating stream was started with success or couldn't be found
     *
     * @param success
     */
    private void notifyStreamLoaded(boolean success) {

        Intent intent = new Intent(STREAM_DONE_LOADING_INTENT);
        intent.putExtra(STREAM_DONE_LOADING_SUCCESS, success);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
        mDelayedStopHandler.removeCallbacksAndMessages(null);
    }


    /**
     * Seek up to a position on a media player
     *
     * @param position seek position
     */
    public void seek(int position) {
        if (player != null) {
            if (position < 0) {
                position = 0;
            } else if (position > currentStream.getDuration()) {
                position = currentStream.getDuration();
            }
            player.seekTo((int) position);
//            notifyChange(POSITION_CHANGED);
        }
    }

    private PendingIntent playbackAction(int actionNumber) {
        Timber.d("playbackAction");
        Intent playbackAction = new Intent(this, StreamService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    private void initMediaSession() throws RemoteException {
        Timber.d("initMediaSession");
        if (mediaSessionManager != null) return; //mediaSessionManager exists

        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        //Set mediaSession's MetaData
        updateMetaData();

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                Timber.d("onPlay");
                super.onPlay();
                resumeStream();
                toForeground();
            }

            @Override
            public void onPause() {
                Timber.d("onPause");
                super.onPause();
                pauseStream();
//                buildNotification(PlaybackStatus.PAUSED);
                toForeground();
                toBackground(false);
            }

            @Override
            public void onSkipToNext() {
                Timber.d("onSkipToNext");
                super.onSkipToNext();
//                skipToNext();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                Timber.d("onSkipToPrevious");
                super.onSkipToPrevious();
//                skipToPrevious();
//                updateMetaData();
//                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                Timber.d("onStop");
                super.onStop();

                //TODO check when this get triggered
                /*stopself will destroy the service which in turn will remove the notification so
                 I don't see the point of removeNotification()
                  */
//                removeNotification();
                //Stop the service
//                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                Timber.d("onSeekTo");
                super.onSeekTo(position);
            }
        });
    }

    private void updateMetaData() {
        Timber.d("updateMetaData");
        if (currentStream != null) {
            Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher_round); //replace with medias albumArt
            // Update the current metadata
            mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, currentStream
                            .getArtwork())
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentStream.getArtist())
                    .putString(MediaMetadataCompat.METADATA_KEY_GENRE, currentStream.getGenre())
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentStream.getTitle())
                    .build());
        }
    }

    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager
            .OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(final int focusChange) {
            if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                pauseStream();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Resume playback
                resumeStream();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(this);
                // Stop playback
            } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // Lower the volume
                pauseStream();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Raise it back to normal
                resumeStream();
            }
        }
    };

    /**
     * A simple handler that stops the service if playback is not active (playing)
     */
    private static class DelayedStopHandler extends Handler {
        private final WeakReference<StreamService> mWeakReference;

        private DelayedStopHandler(StreamService service) {
            mWeakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            StreamService service = mWeakReference.get();
            if (service != null && service.player != null) {
                if (service.player.isPlaying() || service.state == State.PLAYING) {
                    Timber.d("Ignoring delayed stop since the media player is in use.");
                    return;
                }
                Timber.d("Stopping service with delay handler.");
                service.stopSelf();
            }
        }
    }
}