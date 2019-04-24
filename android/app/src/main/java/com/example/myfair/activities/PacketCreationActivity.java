package com.example.myfair.activities;

import androidx.annotation.NonNull;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.db.Packet;
import com.example.myfair.views.ConnectionInfoView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import java.util.HashMap;

public class PacketCreationActivity extends AppCompatActivity {
    public static final String INTENT_TOOLBAR_TITLE = "New Packet";
    public static final String TAG = "PacketCreationLog";
    private String docName, docLink;
    private LinearLayout lytCardList, lytDocList;
    private Packet packet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_creation);
        setupToolbar();

        packet = new Packet();
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
                if(!packet.containsCard(cID)) {
                    packet.addCard(cID, map);
                    UniversityCardView card = new UniversityCardView(this, cID, map, lytCardList);
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_packet_creation, menu);
        return true;
    }

    /**
     * Handle actions within the action bar
     * @param item - which item was clicked
     * @return boolean value on if it was handled or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                setNameDialog();
                Log.d(TAG, "result: " + packet.getMap());
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    public void sendData(CollectionReference colRef){
        colRef.add(packet.getMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot packet with ID: " + documentReference.getId());
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
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
                if(!packet.containsDocument(docName)) {
                    ConnectionInfoView connection = new ConnectionInfoView(PacketCreationActivity.this);
                    connection.setImage(R.drawable.ic_drive);
                    connection.setText(docName);
                    lytDocList.addView(connection);
                    packet.addDocument(docName, docLink);
                }

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void setNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name Your Packet");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.name_dialog_entry, null, false);

        final EditText etPacketName = (EditText) viewInflated.findViewById(R.id.etPacketName);

        builder.setView(viewInflated);
        builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                FirebaseDatabase db = new FirebaseDatabase();
                String name = etPacketName.getText().toString();
                if(!name.isEmpty()) {
                    packet.setValue(Packet.FIELD_PACKET_OWNER, db.getUserId());
                    packet.setValue(Packet.FIELD_PACKET_NAME, name);
                    sendData(db.userPackets());
                }
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
