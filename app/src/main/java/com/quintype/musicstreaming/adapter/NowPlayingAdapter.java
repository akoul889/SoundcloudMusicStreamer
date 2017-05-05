package com.quintype.musicstreaming.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quintype.musicstreaming.NowPlayingHolder;
import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.models.Audio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshaykoul on 06/04/17.
 */

public class NowPlayingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Audio> nowPlaying = new ArrayList<>();

    public NowPlayingAdapter(List<Audio> nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .layout_now_playing_item,
                parent, false);
        return NowPlayingHolder.create(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof NowPlayingHolder){
            ((NowPlayingHolder) holder).bind(nowPlaying.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return nowPlaying.size();
    }

    public void addTracks(List<Audio> nowPlaying) {
        int start = nowPlaying.size();
        nowPlaying.addAll(nowPlaying);
        int end = nowPlaying.size();
        notifyItemRangeInserted(start, end);
    }

    public void clearAll() {
        nowPlaying.clear();
        notifyDataSetChanged();
    }
}
