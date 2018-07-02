package com.qiscus.chat.sample.repository;

import java.util.List;

import com.qiscus.chat.sample.model.User;

/**
 * Created by omayib on 22/09/17.
 */

public interface RepositoryTransactionListener {
    public void onLoadAlumnusSucceeded(List<User> alumnus);
}
