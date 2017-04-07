package com.quintype.musicstreaming.api;

import com.quintype.musicstreaming.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AKSHAY on 02-10-2015.
 */
public interface BulkApiService {

    public static final String QUERY_PARAM = "q";
    public static final String SOUNDCLOUD_CLIENT_ID = "client_id";

    @GET("/tracks")
    Call<List<Track>> searchTracks(@Query(QUERY_PARAM) String query, @Query(SOUNDCLOUD_CLIENT_ID)
            String clientId);


}