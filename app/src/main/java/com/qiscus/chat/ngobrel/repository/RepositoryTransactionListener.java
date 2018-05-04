package com.qiscus.chat.ngobrel.repository;

import java.util.List;

import com.qiscus.chat.ngobrel.model.Person;

/**
 * Created by omayib on 22/09/17.
 */

public interface RepositoryTransactionListener {
    public void onLoadAlumnusSucceeded(List<Person> alumnus);
}
