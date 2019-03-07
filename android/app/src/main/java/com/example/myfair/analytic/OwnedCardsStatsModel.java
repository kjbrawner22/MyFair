package com.example.myfair.analytic;

import com.google.firebase.firestore.DocumentReference;

public class OwnedCardsStatsModel extends StatisticsModel {

    public OwnedCardsStatsModel(String Collection, DocumentReference docRef){
        super(Collection,docRef);
    }
}
