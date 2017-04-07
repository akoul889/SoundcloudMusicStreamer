package com.quintype.musicstreaming.api;

import com.quintype.musicstreaming.utils.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SoundCloudApiClient {

    private static final String TAG = SoundCloudApiClient.class.getName();
    private static BulkApiService bulkApiService;

    public static BulkApiService getApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//        TimberLoggingInterceptor timberLoggingInterceptor =  new TimberLoggingInterceptor();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.soundcloud.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        bulkApiService = retrofit.create(BulkApiService.class);
        return bulkApiService;
    }
}
