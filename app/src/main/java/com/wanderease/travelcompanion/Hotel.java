package com.wanderease.travelcompanion;

import android.os.Parcel;
import android.os.Parcelable;
public class Hotel {
    private String name;
    private String rating;
    private String cost;
    private String distance;
    private String imageUrl;
    private String url;

    public Hotel() {
        // Default constructor required for Firebase
    }

    public Hotel(String name, String rating, String cost, String distance, String imageUrl, String url) {
        this.name = name;
        this.rating = rating;
        this.cost = cost;
        this.distance = distance;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
