package com.quintype.musicstreaming;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.quintype.musicstreaming.models.Track;

/**
 * Created by akshaykoul on 06/04/17.
 */

public class TrackHolder extends RecyclerView.ViewHolder {

    TextView trackTitle;

    public TrackHolder(View itemView) {
        super(itemView);
    }

    public static TrackHolder create(View view) {
        TrackHolder trackHolder = new TrackHolder(view);
        trackHolder.trackTitle = (TextView) view.findViewById(R.id.track_title);
        return trackHolder;
    }

    public void bind(Track track) {
        trackTitle.setText(track.getTitle());
    }

}
