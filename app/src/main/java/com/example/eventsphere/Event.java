package com.example.eventsphere;

public class Event {
    public String type;
    public String date;
    public String time;
    public double price;
    public String searchField;

    public Event() {
    }

    public Event(String type, String date, double price, String time) {
        this.type = type;
        this.date = date;
        this.price = price;
        this.time = time;
        this.searchField = (type).toLowerCase();
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
}
