package com.quintype.musicstreaming.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.quintype.musicstreaming.R;
import com.quintype.musicstreaming.adapter.TrackAdapter;
import com.quintype.musicstreaming.api.SoundCloudApiClient;
import com.quintype.musicstreaming.models.Track;
import com.quintype.musicstreaming.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class SoundcloudListFragment extends Fragment {


    RecyclerView rvRecyclerView;
    FrameLayout llMainContainer;
    LinearLayout llRetry;
    AppCompatButton retryButton;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeContainer;
    TrackAdapter trackAdapter;

    private FragmentCallbacks callbacks;

    public SoundcloudListFragment() {
        // Required empty public constructor
    }

    public static SoundcloudListFragment create() {
        SoundcloudListFragment fragment = new SoundcloudListFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_soundcloud_list, container, false);
        rvRecyclerView = (RecyclerView) view.findViewById(R.id
                .fragment_soundcloud_list_rv_recyclerview);
        llMainContainer = (FrameLayout) view.findViewById(R.id
                .fragment_soundcloud_ll_main_container);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_tag_fragment);

        trackAdapter = new TrackAdapter(callbacks);
        rvRecyclerView.setAdapter(trackAdapter);


        Timber.d("Making the Call");
        SoundCloudApiClient.getApiService().searchTracks("Layla", getString(R.string
                .soundcloud_client_id)).enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    trackAdapter.addTracks(response.body());
                    progressBar.setVisibility(View.GONE);
                    callbacks.propagateEvent(new Pair<String, Object>(Constants
                            .EVENT_UPDATE_PLAYLIST, response.body()));
                } else {
                    Timber.d("Call failure");
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {

                Timber.d("Call failure");
            }
        });
        return view;
    }

//    public void onButtonPressed(Uri uri) {
//        if (callbacks != null) {
//            callbacks.propagateEvent();
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallbacks) {
            callbacks = (FragmentCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

}
