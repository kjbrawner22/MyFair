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

    public FirebaseFirestore getDb(){
        return db;
    }

    public CollectionReference userCards(){
        return db.collection("users").document(currentUser.getUid()).collection("cards");
    }

    /*
            Get database reference for card library
            must pass in desired uID
     */
    public CollectionReference cards(String uID){
        return db.collection("users").document(uID).collection("cards");
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
            Get database reference for card collection,
            must pass in user ID

            Use "colRef.add(data);" to set new card
            get doc ID and add metadata/styles folder
     */
    public CollectionReference newCard(){
        return cards(currentUser.getUid());
    }


    /*
            Get Database reference for a specific card,
            must pass in user ID and card ID

            Use this DocumentReference to pull card from db
     */
    public DocumentReference getCardRef(String uID, String cID){
        return cards(uID).document(cID);
    }


}
