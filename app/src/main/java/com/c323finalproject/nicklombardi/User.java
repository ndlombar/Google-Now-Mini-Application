package com.c323finalproject.nicklombardi;

import android.graphics.Bitmap;

public class User {

    private String username;
    private Bitmap profile_pic;
    private String number;

    public User(String username, Bitmap profile_pic, String number) {
        this.username = username;
        this.profile_pic = profile_pic;
        this.number = number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(Bitmap profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
