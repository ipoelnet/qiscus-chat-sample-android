package com.qiscus.chat.ngobrel.repository;

import com.qiscus.chat.ngobrel.model.Person;

import java.util.List;

/**
 * Created by omayib on 22/09/17.
 */
public class CacheRepository implements Repository, CachedData {

    @Override
    public void loadAll(RepositoryCallback<List<Person>> callback) {
        callback.onSucceed(alumnus);
    }

    @Override
    public void save(List<Person> persons) {
        alumnus.addAll(persons);
    }

    @Override
    public void save(Person person) {

    }
}
