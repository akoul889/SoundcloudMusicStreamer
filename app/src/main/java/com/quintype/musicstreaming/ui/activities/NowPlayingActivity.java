package com.quintype.musicstreaming.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.adapter.NowPlayingAdapter;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.utils.StorageUtil;

import java.util.ArrayList;

public class NowPlayingActivity extends AppCompatActivity {

    RecyclerView nowPlayingRecyclerView;
    TextView currentSongTitle;
    TextView currentSongArtist;
    ImageView currentSongThumbnail;
    StorageUtil storageUtil;
    NowPlayingAdapter nowPlayingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storageUtil = new StorageUtil(getApplicationContext());
        ArrayList<Audio> nowPlaying = storageUtil.loadAudio();
        int currentlyPlaying = storageUtil.loadAudioIndex();

        nowPlayingRecyclerView = (RecyclerView) findViewById(R.id.now_playing_list);
        currentSongTitle = (TextView) findViewById(R.id.song_title);
        currentSongArtist = (TextView) findViewById(R.id.song_artist);
        currentSongThumbnail = (ImageView) findViewById(R.id.song_thumbnail);
        nowPlayingAdapter = new NowPlayingAdapter(nowPlaying);
        nowPlayingRecyclerView.setAdapter(nowPlayingAdapter);
        currentSongTitle.setText(nowPlaying.get(currentlyPlaying).getTitle());
        currentSongArtist.setText(nowPlaying.get(currentlyPlaying).getArtist());
        Glide.with(this).load(nowPlaying.get(currentlyPlaying).getArtwork()).into(currentSongThumbnail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

}
