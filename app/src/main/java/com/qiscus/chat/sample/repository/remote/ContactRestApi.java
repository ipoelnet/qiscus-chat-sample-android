package com.qiscus.chat.sample.repository.remote;

import com.google.gson.JsonObject;
import com.qiscus.chat.sample.util.Configuration;
import com.qiscus.sdk.Qiscus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Single;

public enum ContactRestApi {
    INSTANCE;

    private final Api api;

    ContactRestApi() {
        String baseUrl = Configuration.BASE_URL;

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(Api.class);
    }



    public Single<JsonObject> getContacts() {
        return api.getContacts();
    }

    private interface Api {
        @GET("/api/contacts?show_all=true")
        Single<JsonObject> getContacts();
    }
}