package com.example.myfair.activities.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

public class UserCardAnalyticsActivity extends AppCompatActivity {

    FirebaseDatabase db;
    String cardId;
    DocumentReference card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_analytics);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cardId = null;
            } else {
                cardId = extras.getString("cId");
            }
        } else {
            cardId = (String) savedInstanceState.getSerializable("cId");
        }

        db = new FirebaseDatabase();
        card  = db.userCards().document(cardId);



    }
}
