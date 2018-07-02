package com.qiscus.chat.sample.repository;

import java.util.List;

import com.qiscus.chat.sample.model.User;

/**
 * Created by omayib on 22/09/17.
 */

public class CacheRepository implements Repository, CachedData {


    @Override
    public void loadAll(RepositoryCallback<List<User>> callback) {
        callback.onSucceed(alumnus);
    }

    @Override
    public void save(List<User> users) {
        alumnus.addAll(users);
    }

    @Override
    public void save(User user) {

    }
}
