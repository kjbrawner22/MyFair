package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.views.UniversityCardView;

import java.util.HashMap;

public class CardInfoActivity extends AppCompatActivity {
    private LinearLayout lytCardInfo;
    private UniversityCardView cardView;

    public static final String INTENT_TOOLBAR_TITLE = "Card Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lytCardInfo = findViewById(R.id.lytCardInfo);
        HashMap<String, Object> map;
        String cID;
        setupToolbar(INTENT_TOOLBAR_TITLE);

        if(bundle != null) {
            map = (HashMap<String, Object>) bundle.getSerializable("card_map");
            cID = bundle.getString("card_id");
            cardView = new UniversityCardView(this, cID, map);
            lytCardInfo.addView(cardView);
            cardView.setMargins();
            String name = (String) map.get(Card.FIELD_NAME);
            setupToolbar(name);
        }
        else{
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_INFO", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
