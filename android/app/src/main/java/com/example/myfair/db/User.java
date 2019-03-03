package com.example.myfair.db;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;

public class User extends DatabaseObject {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_UNIVERSITY_MAJOR = "university_major";
    public static final String FIELD_PROFILE_CREATED = "profile_created";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_COMPANY_POSITION = "company_position";
    public static final String FIELD_USERNAME ="username";

    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    public User() {
        super();
    }

    public User(String name){
        super.setValue(User.FIELD_NAME, name);
    }

    public User(Map<String, Object> newMap) {
        super(newMap);
    }

    public boolean profileCreated() {
        if (containsKey(FIELD_PROFILE_CREATED)) {
            return getValue(FIELD_PROFILE_CREATED).equals(VALUE_TRUE);
        }
        Log.d("getCardInfo", "Doesn't contain key");
        return false;
    }

    public DocumentReference setFromDb(){
        final String TAG = "getUserInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return null;
        }

        DocumentReference docRef = db.collection("users").document(user.getUid());
        return docRef;
    }

    public void sendToDb() {
        final String TAG = "sendUserInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.set(getMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                } else {
                    Log.d(TAG, "Error updating document");
                }
            }
        });
    }

    public static boolean isPrivateField(String key) {
        if (key.equals(FIELD_PROFILE_CREATED)) {
            return true;
        }

        return false;
    }

}
