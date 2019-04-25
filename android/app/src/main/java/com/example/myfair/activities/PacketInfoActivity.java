package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.db.Packet;
import com.example.myfair.modelsandhelpers.Connection;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.CardInfoView;
import com.example.myfair.views.ConnectionInfoView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;

import java.util.HashMap;

public class PacketInfoActivity extends AppCompatActivity {
    public static final String INTENT_TOOLBAR_TITLE = "Packet Info";
    private LinearLayout lytPacketInfo, lytDocumentList;
    private String encryptedString;
    HashMap<String, Object> cards, documents;
    DocumentReference packetRef;

    public static final String INTENT_PACKET_MAP = "packet_map";
    public static final String INTENT_PACKET_ID = "packet_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_info);

        FirebaseDatabase db = new FirebaseDatabase();
        setupToolbar();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lytPacketInfo = findViewById(R.id.lytCardInfo);
        lytDocumentList = findViewById(R.id.lytDocumentList);
        HashMap<String, Object> map;
        String pID;

        if(bundle != null) {
            map = (HashMap<String, Object>) bundle.getSerializable(INTENT_PACKET_MAP);
            pID = bundle.getString(INTENT_PACKET_ID);
            String uID = (String) map.get(Packet.FIELD_PACKET_OWNER);
            setQrString(uID, pID);

            if(uID!=null && uID.equals(db.getUserId()))
                packetRef = db.getPacketRef(uID, pID);
            else
                packetRef = db.packetsLibrary().document(pID);

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
                ConnectionInfoView v = new ConnectionInfoView(PacketInfoActivity.this, lytDocumentList, R.drawable.ic_drive, key);
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

    private void shareCard() {
        Bundle bundle = new Bundle();
        bundle.putString("encryptedString", encryptedString);
        BottomSheet bottomSheet = new BottomSheet();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }

    private void setQrString(String uID, String pID) {
        qrObject user = new qrObject(uID, pID);
        user.setType(qrObject.VALUE_TYPE_PACKET);
        String serializeString = new Gson().toJson(user);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        Log.d("CardInfoActivityLog", "CardInfoView: " + encryptedString);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_card_info, menu);
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
            case R.id.action_delete:
                createDialog();
                break;
            case R.id.action_share:
                shareCard();
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

    private void createDialog() {
        final String TAG = "createDialog";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete this packet?");

        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                packetRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
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
