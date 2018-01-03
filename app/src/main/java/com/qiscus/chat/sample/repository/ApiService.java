package com.qiscus.chat.sample.repository;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by asyrof on 20/11/17.
 */

public interface ApiService {

    @GET("/api/contacts?show_all=true")
    Call<JsonObject> getContacts();

}
