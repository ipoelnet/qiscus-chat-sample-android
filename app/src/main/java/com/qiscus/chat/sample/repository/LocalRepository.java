package com.qiscus.chat.sample.repository;

import java.util.ArrayList;
import java.util.List;

import com.qiscus.chat.sample.model.User;
import com.qiscus.chat.sample.db.PersonPersistance;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by omayib on 22/09/17.
 */

public class LocalRepository implements Repository {
    private static final String TAG = "LocalRepository";

    public Realm realm;

    public LocalRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void loadAll(RepositoryCallback<List<User>> callback) {
        RealmResults<PersonPersistance> personPersistances = realm.where(PersonPersistance.class).findAll();
        ArrayList<User> alumnus = new ArrayList<>();
        for (int i = 0; i < personPersistances.size(); i++) {
            PersonPersistance item = personPersistances.get(i);
            alumnus.add(new User(item.getId(),item.getName(),item.getEmail(),item.getJob()));
        }
        callback.onSucceed(alumnus);
    }

    @Override
    public void save(final List<User> users) {
        realm.beginTransaction();
        for (User p :
                users) {
            PersonPersistance personPersistance;
            personPersistance = realm.where(PersonPersistance.class).equalTo("id", p.getId()).findFirst();
            if (personPersistance == null) {
                personPersistance = realm.createObject(PersonPersistance.class);
            }

            personPersistance.setEmail(p.getEmail());
            personPersistance.setId(p.getId());
            personPersistance.setJob(p.getJob());
            personPersistance.setName(p.getName());
        }
        realm.commitTransaction();

    }

    @Override
    public void save(User user) {

    }
}
