package com.karol.vimos.vimosapp.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Vimos on 15/02/17.
 */

@IgnoreExtraProperties
public class User {

    public String uid;
    public String email;
    public String firebaseToken;
    public String name;
    public String nick;
    public String profession;
    public String lector;
    public int radius;
    public int score;
    public LatLng latLng;
    public float distance;
    public int red_detection, green_detection, orange_detection;


    public User() {
    }

    public User(String lector) {
        this.lector = lector;
    }

    public User(String uid, String email, String firebaseToken) {
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }

    public User(int red_detection, int green_detection, int orange_detection) {
        this.red_detection = red_detection;
        this.green_detection = green_detection;
        this.orange_detection = orange_detection;
    }

    public User(String name, String nick, String profession, int radius, int score, LatLng latLng, float distance) {
        this.name = name;
        this.nick = nick;
        this.radius = radius;
        this.score = score;
        this.latLng = latLng;
        this.distance = distance;
        this.profession = profession;

    }


    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getLector() {
        return lector;
    }

    public void setLector(String lector) {
        this.lector = lector;
    }

    public String getUid() {
        return uid;
    }

    public int getRed_detection() {
        return red_detection;
    }

    public void setRed_detection(int red_detection) {
        this.red_detection = red_detection;
    }

    public int getGreen_detection() {
        return green_detection;
    }

    public void setGreen_detection(int green_detection) {
        this.green_detection = green_detection;
    }

    public int getOrange_detection() {
        return orange_detection;
    }

    public void setOrange_detection(int orange_detection) {
        this.orange_detection = orange_detection;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
