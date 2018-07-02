package com.qiscus.chat.sample.repository;

import java.util.List;

import com.qiscus.chat.sample.model.User;

/**
 * Created by omayib on 22/09/17.
 */

public interface Repository {
    public void loadAll(RepositoryCallback<List<User>> callback);
    public void save(List<User> users);
    public void save(User user);
}
