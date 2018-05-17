package com.qiscus.chat.ngobrel.data.repository.remote;

import com.google.gson.JsonObject;
import com.qiscus.chat.ngobrel.BuildConfig;
import com.qiscus.sdk.Qiscus;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Single;

/**
 * Created on : May 16, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public enum QiscusRestApi {
    INSTANCE;

    private final Api api;

    QiscusRestApi() {
        String baseUrl = "https://" + Qiscus.getAppId() + ".qiscus.com";

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(this::headersInterceptor)
                .addInterceptor(makeLoggingInterceptor(Qiscus.isEnableLog()))
                .build();

        api = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(Api.class);
    }

    private Response headersInterceptor(Interceptor.Chain chain) throws IOException {
        Request req = chain.request().newBuilder()
                .addHeader("QISCUS_SDK_SECRET", BuildConfig.QISCUS_SECRET)
                .build();
        return chain.proceed(req);
    }

    private HttpLoggingInterceptor makeLoggingInterceptor(boolean isDebug) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return logging;
    }

    public Single<JsonObject> addMember(long roomId, String email) {
        return api.addMember(roomId, Collections.singletonList(email))
                .flatMap(jsonObject -> postSystemEvent(roomId, "add_member", email));
    }

    public Single<JsonObject> removeMember(long roomId, String email) {
        return api.removeMember(roomId, Collections.singletonList(email))
                .flatMap(jsonObject -> postSystemEvent(roomId, "remove_member", email));
    }

    private Single<JsonObject> postSystemEvent(long roomId, String type, String objectEmail) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("room_id", roomId + "");
        payload.put("system_event_type", type);
        payload.put("subject_user_id", Qiscus.getQiscusAccount().getEmail());
        payload.put("object_user_id", Collections.singletonList(objectEmail));
        return api.postSystemEvent(payload);
    }

    private interface Api {
        @FormUrlEncoded
        @POST("/api/v2.1/rest/add_room_participants")
        Single<JsonObject> addMember(@Field("room_id") long roomId, @Field("user_ids[]") List<String> emails);

        @FormUrlEncoded
        @POST("/api/v2.1/rest/remove_room_participants")
        Single<JsonObject> removeMember(@Field("room_id") long roomId, @Field("user_ids[]") List<String> emails);

        @POST("/api/v2.1/rest/post_system_event_message")
        Single<JsonObject> postSystemEvent(@Body Map<String, Object> payload);
    }
}
