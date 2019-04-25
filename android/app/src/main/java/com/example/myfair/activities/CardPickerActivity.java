package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CardPickerActivity extends AppCompatActivity {

    private static final String INTENT_TOOLBAR_TITLE = "Choose Card";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_picker);

        FirebaseDatabase db = new FirebaseDatabase();
        LinearLayout cardList = findViewById(R.id.lytListView);
        getIdList(db.userCards(), cardList);
        setupToolbar();
    }

    /**
     * Gets the list of IDs of cards and populates a linear layout with cardViews.
     * */
    private void getIdList(CollectionReference ref, final LinearLayout listView){
        final String TAG = "CardViewingActivityLog";
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cID = document.getId();
                        HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                        UniversityCardView v = new UniversityCardView(CardPickerActivity.this, cID, map, listView);
                        v.setOnClickListener(cardListener);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Generic onclickListener for UniversityCardView
     * Puts card data into a bundle to send back to parent activity
     */
    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent returnIntent = new Intent();

            Bundle extras = new Bundle();
            extras.putSerializable("card_map", ((UniversityCardView) view).getMap());
            extras.putString("card_id", ((UniversityCardView) view).getCardID());

            returnIntent.putExtras(extras);

            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Helper function to setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setTitle(INTENT_TOOLBAR_TITLE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_VIEWING", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
