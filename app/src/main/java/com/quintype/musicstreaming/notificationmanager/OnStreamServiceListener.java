package com.quintype.musicstreaming.notificationmanager;

import com.quintype.musicstreaming.models.Audio;

import java.util.List;

public interface OnStreamServiceListener {

    void streamStopped();

    void updateTimerValue(String timeLeft);

    void restoreUI(Audio stream, boolean isPlaying);

    void setLoading();

    void streamPlaying();

    void animateTo(Audio currentStream);

    void error(String string);

    void showAllStreams(List<Audio> streams);
}
