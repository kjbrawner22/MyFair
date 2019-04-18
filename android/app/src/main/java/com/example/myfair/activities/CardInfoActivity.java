package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.UniversityCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;

public class CardInfoActivity extends AppCompatActivity {
    private LinearLayout lytCardInfo;
    private UniversityCardView cardView;
    private String encryptedString;

    public static final String INTENT_TOOLBAR_TITLE = "Card Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        TextView cardBio = findViewById(R.id.tvCardBio);
        FloatingActionButton fabShare;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(fabListener);
        lytCardInfo = findViewById(R.id.lytCardInfo);
        HashMap<String, Object> map;
        String cID;
        setupToolbar(INTENT_TOOLBAR_TITLE);

        if(bundle != null) {
            map = (HashMap<String, Object>) bundle.getSerializable("card_map");
            cID = bundle.getString("card_id");
            Log.d("CardInfoActivityLog", "uID: "+ (String) map.get(Card.FIELD_CARD_OWNER) + "cID: " + cID);
            cardView = new UniversityCardView(this, cID, map);
            lytCardInfo.addView(cardView, 0);
            cardView.setMargins();
            String name = (String) map.get(Card.FIELD_NAME);
            setupToolbar(name);
            if(map.containsKey(Card.FIELD_ABOUT)){
                cardBio.setText((String) map.get(Card.FIELD_ABOUT));
            }
            setQrString((String) map.get(Card.FIELD_CARD_OWNER), cID);
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

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString("encryptedString", encryptedString);
            BottomSheet bottomSheet = new BottomSheet();
            bottomSheet.setArguments(bundle);
            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
        }
    };

    public void setQrString(String uID, String cID){
        qrObject user = new qrObject(uID, cID);
        String serializeString = new Gson().toJson(user);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        Log.d("CardInfoActivityLog", "CardInfoView: " + encryptedString);
    }
}
