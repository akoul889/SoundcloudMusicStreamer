package com.quintype.musicstreaming.ui.fragments;

/**
 * Created by akshaykoul on 19/04/17.
 */

public interface MusicFragmentCallbacks extends FragmentCallbacks{

    public int getCurrentTrackPosition();

    public boolean isPlaying();

    public void seek(int position);
}
