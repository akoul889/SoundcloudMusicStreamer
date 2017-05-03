package com.quintype.musicstreaming.notificationmanager;

import com.quintype.musicstreaming.interactor.MainInteractor;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.ui.activities.UIinteractor;
import com.quintype.musicstreaming.utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niels Masdorp (NielsMasdorp)
 */
public class MainPresenter implements OnStreamServiceListener {

    private List<UIinteractor> uIinteractors = new ArrayList<>();
    private MainInteractor interactor;

    public MainPresenter(MainInteractor interactor) {

        this.interactor = interactor;
    }

    public void startService() {

        interactor.startService(this);
    }

    public void unBindService() {

        interactor.unbindService();
    }

    public void playStream() {

        interactor.playStream();
    }

    public void nextStream() {

        interactor.nextStream();
    }

    public void previousStream() {

        interactor.previousStream();
    }

    public void setSleepTimer(int ms) {

        interactor.setSleepTimer(ms);
    }

    public void getAllStreams() {

        interactor.getAllStreams();
    }

    public boolean isStreamWifiOnly() {

        return interactor.isStreamWifiOnly();
    }

    public void setStreamWifiOnly(boolean checked) {

        interactor.setStreamWifiOnly(checked);
    }

    public void streamStopped() {

        for (UIinteractor view : uIinteractors) {

            view.setToStopped();
        }
    }

    @Override
    public void updateTimerValue(String timeLeft) {

        for (UIinteractor view : uIinteractors) {

            view.updateTimer(timeLeft);
        }
    }

    public void restoreUI(Audio stream, boolean isPlaying) {

        for (UIinteractor view : uIinteractors) {

            view.initializeUI(stream, isPlaying);
        }
    }

    public void setLoading() {
        for (UIinteractor view : uIinteractors) {

            view.setLoading();
        }
    }

    public void streamPlaying() {
        for (UIinteractor view : uIinteractors) {

            view.setToPlaying();
        }
    }

    public void animateTo(Audio currentStream) {
        for (UIinteractor view : uIinteractors) {

            view.animateTo(currentStream);
        }
    }

    @Override
    public void error(String error) {
        for (UIinteractor view : uIinteractors) {

            view.error(error);
        }
    }

    @Override
    public void showAllStreams(List<Audio> streams) {
        for (UIinteractor view : uIinteractors) {

            view.showStreamsDialog(streams);
        }
    }

    public void updatePlaylist(ArrayList<Audio> streams, StorageUtil storage) {
        storage.storeAudio(streams);
        interactor.updatePlaylist(streams);
    }

    public void playNewTrack(ArrayList<Audio> streams, int audioIndex, StorageUtil storage) {
        updatePlaylist(streams, storage);
        storage.storeAudioIndex(audioIndex);
        interactor.playNewStream(audioIndex);
    }

    public int getCurrentMediaPosition() {
        return interactor.getCurrentMediaPosition();
    }

    public boolean isMediaPlaying() {
        return interactor.isMediaPlaying();
    }

    public void seek(int pos) {
        interactor.seek(pos);
    }

    public void addInteractor(UIinteractor view) {
        uIinteractors.add(view);
    }

    public void removeInteractor(UIinteractor view) {
        uIinteractors.remove(view);
    }

}
