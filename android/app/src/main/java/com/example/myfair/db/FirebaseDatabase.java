package com.example.myfair.db;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseDatabase {
    private FirebaseFirestore db;

    public FirebaseDatabase() {
        db = FirebaseFirestore.getInstance();
    }


}
