package com.atmsimulator.model;

public class User {
    private String userId;
    private String fullName;
    private String role;

    public User(String userId, String fullName, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.role = role;
    }

    public String getUserId() {return userId;}
    public String getFullName() {return fullName;}
    public String getRole() {return role;}
}
