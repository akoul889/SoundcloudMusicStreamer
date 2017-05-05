package com.quintype.musicstreaming.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.adapter.NowPlayingAdapter;
import com.quintype.musicstreaming.utils.StorageUtil;

public class NowPlayingActivity extends AppCompatActivity {

    RecyclerView nowPlayingRecyclerView;
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
        nowPlayingRecyclerView = (RecyclerView) findViewById(R.id.now_playing_list);
        nowPlayingAdapter = new NowPlayingAdapter(storageUtil.loadAudio());
        nowPlayingRecyclerView.setAdapter(nowPlayingAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

}
