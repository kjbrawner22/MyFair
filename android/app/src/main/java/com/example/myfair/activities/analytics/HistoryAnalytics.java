package com.example.myfair.activities.analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HistoryAnalytics extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    CollectionReference scannedCards;
    String TAG = "history analytic";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_analytics);

        firebaseDatabase = new FirebaseDatabase();

        scannedCards = firebaseDatabase.userContacts();
        listView = findViewById(R.id.scannedCardList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //New Fragment showing the card they clicked on
            }
        });

        scannedCards.orderBy("meta.timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if( task.isSuccessful()){
                            //Load into list view
                        }
                        else{
                            Log.d(TAG,"Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
