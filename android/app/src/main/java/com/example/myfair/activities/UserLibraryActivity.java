package com.example.myfair.activities;

import android.os.Bundle;
import android.util.Log;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.DatabaseMap;
import com.example.myfair.db.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserLibraryActivity extends AppCompatActivity {
    static final String TAG = "UserCardLib";
    FirebaseDatabase db;
    HashMap<String, Object> map;
    ArrayList<Card> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);

        db = new FirebaseDatabase();
        map = new HashMap<>();
        cardList = new ArrayList<>();

        getIdList();
    }

    private void getIdList(){
        CollectionReference ref = db.personalCollection();

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map.put(document.getId(), document.getData());
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    Log.d(TAG, "Map contents: " + map);

                    for (HashMap.Entry<String, Object> entry : map.entrySet()) {
                        String cardId = entry.getKey();
                        Object cardMap = entry.getValue();
                        Card c = new Card();

                        c.setId(cardId);
                        if (cardMap instanceof HashMap)
                            c.setMap((HashMap<String, Object>) cardMap);
                        cardList.add(c);
                    }
                    Log.d(TAG, "List contents: " + cardList);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

}
