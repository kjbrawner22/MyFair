package com.example.myfair.analytic;


import android.util.Log;

import com.example.myfair.db.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class StatisticsModel {


    ArrayList<Card> noteList;

    public StatisticsModel(String Collection, DocumentReference docRef){
        noteList = new ArrayList<>();
        docRef.collection(Collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.e("pulling data","Successful pull");
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Card n = new Card(document.getData());
                        Log.e("pulling data","data pull sucessful");
                        noteList.add(n);

                    }


                }
                else{
                    Log.e("pulling data", "Unsuccessful pull from the database");
                }


            }
        });
    }

    int getTotalCards(){
        return noteList.size();
    }
}
