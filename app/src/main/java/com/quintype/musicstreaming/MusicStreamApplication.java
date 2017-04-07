package com.quintype.musicstreaming;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by akshaykoul on 05/04/17.
 */

public class MusicStreamApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }
    }
}
