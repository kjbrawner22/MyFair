package com.example.myfair.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myfair.ModelsandHelpers.EncryptionHelper;
import com.example.myfair.ModelsandHelpers.qrObject;
import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


public class ScannedActivity extends AppCompatActivity {

    String SCANNED_STRING = "scanned_string";
    Intent getScannedActivity(Context callingClassContext, String encryptedString){
        return new Intent(callingClassContext,ScannedActivity.class).putExtra(SCANNED_STRING,encryptedString);
    }
    TextView scannedNameTextView;
    TextView scannedUniCompTextView;
    TextView scannedMajPosTextView;

    TextView scannedUniComp;
    TextView scannedMajPos;

    /**
     * Decode the QR Code and display the contents of the map received from Firebase
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned);

        //SCANNED_STRING = "scanned_string";

        scannedNameTextView = findViewById(R.id.scannedNameTextView);
        scannedUniCompTextView = findViewById(R.id.scannedUniCompTextView);
        scannedMajPosTextView = findViewById(R.id.scannedMajPosTextView);

        scannedUniComp = findViewById(R.id.scannedUniComp);
        scannedMajPos = findViewById(R.id.scannedMajPos);

        if(getIntent().getSerializableExtra(SCANNED_STRING) == null) throw new RuntimeException("No encrypted string found in intent");

        String decryptedString = EncryptionHelper.getInstance().getDecryptionString(getIntent().getStringExtra(SCANNED_STRING));
        qrObject qrObject = new Gson().fromJson(decryptedString, qrObject.class);
        String cID = qrObject.getCardID();
        String uID = qrObject.getUserID();

        //Run the request to get the info from firebase
        final Card sharedCard = new Card();
        Log.d("Scanned", "Checking Ids "+uID+" "+cID);

        DocumentReference ref = sharedCard.setFromDb(uID,cID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Scanned", "Listen failed.");
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Scanned", "User snapshot updated");
                    sharedCard.setMap(snapshot.getData());
                    Log.d("Scanned","Object info" + sharedCard.getMap());
                    scannedNameTextView.setText(sharedCard.getValue(Card.FIELD_NAME));

                    if(sharedCard.containsKey(Card.FIELD_UNIVERSITY_NAME)){
                        scannedUniComp.setText(getResources().getString(R.string.c_university));
                        scannedMajPos.setText(getResources().getString(R.string.c_major));

                        scannedUniCompTextView.setText(sharedCard.getValue(Card.FIELD_UNIVERSITY_NAME));
                        scannedMajPosTextView.setText(sharedCard.getValue(Card.FIELD_UNIVERSITY_MAJOR));
                    }
                    else{
                        scannedUniComp.setText(getResources().getString(R.string.c_company));
                        scannedMajPos.setText(getResources().getString(R.string.c_position));

                        scannedUniCompTextView.setText(sharedCard.getValue(Card.FIELD_COMPANY_NAME));
                        scannedMajPosTextView.setText(sharedCard.getValue(Card.FIELD_COMPANY_POSITION));
                    }
                }
            }
        });
    }
}
