package com.example.myfair.activities.analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.myfair.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserAnalytics extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseUser user;
    CollectionReference usersCards;
    String TAG = "usersCards";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_analytics);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        usersCards =  db.collection("users").document(user.getUid()).collection("cards");

        usersCards.orderBy("name").get()
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

        listView = findViewById(R.id.usersCardListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Open up a more detailed page of information regarding the created card
            }
        });
    }
}
