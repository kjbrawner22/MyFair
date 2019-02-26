package com.example.myfair.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabase {
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    public FirebaseDatabase() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public CollectionReference personalCollection(){
        return db.collection("users").document(currentUser.getUid()).collection("cards");
    }
}
