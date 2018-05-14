package com.qiscus.chat.ngobrel.repository;

import com.qiscus.chat.ngobrel.model.Person;

import java.util.List;

/**
 * Created by omayib on 22/09/17.
 */
public interface Repository {
    void loadAll(RepositoryCallback<List<Person>> callback);

    void save(List<Person> persons);

    void save(Person person);
}
