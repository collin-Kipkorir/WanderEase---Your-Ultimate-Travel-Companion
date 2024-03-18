package com.wanderease.travelcompanion;

public class Hotels {
    private String hotelImage;
    private String hotelName;
    private float hotelRating;
    private double hotelCost;
    private String placeId;
    private String hotelId;
    private double latitude;
    private double longitude;

    // Default constructor (required by Firebase)
    public Hotels() {
    }

    public Hotels(String hotelImage, String hotelName, float hotelRating, double hotelCost, double latitude, double longitude) {
        this.hotelImage = hotelImage;
        this.hotelName = hotelName;
        this.hotelRating = hotelRating;
        this.hotelCost = hotelCost;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    // Getters and setters for each field
    public String gethotelId() {
        return hotelId;
    }

    public void sethotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public float getHotelRating() {
        return hotelRating;
    }

    public void setHotelRating(float hotelRating) {
        this.hotelRating = hotelRating;
    }

    public double getHotelCost() {
        return hotelCost;
    }

    public void setHotelCost(double hotelCost) {
        this.hotelCost = hotelCost;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
