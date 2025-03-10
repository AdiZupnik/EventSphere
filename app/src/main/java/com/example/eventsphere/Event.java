package com.example.eventsphere;

public class Event {
    private String type;
    private String date;
    private double price;
    private String time;
    private String sellerEmail;
    private String sellerPhone;
    private String searchField;  // Search optimization field
    private double latitude;
    private double longitude;

    public Event() {
        // Required empty constructor
    }

    public Event(String type, String date, double price, String time,
                 String sellerEmail, String sellerPhone, double lat, double lng) {
        this.type = type;
        this.date = date;
        this.price = price;
        this.time = time;
        this.sellerEmail = sellerEmail;
        this.sellerPhone = sellerPhone;
        this.latitude = lat;
        this.longitude = lng;
        this.searchField = type.trim().toLowerCase();
    }

    // Add these getters/setters
    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }
    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
