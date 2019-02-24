package com.example.myfair.db;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;

public class DbObject {
        private HashMap<String, Object> map;

        public DbObject() {
            map = new HashMap<>();
        }

        public DbObject(Map<String, Object> newMap) {
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

}

