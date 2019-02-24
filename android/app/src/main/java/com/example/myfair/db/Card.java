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

    public Card() {
        super();
    }

    public Card(Map<String, Object> newMap) {
        super(newMap);
    }

    public DocumentReference setFromDb(String uID, String cID){
        final String TAG = "getCardInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        */
        DocumentReference docRef = db.collection("users").document(uID).collection("cards").document(cID);
        return docRef;
    }

    public void sendToDb(String cID){
        final String TAG = "sendCardInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        DocumentReference docRef;

        if (cID.equals(com.example.myfair.db.Card.VALUE_NEW_CARD))
            docRef = db.collection("users").document(user.getUid()).collection("cards").document();
        else
            docRef = db.collection("users").document(user.getUid()).collection("cards").document(cID);

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
}
