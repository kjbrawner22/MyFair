package com.example.myfair.db;

import java.util.HashMap;
import java.util.Map;

public class DatabaseMap {
        private String id;
        private HashMap<String, Object> map;

        public DatabaseMap() {
            map = new HashMap<>();
        }

        public DatabaseMap(Map<String, Object> newMap) {
            map = (HashMap<String, Object>) newMap;
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

        public String getId(){
            return id;
        }

        public void setId(String newId){
            id = newId;
        }
}

