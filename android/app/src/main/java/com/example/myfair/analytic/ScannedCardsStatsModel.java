package com.example.myfair.analytic;

import com.google.firebase.firestore.DocumentReference;

public class ScannedCardsStatsModel extends StatisticsModel {

    public ScannedCardsStatsModel(String collection, DocumentReference docRef){
        super(collection,docRef);
    }
}
