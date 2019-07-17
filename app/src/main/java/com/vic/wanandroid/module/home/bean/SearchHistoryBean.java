package com.vic.wanandroid.module.home.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SearchHistoryBean extends RealmObject {
    private String name;
    @PrimaryKey
    private long currentTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
