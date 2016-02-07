package com.asherfischbaum.hackapint;

/**
 * Created by Andrea on 07/02/2016.
 */
public class User {
    private String name, KEY, matchedKEY, gender;
    private double lat, lng;
    private boolean hasAccepted;
    private int age;

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public void setMatchedUser(String matchedUser) {
        this.matchedKEY = matchedUser;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setHasAccepted(boolean hasAccepted) {
        this.hasAccepted = hasAccepted;
    }
}
