package com.example.myfair.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myfair.modelsandhelpers.Connection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/**
 * Class model for user information that is stored in the database
 */
public class User extends DatabaseObject {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_UNIVERSITY_MAJOR = "university_major";
    public static final String FIELD_PROFILE_CREATED = "profile_created";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_COMPANY_POSITION = "company_position";
    public static final String FIELD_USERNAME ="username";

    // social media username fields
    public static final String FIELD_CELL_NUMBER = "cell_number";
    public static final String FIELD_WORK_NUMBER = "work_number";
    public static final String FIELD_HOME_NUMBER = "home_number";
    public static final String FIELD_TWITTER_USERNAME ="twitter_username";
    public static final String FIELD_GITHUB_USERNAME ="github_username";
    public static final String FIELD_LINKED_IN_USERNAME ="linked_in_username";
    public static final String FIELD_INSTAGRAM_USERNAME ="instagram_username";


    public static final String VALUE_TRUE = "true";
    public static final String VALUE_FALSE = "false";

    /**
     * Default Constructor
     */
    public User() {
        super();
    }

    /**
     * Standard constructor that sets the user's name
     * @param name - String representing the name of the user
     */
    public User(String name){
        super.setValue(User.FIELD_NAME, name);
    }

    /**
     * Constructor that initializes the user using a predefined map
     * NOTE: data from DB comes in the form of a map, User user = new User(data);
     * @param newMap - Map variable that represents the underlying data in the user class
     */
    public User(Map<String, Object> newMap) {
        super(newMap);
    }

    /**
     * Method that checks if profile information has been initialized in the database
     * @return - boolean variable that specifies profile completion
     */
    public boolean profileCreated() {
        if (containsKey(FIELD_PROFILE_CREATED)) {
            boolean val = getValue(FIELD_PROFILE_CREATED).equals(VALUE_TRUE);
            return val;
        }
        //Log.d("getCardInfo", "Doesn't contain key");
        return false;
    }

    /**
     * Custom method that generates a DocRef for a specific user
     * @return returns document reference for the specified user
     */
    public DocumentReference setFromDb(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return null;
        }

        return db.collection("users").document(user.getUid());
    }

    /**
     * Custom method that updates user information in the database
     */
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

    public static String[] getAllConnectionFields() {
        return new String[]{
                User.FIELD_CELL_NUMBER,
                User.FIELD_WORK_NUMBER,
                User.FIELD_HOME_NUMBER,
                User.FIELD_TWITTER_USERNAME,
                User.FIELD_GITHUB_USERNAME,
                User.FIELD_INSTAGRAM_USERNAME,
                User.FIELD_LINKED_IN_USERNAME
        };
    }

    public ArrayList<Connection> getMyConnections() {
        ArrayList<Connection> connections = new ArrayList<>();

        for (Connection connection : Connection.getConnectionList()) {
            if (containsKey(connection.getDbKey())) {
                connection.setValue(getValue(connection.getDbKey()));
                connection.setEnabled(false);
                connections.add(connection);
            }
        }

        return connections;
    }

    /**
     * Static method that determines whether a field is private
     * @param key - String that representsa specified field
     * @return - boolean variable that specifies if the given field is private
     * NOTE: true = private, false = public
     */
    public static boolean isPrivateField(String key) {
        if (key.equals(FIELD_PROFILE_CREATED)) {
            return true;
        }

        return false;
    }
}