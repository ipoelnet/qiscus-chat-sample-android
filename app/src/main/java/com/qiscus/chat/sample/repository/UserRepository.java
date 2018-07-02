package com.qiscus.chat.sample.repository;

import com.qiscus.chat.sample.model.User;
import com.qiscus.chat.sample.util.Action;

import java.util.List;

public interface UserRepository {
    void login(String name, String email, String password, Action<User> onSuccess, Action<Throwable> onError);

    void getCurrentUser(Action<User> onSuccess, Action<Throwable> onError);

    void getUsers(Action<List<User>> onSuccess, Action<Throwable> onError);

    void updateProfile(String name, Action<User> onSuccess, Action<Throwable> onError);

    void logout();
}
