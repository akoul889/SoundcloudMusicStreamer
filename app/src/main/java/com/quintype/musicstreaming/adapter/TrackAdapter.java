package com.quintype.musicstreaming.adapter;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.TrackHolder;
import com.quintype.musicstreaming.models.Track;
import com.quintype.musicstreaming.ui.fragments.FragmentCallbacks;
import com.quintype.musicstreaming.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshaykoul on 06/04/17.
 */

public class TrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Track> tracks = new ArrayList<>();
    FragmentCallbacks fragmentCallbacks;

    public TrackAdapter(FragmentCallbacks fragmentCallbacks) {
        this.fragmentCallbacks = fragmentCallbacks;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_track_item,
                parent, false);
        return TrackHolder.create(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TrackHolder) {
            ((TrackHolder) holder).bind(tracks.get(position));
            ((TrackHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentCallbacks.propagateEvent(new Pair<String, Object>(Constants
                            .EVENT_TRACK_CLICK, position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void addTracks(List<Track> newTracks) {
        int start = tracks.size();
        tracks.addAll(newTracks);
        int end = tracks.size();
        notifyItemRangeInserted(start, end);
    }
}
