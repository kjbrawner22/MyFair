package com.example.myfair.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabase {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public FirebaseDatabase() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private FirebaseFirestore getDb(){
        return db;
    }

    /*
            Get database reference for card library
            must pass in desired uID
     */
    public CollectionReference cards(String uID){
        return db.collection("users").document(uID).collection("cards");
    }

    public CollectionReference userCards(){
        return db.collection("users").document(currentUser.getUid()).collection("cards");
    }

    /*
            Get database reference for contacts collection
            no parameters

            Use "colRef.add();" to add new card
     */
    public CollectionReference userContacts(){
        return db.collection("users").document(currentUser.getUid()).collection("collection");
    }




    /*
            Get Database reference for a specific card,
            must pass in user ID and card ID

            Use this DocumentReference to pull card from db
            pass in null for uID to get cards belonging to
            the current user
     */
    public DocumentReference getCardRef(String uID, String cID){
        if(uID == null){
            return userCards().document(cID);
        }
        return cards(uID).document(cID);
    }

}
