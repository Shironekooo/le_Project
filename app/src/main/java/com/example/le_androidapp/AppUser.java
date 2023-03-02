package com.example.le_androidapp;

import java.util.ArrayList;

public class AppUser {
    public static ArrayList<AppUser> userArrayList = new ArrayList<>();
    public static String USER_EDIT_EXTRA = "userEdit";

    private int id;
    private String username;
    private int badCount;
    private int selected;

    public AppUser(int id, String username, int badCounter, int selected) {
        this.id = id;
        this.username = username;
        this.badCount = badCount;
        this.selected = selected;
    }

    public static AppUser getUserForId(int passedUserId) {
        for (AppUser appUser : userArrayList) {
            if (appUser.getId() == passedUserId) {
                return appUser;
            }
        }
        return null;
    }

    public static AppUser currentlySelectedUser() {
        for (AppUser appUser : userArrayList) {
            if (appUser.getSelected() == 1) return appUser;
        }

        return null;
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

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
