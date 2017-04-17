package com.quintype.musicstreaming.notificationmanager;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.ui.activities.MainActivity;

import java.util.concurrent.TimeUnit;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class StreamService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = StreamService.class.getSimpleName();

    private static final int NOTIFY_ID = 1;
    private final float MAX_VOLUME = 1.0f;

    public static final String STREAM_DONE_LOADING_INTENT = "stream_done_loading_intent";
    public static final String STREAM_DONE_LOADING_SUCCESS = "stream_done_loading_success";
    public static final String TIMER_UPDATE_INTENT = "timer_update_intent";
    public static final String TIMER_UPDATE_VALUE = "timer_update_value";
    public static final String TIMER_DONE_INTENT = "timer_done_intent";

    private static final String ACTION_STOP = "action_stop";

    public enum State {PAUSED, STOPPED, PLAYING, PREPARING}

    private State state = State.STOPPED;

    private final IBinder streamBinder = new StreamBinder();
    private MediaPlayer player;
    private Audio currentStream;
    private LocalBroadcastManager broadcastManager;
    private CountDownTimer countDownTimer;

    public void onCreate() {
        super.onCreate();

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    public class StreamBinder extends Binder {
        public StreamService getService() {
            return StreamService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        toBackground();
        return streamBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        toBackground();
        super.onRebind(intent);
    }

    private void toBackground() {
        stopForeground(true);
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
    private void toForeground() {


        //Todo fix the notification logic
        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setShowWhen(false)
                .setContent(notificationView);

        notificationView.setImageViewResource(R.id.streamIcon, R.mipmap.ic_launcher_round);
        notificationView.setTextViewText(R.id.titleTxt, getString(R.string.app_name));
        notificationView.setTextViewText(R.id.descTxt, currentStream.getTitle());

        Intent closeIntent = new Intent(getApplicationContext(), StreamService.class);
        closeIntent.setAction(ACTION_STOP);
        PendingIntent pendingCloseIntent = PendingIntent.getService(getApplicationContext(), 1,
                closeIntent, 0);

        notificationView.setOnClickPendingIntent(R.id.closeStream, pendingCloseIntent);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent
                .FLAG_ONE_SHOT);
        builder.setContentIntent(resultPendingIntent);

        Notification notification = builder.build();
        startForeground(NOTIFY_ID, notification);
    }

    /**
     * Handle intent from notification
     *
     * @param intent intent to add pending intent to
     */
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_STOP)) {
            Log.i(TAG, "handleIntent: stopping stream from notification");

            stopStreaming();
            stopSleepTimer();
            toBackground();
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

        // If a stream was already running stop it and reset
        if (player.isPlaying()) {
            player.stop();
        }

        player.reset();

        try {
            state = State.PREPARING;

//            List<String> keys = Arrays.asList(getResources().getStringArray(R.array.api_keys));
//            String key = keys.get((new Random()).nextInt(keys.size()));

            player.setDataSource(this, Uri.parse(String.format("%s?client_id=%s", stream
                    .getStreamUrl(), getString(R.string.soundcloud_client_id))));
            player.setLooping(true);
            player.setVolume(MAX_VOLUME, MAX_VOLUME);
            currentStream = stream;
        } catch (Exception e) {
            Log.e(TAG, "playStream: ", e);
        }
        player.prepareAsync();
    }

    public void pauseStream() {

        if (state == State.PLAYING) {
            player.pause();
            stopSleepTimer();
            state = State.PAUSED;
        }
    }

    public void resumeStream() {

        if (state == State.PAUSED) {
            player.start();
            state = State.PLAYING;
        }
    }

    /**
     * Stop the MediaPlayer if something is streaming
     */
    public void stopStreaming() {

        if (state == State.PLAYING || state == State.PAUSED) {
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
                    toBackground();
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
    }
}