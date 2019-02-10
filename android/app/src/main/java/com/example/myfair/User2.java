package com.example.myfair;

import java.util.HashMap;
import java.util.Map;

public class User2 {
    //  public static final String TYPE_STUDENT = "student";
    //  public static final String TYPE_WORKER = "worker";

    private String type;
    private String fname;
    private String lname;

    public User2() {

    }

    User2(String type, String fname, String lname) {
        this.type = type;
        this.fname = fname;
        this.lname = lname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return fname;
    }

    public void setFirstName(String firstName) {
        this.fname = firstName;
    }

    public String getLastName() {
        return lname;
    }

    public void setLastName(String lastName) {
        this.lname = lastName;
    }
}
