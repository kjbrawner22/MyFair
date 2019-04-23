package com.example.myfair.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myfair.R;

import java.net.URL;

public class PacketCreationActivity extends AppCompatActivity {
    public static final String INTENT_TOOLBAR_TITLE = "New Packet";
    private String docName, docLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_creation);
        setupToolbar();


        CardView cvAddDoc = findViewById(R.id.cvAddDoc);
        cvAddDoc.setOnClickListener(cardListener);
    }

    private View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            createDialog();
            Log.d("PacketCreationLog", "Strings: " + docName + ", " + docLink);
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
