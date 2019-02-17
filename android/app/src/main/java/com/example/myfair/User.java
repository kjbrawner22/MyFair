package com.example.myfair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_PROFILE_CREATED = "profile_created";
    public static final String FIELD_USERNAME ="username";

    public static final String VALUE_TRUE = "true";

    private HashMap<String, Object> map;

    public User() {
        map = new HashMap<>();
    }

    User(Map<String, Object> newMap) {
        map = (HashMap<String, Object>) newMap;
    }

    public User(String firstName, String lastName) {
        map = new HashMap<>();
        map.put(FIELD_FIRST_NAME, firstName);
        map.put(FIELD_LAST_NAME, lastName);
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> newMap){
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

    boolean profileCreated() {
        if (map.containsKey(FIELD_PROFILE_CREATED)) {
            return Objects.equals(map.get(FIELD_PROFILE_CREATED), VALUE_TRUE);
        }
        return false;
    }
}
