package com.vic.wanandroid.utils;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DatabaseHelper {
    private final Realm realm;

    private DatabaseHelper(Context context) {
        Realm.init(context);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("wanandroid.db")
                .schemaVersion(1)
                .build();
        realm = Realm.getInstance(configuration);
    }

    public static DatabaseHelper init(Context context) {
        return new DatabaseHelper(context);
    }

    public void close() {
        realm.close();
    }

    public <T extends RealmObject> void add(T t) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(t);
            }
        });
    }

    public RealmResults findAll(Class cl) {
        return realm.where(cl).findAll();
    }

    public void deleteResult(Class cl, int index) {
        RealmResults results = realm.where(cl).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFromRealm(index);
            }
        });
    }

    public void deleteAllResult(Class cl) {
        RealmResults results = realm.where(cl).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    public Realm getRealm() {
        return this.realm;
    }
}
