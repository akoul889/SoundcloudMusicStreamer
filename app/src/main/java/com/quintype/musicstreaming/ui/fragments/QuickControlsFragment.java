/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.quintype.musicstreaming.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.models.Audio;
import com.quintype.musicstreaming.utils.Constants;
import com.quintype.musicstreaming.utils.ImageUtils;
import com.quintype.musicstreaming.widgets.PlayPauseButton;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class QuickControlsFragment extends Fragment {


    public static View topContainer;
    private ProgressBar mProgress;
    private SeekBar mSeekBar;
    int newpos = 0;
    public Runnable mUpdateProgress = new Runnable() {

        @Override
        public void run() {

            int dummyPosition = 10;
//            long position = MusicPlayer.position();
//            mProgress.setProgress((int) position);
//            mSeekBar.setProgress((int) position);
            mProgress.setProgress((int) newpos);
            mSeekBar.setProgress((int) newpos);

            newpos += dummyPosition;
//            if (MusicPlayer.isPlaying()) {
            if (newpos < 100) {
                mProgress.postDelayed(mUpdateProgress, 50);
            } else mProgress.removeCallbacks(this);

        }
    };
    private PlayPauseButton mPlayPause, mPlayPauseExpanded;
    private TextView mTitle, mTitleExpanded;
    private TextView mArtist, mArtistExpanded;
    private ImageView mAlbumArt, mBlurredArt;
    private View rootView;
    private View playPauseWrapper, playPauseWrapperExpanded;
    private MaterialIconView previous, next;
    private ProgressBar streamProgress, streamProgressExpanded;
    private boolean duetoplaypause = false;
    private final View.OnClickListener mPlayPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            } else {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callbacks.propagateEvent(new Pair<String, Object>(Constants
                            .EVENT_PLAY_PAUSE_CLICK
                            , Constants.EVENT_PLAY_PAUSE_CLICK));
                }
            }, 200);

        }
    };
    private final View.OnClickListener mPlayPauseExpandedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            duetoplaypause = true;
            if (!mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(true);
                mPlayPauseExpanded.startAnimation();
            } else {
                mPlayPauseExpanded.setPlayed(false);
                mPlayPauseExpanded.startAnimation();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    callbacks.propagateEvent(new Pair<String, Object>(Constants
                            .EVENT_PLAY_PAUSE_CLICK
                            , Constants.EVENT_PLAY_PAUSE_CLICK));
                }
            }, 200);

        }
    };
    private FragmentCallbacks callbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);
        this.rootView = rootView;

        mPlayPause = (PlayPauseButton) rootView.findViewById(R.id.play_pause);
        mPlayPauseExpanded = (PlayPauseButton) rootView.findViewById(R.id.playpause);
        playPauseWrapper = rootView.findViewById(R.id.play_pause_wrapper);
        playPauseWrapperExpanded = rootView.findViewById(R.id.playpausewrapper);
        playPauseWrapper.setOnClickListener(mPlayPauseListener);
        playPauseWrapperExpanded.setOnClickListener(mPlayPauseExpandedListener);
        mProgress = (ProgressBar) rootView.findViewById(R.id.song_progress_normal);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.song_progress);
        mTitle = (TextView) rootView.findViewById(R.id.title);
        mArtist = (TextView) rootView.findViewById(R.id.artist);
        mTitleExpanded = (TextView) rootView.findViewById(R.id.song_title);
        mArtistExpanded = (TextView) rootView.findViewById(R.id.song_artist);
        mAlbumArt = (ImageView) rootView.findViewById(R.id.album_art_nowplayingcard);
        mBlurredArt = (ImageView) rootView.findViewById(R.id.blurredAlbumart);
        next = (MaterialIconView) rootView.findViewById(R.id.next);
        previous = (MaterialIconView) rootView.findViewById(R.id.previous);
        topContainer = rootView.findViewById(R.id.topContainer);
        streamProgress = (ProgressBar) rootView.findViewById(R.id.pb_playback_control);
        streamProgressExpanded = (ProgressBar) rootView.findViewById(R.id
                .pb_playback_control_expanded);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mProgress
                .getLayoutParams();
        mProgress.measure(0, 0);
        layoutParams.setMargins(0, -(mProgress.getMeasuredHeight() / 2), 0, 0);
        mProgress.setLayoutParams(layoutParams);

        mPlayPause.setColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color
                .colorAccent));
        mPlayPauseExpanded.setColor(Color.WHITE);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
//                    MusicPlayer.seek((long) i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        MusicPlayer.next();
                    }
                }, 200);

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        MusicPlayer.previous(getActivity(), false);
                    }
                }, 200);

            }
        });


//        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
//
//        if (PreferencesUtility.getInstance(getActivity()).isGesturesEnabled()) {
//            new SlideTrackSwitcher() {
//                @Override
//                public void onClick() {
//                    NavigationUtils.navigateToNowplaying(getActivity(), false);
//                }
//            }.attach(rootView.findViewById(R.id.root_view));
//        }


        return rootView;
    }

    public void updateNowplayingCard(Audio stream, boolean isPlaying) {
//        mTitle.setText(MusicPlayer.getTrackName());
//        mArtist.setText(MusicPlayer.getArtistName());
//        mTitleExpanded.setText(MusicPlayer.getTrackName());
//        mArtistExpanded.setText(MusicPlayer.getArtistName());
        mTitle.setText(stream.getTitle());
        mArtist.setText(stream.getArtist());
        mTitleExpanded.setText(stream.getTitle());
        mArtistExpanded.setText(stream.getArtist());
        if (!duetoplaypause) {
//            ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(MusicPlayer
//                            .getCurrentAlbumId()).toString(), mAlbumArt,
//                    new DisplayImageOptions.Builder().cacheInMemory(true)
//                            .showImageOnFail(R.drawable.ic_empty_music2)
//                            .resetViewBeforeLoading(true)
//                            .build(), new ImageLoadingListener() {
//                        @Override
//                        public void onLoadingStarted(String imageUri, View view) {
//
//                        }
//
//                        @Override
//                        public void onLoadingFailed(String imageUri, View view, FailReason
//                                failReason) {
//                            Bitmap failedBitmap = ImageLoader.getInstance().loadImageSync
//                                    ("drawable://" + R.drawable.ic_empty_music2);
//                            if (getActivity() != null)
//                                new setBlurredAlbumArt().execute(failedBitmap);
//                        }
//
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap
//                                loadedImage) {
//                            if (getActivity() != null)
//                                new setBlurredAlbumArt().execute(loadedImage);
//
//                        }
//
//                        @Override
//                        public void onLoadingCancelled(String imageUri, View view) {
//
//                        }
//                    });

            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_empty_music2);
            new setBlurredAlbumArt().execute(icon);
        }
        duetoplaypause = false;
//        mProgress.setMax((int) MusicPlayer.duration());
//        mSeekBar.setMax((int) MusicPlayer.duration());
        mProgress.setMax((int) 100);
        mSeekBar.setMax((int) 100);
        mProgress.postDelayed(mUpdateProgress, 10);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
        topContainer = rootView.findViewById(R.id.topContainer);

    }

    public void updateState(boolean isPlaying) {
        if (isPlaying) {
            if (!mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
                mPlayPause.startAnimation();
            }
            if (!mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(true);
                mPlayPauseExpanded.startAnimation();
            }
        } else {
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(false);
                mPlayPause.startAnimation();
            }
            if (mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(false);
                mPlayPauseExpanded.startAnimation();
            }
        }
    }

    private class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Bitmap... loadedImage) {
            Drawable drawable = null;
            try {
                drawable = ImageUtils.createBlurredImageFromBitmap(loadedImage[0], getActivity(),
                        6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                if (mBlurredArt.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    mBlurredArt.getDrawable(),
                                    result
                            });
                    mBlurredArt.setImageDrawable(td);
                    td.startTransition(400);

                } else {
                    mBlurredArt.setImageDrawable(result);
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }

    public void initializePlayer(Audio stream, boolean isPlaying) {
        updateNowplayingCard(stream, isPlaying);
        updateState(isPlaying);
    }

    public void setLoading() {

        streamProgress.setVisibility(View.VISIBLE);
        streamProgressExpanded.setVisibility(View.VISIBLE);
        mPlayPause.setVisibility(View.INVISIBLE);
        mPlayPauseExpanded.setVisibility(View.INVISIBLE);
//        nextButton.setEnabled(false);
//        previousButton.setEnabled(false);
//        showSoundsButton.setEnabled(false);
//
//        playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause_48dp));
    }

    public void setToPlaying() {

        stopLoading();

        updateState(true);
    }

    public void setToStopped() {

        stopLoading();
        updateState(false);
    }

    private void stopLoading() {

        streamProgress.setVisibility(View.INVISIBLE);
        streamProgressExpanded.setVisibility(View.INVISIBLE);
        mPlayPause.setVisibility(View.VISIBLE);
        mPlayPauseExpanded.setVisibility(View.VISIBLE);
//        nextButton.setEnabled(true);
//        previousButton.setEnabled(true);
//        showSoundsButton.setEnabled(true);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallbacks) {
            callbacks = (FragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }


}
