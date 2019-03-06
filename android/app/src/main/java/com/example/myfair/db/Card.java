package com.example.myfair.db;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Card extends DatabaseObject {
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

    private String cID;

    public Card() {
        super();
    }

    public Card(String fullName){
        super();
        this.setValue(Card.FIELD_NAME, fullName);
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
        setCardID(cID);
        return docRef;
    }

    public void sendToDb(String cID){
        final String TAG = "sendCardInfo";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        if (cID.equals(com.example.myfair.db.Card.VALUE_NEW_CARD)) {
             final CollectionReference colRef = db.collection("users").document(user.getUid()).collection("cards");
             colRef.add(getMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                 @Override
                 public void onSuccess(DocumentReference documentReference) {
                     Log.d(TAG, "DocumentSnapshot card info with ID: " + documentReference.getId());
                     createMetadata(documentReference);
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Log.w(TAG, "Error adding document", e);
                 }
             });
        }
        else {
            DocumentReference docRef = db.collection("users").document(user.getUid()).collection("cards").document(cID);

            docRef.set(getMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot card info successfully updated!");
                    } else {
                        Log.d(TAG, "Error updating card info document");
                    }
                }
            });
        }
    }

    private void createMetadata(DocumentReference docRef){
        final String TAG = "sendCardInfo";
        DocumentReference metaDoc = docRef.collection("cdata").document("metadata");
        HashMap<String, Object> data = new HashMap<>();
        Date currentTime = Calendar.getInstance().getTime();
        data.put("created", currentTime);
        data.put("shared", 0);

        Log.d("CardCreationLog", "Current Time: " + currentTime);
        metaDoc.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "DocumentSnapshot metadata successfully updated!");
                } else {
                    Log.d(TAG, "Error updating metadata document");
                }
            }
        });
    }

    public String getCardID(){
        return cID;
    }

    public void setCardID(String ID){
        cID = ID;
    }
}
