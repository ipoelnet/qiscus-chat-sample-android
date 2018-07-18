package com.qiscus.chat.sample.data.repository;

import android.net.Uri;

import com.qiscus.chat.sample.data.model.User;
import com.qiscus.chat.sample.util.Action;
import com.qiscus.sdk.data.remote.QiscusApi;

import java.util.List;

/**
 * Created on : January 30, 2018
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
public interface UserRepository {

    void login(String name, String email, String password, Action<User> onSuccess, Action<Throwable> onError);

    void getCurrentUser(Action<User> onSuccess, Action<Throwable> onError);

    void getUsers(Action<List<User>> onSuccess, Action<Throwable> onError);

    void updateProfile(String name, Action<User> onSuccess, Action<Throwable> onError);

    void logout();

    void uploadPhoto(String realPathFromURI, QiscusApi.ProgressListener progressListener,
                     Action<User> onSuccess, Action<Throwable> onError);
}
