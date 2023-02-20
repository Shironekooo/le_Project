package com.example.le_androidapp;

import java.util.ArrayList;
import java.util.Date;

public class AppUser {
    public static ArrayList<AppUser> userArrayList = new ArrayList<>();

    private int id;
    private String username;
    private int badCount;
    private Date deleted;

    public AppUser(int id, String username, int badCount, Date deleted) {
        this.id = id;
        this.username = username;
        this.badCount = badCount;
        this.deleted = deleted;
    }

    public AppUser(int id, String username, int badCount) {
        this.id = id;
        this.username = username;
        this.badCount = badCount;
        deleted = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBadCount() {
        return badCount;
    }

    public void setBadCount(int badCount) {
        this.badCount = badCount;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
