package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.db.Packet;
import com.example.myfair.modelsandhelpers.Connection;
import com.example.myfair.views.CardInfoView;
import com.example.myfair.views.ConnectionInfoView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

public class PacketInfoActivity extends AppCompatActivity {
    public static final String INTENT_TOOLBAR_TITLE = "Packet Info";
    private LinearLayout lytPacketInfo, lytDocumentList;
    HashMap<String, Object> cards, documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_info);

        FirebaseDatabase db = new FirebaseDatabase();
        setupToolbar();

        FloatingActionButton fabShare;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //fabShare = findViewById(R.id.fabShare);
        //fabShare.setOnClickListener(fabListener);
        lytPacketInfo = findViewById(R.id.lytCardInfo);
        lytDocumentList = findViewById(R.id.lytDocumentList);
        HashMap<String, Object> map;
        String pID;


        if(bundle != null) {
            map = (HashMap<String, Object>) bundle.getSerializable("packet_map");
            pID = bundle.getString("packet_id");
            String uID = (String) map.get(Packet.FIELD_PACKET_OWNER);

            Log.d("CardInfoActivityLog", "uID: "+ (String) map.get(Card.FIELD_CARD_OWNER) + "pID: " + pID);
            //cardView = new UniversityCardView(this, cID, map, lytPacketInfo, 0);
            cards = (HashMap<String, Object>) map.get(Packet.FIELD_CARD_LIST);
            documents = (HashMap<String, Object>) map.get(Packet.FIELD_DOCUMENT_LIST);

            for (HashMap.Entry<String, Object> entry : cards.entrySet()) {
                String key = entry.getKey();
                HashMap<String, Object> value = (HashMap<String, Object>) entry.getValue();
                UniversityCardView cardView = new UniversityCardView(this, key, value, lytPacketInfo, 0);
                cardView.setOnClickListener(universityCardClickListener);
            }

            for (HashMap.Entry<String, Object> entry : documents.entrySet()) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                ConnectionInfoView v = new ConnectionInfoView(PacketInfoActivity.this, lytDocumentList, R.drawable.ic_twitter, key);
                v.hideSelectors();
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PacketInfoActivity.this, WebViewActivity.class);
                        intent.putExtra(WebViewActivity.TOOLBAR_TITLE, key);
                        intent.putExtra(WebViewActivity.VIEW_URL, value);
                        startActivity(intent);
                    }
                });

                Log.d("WebViewCreate", "URL: " + v.getURL());
            }
            //setQrString(uID, pID);
            //displayConnections(map);
        }
        else{
            finish();
        }
    }

    private View.OnClickListener universityCardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle extras = new Bundle();
            extras.putSerializable("card_map", ((UniversityCardView) view).getMap());
            extras.putString("card_id", ((UniversityCardView) view).getCardID());

            Intent intent = new Intent(PacketInfoActivity.this, CardInfoActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(INTENT_TOOLBAR_TITLE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_INFO", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

}
