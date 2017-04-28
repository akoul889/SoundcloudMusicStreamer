package com.quintype.musicstreaming.models;

import java.util.ArrayList;

/**
 * Created by akshaykoul on 27/04/17.
 */

public class NowPlaying {
    ArrayList<Audio> mNowPlayingList;
    int mNowplayingPosition;

    public NowPlaying(ArrayList<Audio> mNowPlayingList, int mNowplayingPosition) {
        this.mNowPlayingList = mNowPlayingList;
        this.mNowplayingPosition = mNowplayingPosition;
    }

    public ArrayList<Audio> getmNowPlayingList() {
        return mNowPlayingList;
    }

    public void setmNowPlayingList(ArrayList<Audio> mNowPlayingList) {
        this.mNowPlayingList = mNowPlayingList;
    }

    public int getmNowplayingPosition() {
        return mNowplayingPosition;
    }

    public void setmNowplayingPosition(int mNowplayingPosition) {
        this.mNowplayingPosition = mNowplayingPosition;
    }
}
