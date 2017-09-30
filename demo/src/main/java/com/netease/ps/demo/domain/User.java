package com.netease.ps.demo.domain;

/**
 * Created by hzzhangxuan on 2017/9/25.
 */
public class User {
    String name;

    public User(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

}
