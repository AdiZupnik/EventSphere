package com.example.eventsphere;

public class User {
    public String userId;
    public String name;
    public String mail;
    public String profile;

    public User(String mail, String name) {
        this.mail = mail;
        this.name = name;
    }

    public User() {
    }
}
