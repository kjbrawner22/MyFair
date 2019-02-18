package com.example.myfair.db;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

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
        if (map.containsKey(FIELD_PROFILE_CREATED)) {
            return map.get(FIELD_PROFILE_CREATED).equals(VALUE_TRUE);
        }
        Log.d("getCardInfo", "Doesnt contain key");
        return false;
    }

    public void setFromDb(){
        final String TAG = "getUserInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.");
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "User snapshot updated");
                    setMap(snapshot.getData());
                }
            }
        });
    }
}
