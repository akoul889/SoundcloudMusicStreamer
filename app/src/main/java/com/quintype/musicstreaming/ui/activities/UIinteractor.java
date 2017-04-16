package com.quintype.musicstreaming.ui.activities;

import com.quintype.musicstreaming.models.Audio;

import java.util.List;
public interface UIinteractor {

    void initializeUI(Audio stream, boolean isPlaying);

    void setLoading();

    void setToStopped();

    void setToPlaying();

    void animateTo(Audio currentStream);

    void updateTimer(String timeLeft);

    void error(String error);

    void showStreamsDialog(List<Audio> streams);
}