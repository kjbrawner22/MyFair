package com.example.myfair;

import java.util.HashMap;
import java.util.Map;

public class Card {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_UNIVERSITY_MAJOR = "university_major";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_COMPANY_POSITION = "company_position";
    public static final String FIELD_CARD_OWNER = "card_owner";

    private HashMap<String, Object> map;

    public Card() {
        map = new HashMap<>();
    }

    public Card(String name) {
        map = new HashMap<>();
        map.put(FIELD_NAME, name);
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
