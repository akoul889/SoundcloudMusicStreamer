package com.quintype.musicstreaming.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.models.Track;
import com.quintype.musicstreaming.notificationmanager.MediaService;
import com.quintype.musicstreaming.ui.fragments.QuickControlsFragment;
import com.quintype.musicstreaming.ui.fragments.SoundcloudListFragment;
import com.quintype.musicstreaming.ui.slidinguppanel.SlidingUpPanelLayout;
import com.quintype.musicstreaming.utils.Constants;
import com.quintype.musicstreaming.utils.StorageUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends BaseFragmentActivity {


    private MediaService player;
    boolean serviceBound = false;
    ArrayList<Audio> playList = new ArrayList<>();
    SlidingUpPanelLayout slidingUpPanelLayout;

//    private PlaybackControlsFragment mControlsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        slidingUpPanelLayout = (SlidingUpPanelLayout)findViewById(R.id.sliding_layout);

        setPanelSlideListeners(slidingUpPanelLayout);
        addFragment(SoundcloudListFragment.create(), null);
        new initQuickControls().execute("");
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void clickAnalyticsEvent(String categoryId, String actionId, String labelId, long
            value) {

    }

    @Override
    public void propagateEvent(Pair<String, Object> event) {
        switch (event.first) {
            case Constants.EVENT_TRACK_CLICK:
                if (!playList.isEmpty()) {
                    Audio audio = playList.get(((int) event.second));
                    Toast.makeText(mContext, "Track clicked =" + audio.getTitle(),
                            Toast.LENGTH_SHORT).show();
//                hidePlaybackControls();
//                showPlaybackControls();
//                    playAudio(audio.getStreamUrl() + "?client_id=" + getString(R.string
//                            .soundcloud_client_id));
                    playAudio(((int) event.second));
                }
                break;
            case Constants.EVENT_UPDATE_PLAYLIST:
                playList.clear();
                playList.addAll(getAudioFromTracks((ArrayList<Track>) event.second));
                break;
            default:
                Toast.makeText(mContext, "Unhandled event " + event.first, Toast.LENGTH_SHORT)
                        .show();
        }
    }

    private Collection<Audio> getAudioFromTracks(ArrayList<Track> trackList) {
        List<Audio> adioList = new ArrayList<>();
        for (Track track : trackList) {
            Audio audio = new Audio(track.getDescription(), track.getTitle(), track.getGenre(),
                    track.getUser().getUsername(), track.getArtworkUrl(), track.getStreamUrl() +
                    "?client_id=" + getString(R.string.soundcloud_client_id));
            adioList.add(audio);
        }
        return adioList;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("Activity onStart");

//        mControlsFragment = (PlaybackControlsFragment) getFragmentManager()
//                .findFragmentById(R.id.fragment_playback_controls);
//        if (mControlsFragment == null) {
//            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
//        }

        hidePlaybackControls();

    }

    protected void showPlaybackControls() {
        Timber.d("showPlaybackControls");
//        if (NetworkHelper.isOnline(this)) {
//            getFragmentManager().beginTransaction()
//                    .setCustomAnimations(
//                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
//                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
//                    .show(mControlsFragment)
//                    .commit();
//        }
    }

    protected void hidePlaybackControls() {
        Timber.d("hidePlaybackControls");
//        getFragmentManager().beginTransaction()
//                .hide(mControlsFragment)
//                .commit();
    }

    protected boolean shouldShowControls() {
        MediaControllerCompat mediaController = getSupportMediaController();
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            return false;
        }
        switch (mediaController.getPlaybackState().getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                return false;
            default:
                return true;
        }
    }

    // Callback that ensures that we are showing the controls
    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        Timber.d("mediaControllerCallback.onPlaybackStateChanged: " +
                                "hiding controls because state is ", state.getState());
                        hidePlaybackControls();
                    }
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        Timber.d("mediaControllerCallback.onMetadataChanged: " +
                                "hiding controls because metadata is null");
                        hidePlaybackControls();
                    }
                }
            };

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaService.LocalBinder binder = (MediaService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(playList);
            storage.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Constants.Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            //service is active
            player.stopSelf();
        }
    }

    public class initQuickControls extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            QuickControlsFragment fragment1 = new QuickControlsFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .replace(R.id.quickcontrols_container, fragment1).commitAllowingStateLoss();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
//            QuickControlsFragment.topContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NavigationUtils.navigateToNowplaying(BaseActivity.this, false);
//                }
//            });
        }

        @Override
        protected void onPreExecute() {
        }
    }


    public void setPanelSlideListeners(SlidingUpPanelLayout panelLayout) {
        panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                View nowPlayingCard = QuickControlsFragment.topContainer;
                nowPlayingCard.setAlpha(1 - slideOffset);
            }

            @Override
            public void onPanelCollapsed(View panel) {
                View nowPlayingCard = QuickControlsFragment.topContainer;
                nowPlayingCard.setAlpha(1);
            }

            @Override
            public void onPanelExpanded(View panel) {
                View nowPlayingCard = QuickControlsFragment.topContainer;
                nowPlayingCard.setAlpha(0);
            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
    }
}
