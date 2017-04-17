package com.quintype.musicstreaming.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.quintype.musicstreaming.interactor.MainInteractor;
import com.quintype.musicstreaming.notificationmanager.MainPresenter;

/**
 * Created by akshaykoul on 16/04/17.
 */

public abstract class PlayerActivity extends BaseFragmentActivity implements UIinteractor{

    protected MainInteractor mainInteractor;
    MainPresenter presenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInteractor = new MainInteractor(getApplication(), PreferenceManager
                .getDefaultSharedPreferences(getApplication()), (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE));
        presenter = new MainPresenter(this, mainInteractor);
    }
    @Override
    protected void onStart() {
        super.onStart();
        presenter.startService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unBindService();
    }
}
