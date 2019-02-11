package com.example.myfair;

import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";

    private HashMap<String, Object> map;

    private String type;

    private String firstName;
    private String lastName;

    public User() {
        map = new HashMap<>();
    }

    public User(String firstName, String lastName) {
        map = new HashMap<>();
        map.put(FIELD_FIRST_NAME, firstName);
        map.put(FIELD_LAST_NAME, lastName);
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public boolean setValue(String key, Object value) {
        map.put(key, value);
        return true;
    }
}
