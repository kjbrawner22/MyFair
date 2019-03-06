package com.example.myfair.activities;

import android.os.Bundle;
import android.util.Log;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.CardList;
import com.example.myfair.db.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class UserLibraryActivity extends AppCompatActivity {
    static final String TAG = "UserCardLib";
    FirebaseDatabase db;
    CardList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);

        db = new FirebaseDatabase();
        list = new CardList();

        getIdList();
    }

    private void getIdList(){
        CollectionReference ref = db.ownCards();

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Card c = new Card();
                        c.setId(document.getId());
                        c.setMap(document.getData());
                        list.add(c);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    list.displayIDs();
                    list.displayWithContents();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

}
