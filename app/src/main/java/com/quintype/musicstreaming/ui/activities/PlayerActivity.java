package com.quintype.musicstreaming.ui.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.quintype.musicstreaming.interactor.MainInteractor;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.notificationmanager.MainPresenter;

import java.util.List;

/**
 * Created by akshaykoul on 16/04/17.
 */

public abstract class PlayerActivity extends BaseFragmentActivity{

    protected MainInteractor mainInteractor;
    MainPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainInteractor = new MainInteractor(getApplication(), PreferenceManager
                .getDefaultSharedPreferences(getApplication()), (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE));
        presenter = new MainPresenter(uIinteractor, mainInteractor);
    }

    UIinteractor uIinteractor = new UIinteractor() {
        @Override
        public void initializeUI(Audio stream, boolean isPlaying) {

        }

        @Override
        public void setLoading() {

        }

        @Override
        public void setToStopped() {

        }

        @Override
        public void setToPlaying() {

        }

        @Override
        public void animateTo(Audio currentStream) {

        }

        @Override
        public void updateTimer(String timeLeft) {

        }

        @Override
        public void error(String error) {

        }

        @Override
        public void showStreamsDialog(List<Audio> streams) {

        }
    };

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
