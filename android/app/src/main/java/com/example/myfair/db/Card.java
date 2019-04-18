package com.example.myfair.db;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Class model for Card object map that is stored in the Firebase DB
 */
public class Card extends DatabaseObject {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_UNIVERSITY_NAME = "university_name";
    public static final String FIELD_UNIVERSITY_MAJOR = "university_major";
    public static final String FIELD_COMPANY_NAME = "company_name";
    public static final String FIELD_COMPANY_POSITION = "company_position";
    public static final String FIELD_CARD_OWNER = "card_owner";
    public static final String FIELD_TYPE = "card_type";
    public static final String FIELD_BANNER_URI = "banner_uri";
    public static final String FIELD_PROFILE_URI = "profile_uri";
    public static final String FIELD_SCAN_REGISTRY = "scanRegistry";

    public static final String VALUE_TYPE_UNIVERSITY = "university_card";
    public static final String VALUE_TYPE_BUSINESS = "business_card";
    public static final String VALUE_NEW_CARD = "new_card";
    public static final String VALUE_DEFAULT_IMAGE = "default_image";

    private FirebaseDatabase db;
    private String cID;
    private CardCreationListener listener;


    /**
     * Default Constructor
     */
    public Card() {
        super();
        db = new FirebaseDatabase();
    }

    /**
     * Standard constructor that also initializes database and fullName
     * @param fullName - String that represents the full name of the card owner
     */
    public Card(String fullName){
        super();
        db = new FirebaseDatabase();
        this.setValue(Card.FIELD_NAME, fullName);
    }

    /**
     * Constructor that initializes card using an existing map
     * NOTE: data pulled from DB is of type map, can use Card card = new Card(data);
     * @param newMap - Map variable that represents the underlying data in the card class
     */
    public Card(Map<String, Object> newMap) {
        super(newMap);
    }

    public void addCardCreationListener(CardCreationListener listener) {
        this.listener = listener;
    }

    /**
     * Custom method that generates a DocRef for a specific uID & cID
     * @param uID - String that represents the user ID for the card owner
     * @param cID - String that represents the card ID
     * @return returns document reference for the specified card
     */
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

    /**
     * Custom method that sends an updated card to the database
     * @param cID - String that represents the card ID
     */
    public void sendToDb(String cID){
        final String TAG = "sendCardInfo";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }

        if (cID.equals(com.example.myfair.db.Card.VALUE_NEW_CARD)) {
             final CollectionReference colRef = db.userCards();
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
            DocumentReference docRef = db.getCardRef(null, cID);

            docRef.set(getMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "DocumentSnapshot card info successfully updated!");

                        if (listener != null) {
                            listener.cardCreated();
                        }
                    } else {
                        Log.d(TAG, "Error updating card info document");
                    }
                }
            });
        }
    }

    public ArrayList<Pair<String, String>> getConnectionEntries() {
        ArrayList<Pair<String, String>> list = new ArrayList<>();

        for (String connectionField : User.getAllConnectionFields()) {
            if (containsKey(connectionField)) {
                list.add(new Pair<>(connectionField, getValue(connectionField)));
            }
        }

        return list;
    }

    /**
     * Helper method that allows sedToDb to initialize the card metadata
     * @param docRef - DocumentReference for the location of the card metadata
     */
    private void createMetadata(DocumentReference docRef){
        final String TAG = "sendCardInfo";
        DocumentReference metaDoc = docRef.collection("cdata").document("metadata");

        HashMap<String, Object> data = new HashMap<>();
        Date currentTime = Calendar.getInstance().getTime();
        data.put("created", currentTime);
        data.put("shared", 0);
        data.put(FIELD_SCAN_REGISTRY, new ArrayList<Timestamp>());

        Log.d("CardCreationLog", "Current Time: " + currentTime);
        metaDoc.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null) {
                        listener.cardCreated();
                    }
                    Log.d(TAG, "DocumentSnapshot metadata successfully updated!");
                } else {
                    Log.d(TAG, "Error updating metadata document");
                }
            }
        });
    }

    /**
     * Getter for the cardID (cID)
     * @return returns Strign for the cID
     */
    public String getCardID(){
        return cID;
    }


    /**
     * Setter for cID
     * @param ID - String that represents the card ID
     */
    public void setCardID(String ID){
        cID = ID;
    }
}
