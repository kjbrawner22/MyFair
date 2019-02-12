package com.example.myfair;

import java.util.HashMap;
import java.util.Map;

public class User {
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_PROFILE_CREATED = "profile_created";
    public static final String FIELD_USERNAME ="username";

    private HashMap<String, Object> map;

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

    public void setMap(Map newMap){
        if(!newMap.isEmpty()) {
            map.putAll(newMap);
        }
    }

    public boolean setValue(String key, Object value) {
        map.put(key, value);
        return true;
    }

    public String getValue(String key){
        return (String) map.get(key);
    }

    public boolean containsKey(String key){
        return map.containsKey(key);
    }

}
