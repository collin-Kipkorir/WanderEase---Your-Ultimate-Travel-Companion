package com.wanderease.travelcompanion;

public class Hotel {
    private String placeId;
    private String name;
    private String rating;
    private String cost;
    private String distance;
    private String imageUrl;
    private String url;

    //Agent details
    private String agentName;
    private String agentNumber;
    private String placeDescription;

    public Hotel() {
        // Default constructor required for Firebase
    }

    public Hotel(String name, String rating, String cost, String distance, String imageUrl, String url, String agentName, String agentNumber, String placeDescription) {
        this.name = name;
        this.rating = rating;
        this.cost = cost;
        this.distance = distance;
        this.imageUrl = imageUrl;
        this.url = url;
        this.agentName = agentName;
        this.agentNumber = agentNumber;
        this.placeDescription = placeDescription;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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

    // Getter and setter for agentName
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    // Getter and setter for agentNumber
    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    // Getter and setter for placeDescription
    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }
}
