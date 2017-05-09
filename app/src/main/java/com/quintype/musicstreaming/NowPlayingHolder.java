package com.quintype.musicstreaming;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.quintype.musicstreaming.models.Audio;

/**
 * Created by akshaykoul on 06/04/17.
 */

public class NowPlayingHolder extends RecyclerView.ViewHolder {

    TextView trackTitle;
    TextView trackArtist;
    ImageView thumbnail;
    RequestManager glideRequestManager;

    public NowPlayingHolder(View itemView) {
        super(itemView);
        glideRequestManager = Glide.with(itemView.getContext());
    }

    public static NowPlayingHolder create(View view) {
        NowPlayingHolder trackHolder = new NowPlayingHolder(view);
        trackHolder.trackTitle = (TextView) view.findViewById(R.id.track_title);
        trackHolder.trackArtist = (TextView) view.findViewById(R.id.track_artist);
        trackHolder.thumbnail = (ImageView) view.findViewById(R.id.media_thumbnail);
        return trackHolder;
    }

    public void bind(Audio audio) {
        trackTitle.setText(audio.getTitle());
        trackArtist.setText(audio.getArtist());
        glideRequestManager.load(audio.getArtwork()).error(R.drawable.ic_empty_music2).into
                (thumbnail);
    }

}
