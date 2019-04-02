package com.example.myfair.activities.analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.BusinessCardView;
import com.example.myfair.views.GenericCardView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserAnalytics extends AppCompatActivity {

    FirebaseDatabase db;
    CollectionReference usersCards;
    String TAG = "usersCards";
    private LinearLayout listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_analytics);

        db = new FirebaseDatabase();
        usersCards =  db.userCards();

        listView = findViewById(R.id.usersCardListView);

        getIdList(usersCards, listView);
    }

    private View.OnClickListener specificCardAnalyticsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(UserAnalytics.this, UserCardAnalyticsActivity.class);
            GenericCardView placeHolder = (GenericCardView) v;
            String cId = placeHolder.getCardID();
            intent.putExtra("cId", cId);
            startActivity(intent);
        }
    };

    private void addCardView(GenericCardView v, LinearLayout listView) {
        listView.addView(v);
        v.setMargins();
    }

    private void getIdList(CollectionReference ref, final LinearLayout listView){
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cID = document.getId();
                        HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                        UniversityCardView v = new UniversityCardView(UserAnalytics.this, cID, map);
                        v.setOnClickListener(specificCardAnalyticsListener);
                        addCardView(v, listView);

                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
