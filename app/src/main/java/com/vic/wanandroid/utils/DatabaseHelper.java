package com.vic.wanandroid.utils;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
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

    public <T extends RealmObject> void add(T t){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(t);
            }
        });
    }

    public RealmResults findAll(Class cl){
        return realm.where(cl).findAll();
    }

    public Realm getRealm(){
        return this.realm;
    }
}
