package com.quintype.musicstreaming.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.quintype.musicstreaming.MusicStreamApplication;
import com.quintype.musicstreaming.notificationmanager.MainPresenter;

/**
 * Created by akshaykoul on 16/04/17.
 */

public abstract class PlayerActivity extends MusicFragmentActivity implements UIinteractor {

    MainPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = ((MusicStreamApplication) getApplication()).getPresenter();
        presenter.addInteractor(this);
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

    @Override
    protected void onDestroy() {
        presenter.removeInteractor(this);
        super.onDestroy();
    }
}
