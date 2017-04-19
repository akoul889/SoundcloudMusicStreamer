package com.quintype.musicstreaming.ui.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.models.Track;
import com.quintype.musicstreaming.ui.fragments.QuickControlsFragment;
import com.quintype.musicstreaming.ui.fragments.SoundcloudListFragment;
import com.quintype.musicstreaming.ui.slidinguppanel.SlidingUpPanelLayout;
import com.quintype.musicstreaming.utils.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends PlayerActivity {


    ArrayList<Audio> playList = new ArrayList<>();
    SlidingUpPanelLayout slidingUpPanelLayout;
    QuickControlsFragment quickControlsFragment = new QuickControlsFragment();
//    private PlaybackControlsFragment mControlsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

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
//                    playAudio(((int) event.second));
                    presenter.playNewTrack((int) event.second);
                }
                break;
            case Constants.EVENT_PLAY_PAUSE_CLICK:
                presenter.playStream();
                break;
            case Constants.EVENT_NEXT_CLICK:
                presenter.nextStream();
                break;
            case Constants.EVENT_PREVIOUS_CLICK:
                presenter.previousStream();
                break;
            case Constants.EVENT_UPDATE_PLAYLIST:
                playList.clear();
                playList.addAll(getAudioFromTracks((ArrayList<Track>) event.second));
                presenter.updatePlaylist(playList);
                break;
            default:
                Toast.makeText(mContext, "Unhandled event " + event.first, Toast.LENGTH_SHORT)
                        .show();
        }
    }

    private String getHirezArtwork(String artworkUrl) {
        return artworkUrl.replace("large.jpg", "t500x500.jpg");
    }

    private Collection<Audio> getAudioFromTracks(ArrayList<Track> trackList) {
        List<Audio> adioList = new ArrayList<>();
        for (Track track : trackList) {
            Audio audio = new Audio(track.getId(), track.getDuration(), track.getDescription(),
                    track.getTitle(), track.getGenre(), track.getUser().getUsername(),
                    getHirezArtwork(track.getArtworkUrl()), track.getStreamUrl() + "?client_id="
                    + getString(R.string
                    .soundcloud_client_id));
            adioList.add(audio);
        }
        return adioList;
    }

    private void playAudio(int audioIndex) {
        //Check is service is active
//        if (!serviceBound) {
//            //Store Serializable audioList to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudio(playList);
//            storage.storeAudioIndex(audioIndex);
//
//            Intent playerIntent = new Intent(this, MediaService.class);
//            startService(playerIntent);
//            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        } else {
//            //Store the new audioIndex to SharedPreferences
//            StorageUtil storage = new StorageUtil(getApplicationContext());
//            storage.storeAudioIndex(audioIndex);
//
//            //Service is active
//            //Send a broadcast to the service -> PLAY_NEW_AUDIO
//            Intent broadcastIntent = new Intent(Constants.Broadcast_PLAY_NEW_AUDIO);
//            sendBroadcast(broadcastIntent);
//        }
    }

    @Override
    public int getCurrentTrackPosition() {
        return presenter.getCurrentMediaPosition();
    }

    @Override
    public boolean isPlaying(){
        return presenter.isMediaPlaying();
    }

    public class initQuickControls extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .replace(R.id.quickcontrols_container, quickControlsFragment)
                    .commitAllowingStateLoss();
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

    @Override
    public void initializeUI(Audio stream, boolean isPlaying) {
        Timber.d("Playing " + stream.getTitle());
        quickControlsFragment.initializePlayer(stream, isPlaying);
    }

    @Override
    public void setLoading() {
        quickControlsFragment.setLoading();
    }

    @Override
    public void setToStopped() {
        quickControlsFragment.setToStopped();
    }

    @Override
    public void setToPlaying() {
        quickControlsFragment.setToPlaying();
    }

    @Override
    public void animateTo(Audio currentStream) {

    }

    @Override
    public void updateTimer(String timeLeft) {

    }

    @Override
    public void error(String error) {
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showStreamsDialog(List<Audio> streams) {

    }

}
