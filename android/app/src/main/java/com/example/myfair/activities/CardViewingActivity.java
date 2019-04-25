package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.fragments.CollectionsFragment;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.CardInfoView;
import com.example.myfair.views.GenericCardView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CardViewingActivity extends AppCompatActivity {
    //implement card directory here
    private Context context;
    FirebaseDatabase db;

    public static final String INTENT_TOOLBAR_TITLE = "toolbar_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_viewing);

        setupToolbar();

        db = new FirebaseDatabase();
        context = this;
        LinearLayout cardList = findViewById(R.id.lytListView);

        String title = getIntent().getStringExtra(INTENT_TOOLBAR_TITLE);
        if (title.equals(CollectionsFragment.CARD_VIEWING_TOOLBAR_TITLE)) {
            getIdList(db.userContacts(), cardList);
        } else {
            getIdList(db.userCards(), cardList);
        }
    }

    /**
     * Implements back button on toolbar
     * @return - boolean variable to specify success
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Helper function to setup toolbar
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(INTENT_TOOLBAR_TITLE));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_VIEWING", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
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
                        UniversityCardView v = new UniversityCardView(context, cID, map, listView);
                        v.setOnClickListener(universityCardClickListener);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Listener for the UniversityCardView
     * Opens CardInfoActivity
     */
    private View.OnClickListener universityCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle extras = new Bundle();
            extras.putSerializable("card_map", ((UniversityCardView) view).getMap());
            extras.putString("card_id", ((UniversityCardView) view).getCardID());

            Intent intent = new Intent(CardViewingActivity.this, CardInfoActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    };
}
