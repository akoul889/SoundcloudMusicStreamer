package com.quintype.musicstreaming.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.adapter.NowPlayingAdapter;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.utils.RecyclerItemClickListener;
import com.quintype.musicstreaming.utils.StorageUtil;
import com.quintype.musicstreaming.widgets.PlayPauseButton;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class NowPlayingActivity extends PlayerActivity {

    RecyclerView nowPlayingRecyclerView;
    TextView currentSongTitle;
    TextView currentSongArtist;
    ImageView currentSongThumbnail;
    StorageUtil storageUtil;
    ProgressBar streamProgress;
    View playPauseWrapper;
    NowPlayingAdapter nowPlayingAdapter;
    PlayPauseButton mPlayPause;
    private SeekBar mSeekBar;
    private boolean duetoPlayPause = false;
    private MaterialIconView previous, next;

    ArrayList<Audio> nowPlaying;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageUtil = new StorageUtil(getApplicationContext());
        nowPlaying = storageUtil.loadAudio();
        int currentlyPlaying = storageUtil.loadAudioIndex();

        nowPlayingRecyclerView = (RecyclerView) findViewById(R.id.now_playing_list);
        playPauseWrapper = findViewById(R.id.playpausewrapper);
        currentSongTitle = (TextView) findViewById(R.id.song_title);
        currentSongArtist = (TextView) findViewById(R.id.song_artist);
        mPlayPause = (PlayPauseButton) findViewById(R.id.playpause);
        currentSongThumbnail = (ImageView) findViewById(R.id.song_thumbnail);
        streamProgress = (ProgressBar) findViewById(R.id.pb_playback_control);
        next = (MaterialIconView) findViewById(R.id.next);
        previous = (MaterialIconView) findViewById(R.id.previous);
        next.setOnClickListener(nextClickListener);
        previous.setOnClickListener(previousClickListener);
        mSeekBar = (SeekBar) findViewById(R.id.song_progress);
        nowPlayingAdapter = new NowPlayingAdapter(nowPlaying);
        nowPlayingRecyclerView.setAdapter(nowPlayingAdapter);
        playPauseWrapper.setOnClickListener(mPlayPauseListener);
        nowPlayingRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (NowPlayingActivity.this, nowPlayingRecyclerView, new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        presenter.playNewTrack(position, storageUtil);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(mContext, "Long Clicked " + nowPlaying.get(position)
                                .getTitle(), Toast.LENGTH_SHORT).show();
                    }
                }));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    presenter.seek(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        initializeNowPlaying(nowPlaying.get(currentlyPlaying));
    }


    private final View.OnClickListener mPlayPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoPlayPause = true;
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            } else {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.playStream();
                }
            }, 200);

        }
    };
    private final View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.nextStream();
                }
            }, 200);

        }
    };

    private final View.OnClickListener previousClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter.previousStream();
                }
            }, 200);

        }
    };


    private void initializeNowPlaying(Audio audio) {

        currentSongTitle.setText(audio.getTitle());
        currentSongTitle.setSelected(true);
        currentSongArtist.setText(audio.getArtist());
        if (!duetoPlayPause) {
            Glide.with(this).load(audio.getArtwork()).error(R.drawable.ic_empty_music2).into
                    (currentSongThumbnail);
        }
        duetoPlayPause = false;
        mSeekBar.setMax((int) audio.getDuration());
        mSeekBar.setProgress(getCurrentTrackPosition());
        seekbarHandler.postDelayed(new UpdateProgress(audio.getDuration()), 500);
    }


    Handler seekbarHandler = new Handler();

    public class UpdateProgress implements Runnable {

        int maxDuration = 0;

        public UpdateProgress(int maxDuration) {
            this.maxDuration = maxDuration;
        }

        @Override
        public void run() {

            long position = getCurrentTrackPosition();
            Timber.d("New position %d", position);
            mSeekBar.setProgress((int) position);

            if (position <= maxDuration) {
                mSeekBar.postDelayed(this, 50);
            } else mSeekBar.removeCallbacks(this);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public void onBackStackChanged() {

    }

    @Override
    public void initializeUI(Audio stream, boolean isPlaying) {
        initializeNowPlaying(stream);
    }

    @Override
    public void setLoading() {
        Timber.d("Media is streaming");
        streamProgress.setVisibility(View.VISIBLE);
        mPlayPause.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setToStopped() {
        Timber.d("Media is streaming");
        streamProgress.setVisibility(View.INVISIBLE);
        mPlayPause.setVisibility(View.VISIBLE);
        updateState(false);
    }

    @Override
    public void setToPlaying() {
        streamProgress.setVisibility(View.INVISIBLE);
        mPlayPause.setVisibility(View.VISIBLE);
        updateState(true);
    }

    @Override
    public void animateTo(Audio currentStream) {

    }

    @Override
    public void updateTimer(String timeLeft) {

    }

    @Override
    public void error(String error) {
        Toast.makeText(NowPlayingActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showStreamsDialog(List<Audio> streams) {

    }

    @Override
    public int getCurrentTrackPosition() {
        return presenter.getCurrentMediaPosition();
    }

    @Override
    public boolean isPlaying() {
        return presenter.isMediaPlaying();
    }

    @Override
    public void seek(int position) {
        presenter.seek(position);
    }

    @Override
    public void clickAnalyticsEvent(String categoryId, String actionId, String labelId, long
            value) {

    }

    @Override
    public void propagateEvent(Pair<String, Object> event) {

    }


    public void updateState(boolean isPlaying) {
        if (isPlaying) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            }
        } else {
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
        }
    }
}
