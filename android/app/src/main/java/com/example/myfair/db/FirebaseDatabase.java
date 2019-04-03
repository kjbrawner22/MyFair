package com.example.myfair.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * FirebaseDatabase object that handles database interactions and references
 */
public class FirebaseDatabase {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    /**
     * Standard constructor that initializes db and currentUser
     */
    public FirebaseDatabase() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Getter method for an instance of the DB
     * @return FirebaseFirestore instance
     */
    private FirebaseFirestore getDb(){
        return db;
    }

    /**
     * Getter method for the current user's user ID
     * @return String - user ID
     */
    public String getUserId(){ return currentUser.getUid(); }

    /**
     * Get database reference for card library for specified uID
     * @param uID - String that represents the desired uID
     * @return CollectionReference for the card Library for specified uID
     */
    public CollectionReference cards(String uID){
        return db.collection("users").document(uID).collection("cards");
    }

    /**
     * Get database reference for card library for current user
     * @return CollectionReference for the card Library for current user
     */
    public CollectionReference userCards(){
        return db.collection("users").document(currentUser.getUid()).collection("cards");
    }

    /**
     * Get database reference for contacts collection
     * NOTE: Use "colRef.add();" to add new card
     * @return CollectionReference for the contact library of the current user
     */
    public CollectionReference userContacts(){
        return db.collection("users").document(currentUser.getUid()).collection("collection");
    }

    /**
     * Get Database reference for a specific card,
     * @param uID - String that represents the specified user ID
     * @param cID - String that represents the specified card ID
     * @return DocumentReference for the appropriate card
     */
    public DocumentReference getCardRef(String uID, String cID){
        if(uID == null){
            return userCards().document(cID);
        }
        return cards(uID).document(cID);
    }

}
