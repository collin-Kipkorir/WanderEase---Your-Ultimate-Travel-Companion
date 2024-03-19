package com.wanderease.travelcompanion;

public class Booking {
    private String hotelName;
    private String hotelCost;
    private String hotelRating;
    private String hotelImage;
    private int days;
    private int nights;
    private String checkInDate;

    public Booking(String hotelName, String hotelCost, String hotelRating, String hotelImage, int days, int nights, String checkInDate) {
        this.hotelName = hotelName;
        this.hotelCost = hotelCost;
        this.hotelRating = hotelRating;
        this.hotelImage = hotelImage;
        this.days = days;
        this.nights = nights;
        this.checkInDate = checkInDate;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelCost() {
        return hotelCost;
    }

    public void setHotelCost(String hotelCost) {
        this.hotelCost = hotelCost;
    }

    public String getHotelRating() {
        return hotelRating;
    }

    public void setHotelRating(String hotelRating) {
        this.hotelRating = hotelRating;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
}

