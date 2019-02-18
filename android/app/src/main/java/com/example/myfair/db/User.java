package com.example.myfair.db;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;

public class User {
    public static final String FIELD_FIRST_NAME = "first_name";
    public static final String FIELD_LAST_NAME = "last_name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_UNIVERSITY_MAJOR = "university_major";
    public static final String FIELD_PROFILE_CREATED = "profile_created";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_COMPANY_POSITION = "company_position";
    public static final String FIELD_USERNAME ="username";

    public static final String VALUE_TRUE = "true";

    private HashMap<String, Object> map;

    public User() {
        map = new HashMap<>();
    }

    public User(Map<String, Object> newMap) {
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

    public boolean profileCreated() {
        boolean a;
        if (map.containsKey(FIELD_PROFILE_CREATED)) {
            String boolString = (String) map.get(FIELD_PROFILE_CREATED);
            Log.d("getCardInfo", "Stored String: " + boolString);
            a = boolString.equals("true");
            Log.d("getCardInfo", "Stored Boolean: " + a);
            return a;
        }
        Log.d("getCardInfo", "Doenat contain key");
        return false;
    }

    public void setFromDb(){
        final String TAG = "getCardInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.reload();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // document snapshot succeeded
                        setMap(document.getData());
                        Log.d(TAG, "Document data " + document.getData());
                    } else {  // document doesn't exist yet
                        Log.d(TAG, "No such document");
                    }
                } else {  // document snapshot failed
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
