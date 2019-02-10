package com.example.myfair;

import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String TYPE_STUDENT = "student";
    public static final String TYPE_WORKER = "worker";

    private String type;

    private String firstName;
    private String lastName;

    public User() {

    }

    public User(String type) {
        this.type = type;
    }

    public User(String type, String firstName, String lastName) {
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
