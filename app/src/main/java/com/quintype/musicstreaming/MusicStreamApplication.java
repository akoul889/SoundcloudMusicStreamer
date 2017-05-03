package com.quintype.musicstreaming;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.quintype.musicstreaming.interactor.MainInteractor;
import com.quintype.musicstreaming.notificationmanager.MainPresenter;
import com.quintype.musicstreaming.utils.StorageUtil;

import timber.log.Timber;

/**
 * Created by akshaykoul on 05/04/17.
 */

public class MusicStreamApplication extends Application {

    protected MainInteractor mainInteractor;
    MainPresenter presenter;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        mainInteractor = new MainInteractor(this, PreferenceManager
                .getDefaultSharedPreferences(this), (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE), new StorageUtil
                (getApplicationContext()));

        presenter = new MainPresenter(mainInteractor);
    }

    public MainInteractor getMainInteractor() {
        return mainInteractor;
    }

    public MainPresenter getPresenter() {
        return presenter;
    }
}
