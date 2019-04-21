package com.example.myfair.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Base Class for database objects, provides basic functionality
 */
public class DatabaseObject {
    private String id;
    private HashMap<String, Object> map;

    /**
     * Default constructor, initializes the underlying map variable
     */
    public DatabaseObject() {
        map = new HashMap<>();
    }

    /**
     * Standard constructor that sets the db object from a predefined map
     * @param newMap - map variable that represents the underlying data
     */
    public DatabaseObject(Map<String, Object> newMap) {
        map = (HashMap<String, Object>) newMap;
    }

    /**
     * Getter for map variable
     * @return - returns type HashMap<String, Object>
     */
    public HashMap<String, Object> getMap() {
        return map;
    }

    /**
     * Setter for map variable
     * @param newMap - HashMap<String, Object> type representing the underlying data for the object
     */
    public void setMap(Map<String, Object> newMap){
        if(!newMap.isEmpty()) {
            map.putAll(newMap);
        }
    }

    /**
     * Setter for a specific field in the map variable
     * NOTE: use static class strings to specify fields and values as needed (Card.FIELD_NAME)
     * @param key - String representing the key for a specific value
     * @param value - String representing the new value for the given key
     * @return boolean variable that represents the success of setValue
     */
    public boolean setValue(String key, Object value) {
        /*if(!containsKey(key))
            return false;*/
        map.put(key, value);
        return true;
    }

    /**
     * Getter for a specific key, value pair in the map
     * @param key - String representing the key for a specific value
     * @return String - value for the given key
     */
    public String getValue(String key){
        if(!containsKey(key))
            return "";
        return (String) map.get(key);
    }

    /**
     * Method that verifies the existence of a given field in the object's map
     * @param key - String that specifies a key
     * @return boolean variable that specifies whether the key was found
     */
    public boolean containsKey(String key){
        return map.containsKey(key);
    }

    /**
     * Getter for the ID variable
     * @return String that represents the object's id
     */
    public String getId(){
        return id;
    }

    /**
     * Setter for the ID variable
     * @param newId - String that represents the ID
     */
    public void setId(String newId){
        id = newId;
    }

    /**
     * Helper method to display map contents in logging
     * @return String that represents the map contents
     */
    public String display(){
        return getMap().toString();
    }
}