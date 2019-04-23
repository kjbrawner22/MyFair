package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.views.UniversityCardView;

import java.net.URL;
import java.util.HashMap;

public class PacketCreationActivity extends AppCompatActivity {
    public static final String INTENT_TOOLBAR_TITLE = "New Packet";
    public static final String TAG = "PacketCreationLog";
    private String docName, docLink;
    private LinearLayout lytCardList, lytDocList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_creation);
        setupToolbar();

        lytCardList = findViewById(R.id.lytCardList);
        lytDocList = findViewById(R.id.lytDocList);

        CardView cvAddDoc = findViewById(R.id.cvAddDoc);
        cvAddDoc.setOnClickListener(docListener);
        CardView cvAddCard = findViewById(R.id.cvAddCard);
        cvAddCard.setOnClickListener(cardListener);

    }

    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(PacketCreationActivity.this, CardPickerActivity.class);
            startActivityForResult(i, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                HashMap<String, Object> map = (HashMap<String, Object>) data.getSerializableExtra("card_map");
                String cID = data.getStringExtra("card_id");
                UniversityCardView card = new UniversityCardView(this, cID, map, lytCardList);
                Log.d(TAG, "result: " + cID);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private View.OnClickListener docListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createDialog();
            Log.d(TAG, "Strings: " + docName + ", " + docLink);
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle(INTENT_TOOLBAR_TITLE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_VIEWING", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Document Info");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_entry, null, false);

        final EditText etDocName = (EditText) viewInflated.findViewById(R.id.etDocName);
        final EditText etDocLink = (EditText) viewInflated.findViewById(R.id.etDocLink);

        builder.setView(viewInflated);
        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                docName = etDocName.getText().toString();
                docLink = etDocLink.getText().toString();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
