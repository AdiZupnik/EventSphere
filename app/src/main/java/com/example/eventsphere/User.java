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

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
