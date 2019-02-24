package com.example.myfair.db;

import android.util.Log;
import android.widget.Toast;

import com.example.myfair.CardCreationActivity;
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

import javax.annotation.Nullable;

import androidx.annotation.NonNull;

public class Card extends DbObject{
    public static final String FIELD_NAME = "name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_UNIVERSITY_MAJOR = "university_major";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_COMPANY_POSITION = "company_position";
    public static final String FIELD_CARD_OWNER = "card_owner";
    public static final String FIELD_TYPE = "card_type";

    public static final String VALUE_TYPE_UNIVERSITY = "university_card";
    public static final String VALUE_TYPE_BUSINESS = "business_card";
    public static final String VALUE_NEW_CARD = "new_card";

    DbObject card;

    public Card() {
        card = new DbObject();
    }

    public Card(String name) {
        card = new DbObject();
        card.getMap().put(FIELD_NAME, name);;
    }
}
