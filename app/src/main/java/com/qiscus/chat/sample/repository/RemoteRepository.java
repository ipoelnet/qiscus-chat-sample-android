package com.qiscus.chat.sample.repository;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qiscus.chat.sample.model.User;
import com.qiscus.sdk.Qiscus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by omayib on 22/09/17.
 */

public class RemoteRepository  implements Repository {
    private static final String TAG = "RemoteRepository";
    public RemoteRepository() {

    }
    ArrayList<User> alumnus = new ArrayList<>();

    @Override
    public void loadAll(final RepositoryCallback<List<User>> callback) {

        // remoteAlumnus = loadContent();

        RestClient.getInstance()
                .getApi()
                .getContacts()
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                JsonObject body = response.body();

                                alumnus = parseAlumnus(body);

                                Log.d("BODY","YES WE ARE PARSING ALUMNUS");
                                callback.onSucceed(alumnus);
                            } else {

                            }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });

        /*
        alumnus.add(new User(UUID.randomUUID().toString(),"Haris","Haris@email.com","Android Programmer"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Anang","Anang@email.com","Web Programmer"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Satya","Satya@email.com","Backend Programmer"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Henri","Henri@email.com","Backend Programmer"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Desi","Desi@email.com","Pengacara"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Sutris","Sutris@email.com","Desktop Programmer"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Dina","Dina@email.com","Android Programmer"));
        alumnus.add(new User(UUID.randomUUID().toString(),"Winda","Winda@email.com","Android Programmer"));
        */

    }

    private ArrayList<User> parseAlumnus(JsonObject body) {
        ArrayList<User> people = new ArrayList<>();
        if (!Qiscus.hasSetupUser()) {
            return people;
        }
        String currentUsername = Qiscus.getQiscusAccount().getUsername();
        JsonArray userArray = body.get("results").getAsJsonObject().get("users").getAsJsonArray();

        for(JsonElement element: userArray){
            JsonObject personElement = element.getAsJsonObject();
            String username = personElement.get("username").getAsString();
            if (!currentUsername.equals(username)) {
                User user = new User();
                user.setId(personElement.get("id").getAsString());
                user.setName(personElement.get("name").getAsString());
                user.setEmail(personElement.get("email").getAsString());
                user.setJob(personElement.get("name").getAsString());
                user.setAvatarUrl(personElement.get("avatar_url").getAsString());
                people.add(user);
            }
        }

        return people;
    }

    @Override
    public void save(List<User> users) {

    }

    @Override
    public void save(User user) {

    }

    private void loadContent() {

    }
}
