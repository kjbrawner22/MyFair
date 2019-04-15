package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
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
    private CardInfoView cardInfo;
    private ScrollView svCardScroller;
    ImageButton cardInfoBack, cardInfoShare;
    FirebaseDatabase db;

    public static final String INTENT_TOOLBAR_TITLE = "toolbar_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_viewing);

        setupToolbar();

        db = new FirebaseDatabase();
        context = this;
        svCardScroller = findViewById(R.id.svCardScroller);
        LinearLayout cardList = findViewById(R.id.lytListView);
        cardInfo = findViewById(R.id.cardInfo);
        cardInfoBack = cardInfo.findViewById(R.id.btnInfoBack);
        cardInfoShare = cardInfo.findViewById(R.id.btnShare);
        cardInfoBack.setOnClickListener(buttonListener);
        cardInfoShare.setOnClickListener(buttonListener);

        changeForm(1);

        String title = getIntent().getStringExtra(INTENT_TOOLBAR_TITLE);
        if (title.equals(CollectionsFragment.CARD_VIEWING_TOOLBAR_TITLE)) {
            getIdList(db.userContacts(), cardList);
        } else {
            getIdList(db.userCards(), cardList);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

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

    private void addCardView(GenericCardView v, LinearLayout listView) {
        listView.addView(v);
        v.setMargins();
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
                        UniversityCardView v = new UniversityCardView(context, cID, map);
                        v.setOnClickListener(universityCardClickListener);
                        addCardView(v, listView);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private View.OnClickListener universityCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeForm(2);
            cardInfo.setFromUniversityCardView((UniversityCardView) view, context);
            Log.d("CardInfoCreated", "card Info Visible");
        }
    };

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Log.d("ButtonIDClicked", "ID: " + id);
            switch(id){
                case R.id.btnInfoBack:
                    changeForm(1);
                    break;
                case R.id.btnShare:
                    Bundle bundle = new Bundle();
                    String str = cardInfo.getQrStr();
                    bundle.putString("encryptedString", str);
                    //Log.d("EncryptedString", str);
                    BottomSheet bottomSheet = new BottomSheet();
                    bottomSheet.setArguments(bundle);
                    bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                    break;
                case R.id.homeAsUp:
                    onBackPressed();
                    break;
                default:
                    Log.d("ErrorLog", view.getId() + "- button not yet implemented");
                    break;
            }
        }
    };

    private void changeForm(int form){
        switch (form){
            case 1:
                // Cards
                svCardScroller.setVisibility(View.VISIBLE);
                cardInfo.setVisibility(View.GONE);
                break;
            case 2:
                // card info
                svCardScroller.setVisibility(View.GONE);
                cardInfo.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("CardViewingActivityLog", "form not implemented..");
        }
    }
}
