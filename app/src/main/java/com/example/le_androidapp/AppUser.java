package com.example.le_androidapp;

import java.util.ArrayList;
import java.util.Date;

public class AppUser {
    public static ArrayList<AppUser> userArrayList = new ArrayList<>();
    public static String USER_EDIT_EXTRA = "userEdit";

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

    public static AppUser getUserForId(int passedUserId) {
        for (AppUser appUser : userArrayList) {
            if (appUser.getId() == passedUserId) {
                return appUser;
            }
        }
        return null;
    }

    public static ArrayList<AppUser> nonDeletedNotes() {
        ArrayList<AppUser> nonDeleted = new ArrayList<>();
        for(AppUser appUser : userArrayList) {
            if(appUser.getDeleted() == null)
                nonDeleted.add(appUser);
        }

        return nonDeleted;
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
