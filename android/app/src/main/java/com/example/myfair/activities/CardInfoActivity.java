package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.myfair.modelsandhelpers.Connection;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.example.myfair.views.BottomSheet;
import com.example.myfair.views.ConnectionInfoView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.gson.Gson;

import java.util.HashMap;

public class CardInfoActivity extends AppCompatActivity {
    private static final String TAG = "CardInfoActivityLog";
    private LinearLayout lytCardInfo;
    private UniversityCardView cardView;
    private String encryptedString;
    DocumentReference cardRef;


    public static final String INTENT_TOOLBAR_TITLE = "Card Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        FirebaseDatabase db = new FirebaseDatabase();
        TextView cardBio = findViewById(R.id.tvCardBio);
        setupToolbar(INTENT_TOOLBAR_TITLE);

        FloatingActionButton fabShare;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(fabListener);
        lytCardInfo = findViewById(R.id.lytCardInfo);
        HashMap<String, Object> map;
        String cID;


        if(bundle != null) {
            map = (HashMap<String, Object>) bundle.getSerializable("card_map");
            cID = bundle.getString("card_id");
            Log.d("CardInfoActivityLog", "uID: "+ (String) map.get(Card.FIELD_CARD_OWNER) + "cID: " + cID);
            cardView = new UniversityCardView(this, cID, map, lytCardInfo, 0);
            String name = (String) map.get(Card.FIELD_NAME);
            String uID = (String) map.get(Card.FIELD_CARD_OWNER);
            setupToolbar(name);

            if(map.containsKey(Card.FIELD_ABOUT)){
                cardBio.setText((String) map.get(Card.FIELD_ABOUT));
            }

            setQrString(uID, cID);
            cardRef = db.getCardRef(uID, cID);

            displayConnections(map);
        }
        else{
            finish();
        }
    }

    private void displayConnections(HashMap<String, Object> map) {
        LinearLayout lytConnections = findViewById(R.id.lytConnections);

        for (Connection connection : Connection.getConnectionList()) {
            String key = connection.getDbKey();
            if (map.containsKey(key)) {
                connection.setValue((String) map.get(key));
                ConnectionInfoView view = new ConnectionInfoView(this, lytConnections, connection);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Connection.isPhoneNumber(connection.getDbKey())) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + connection.getValue()));
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(CardInfoActivity.this, WebViewActivity.class);
                            intent.putExtra(WebViewActivity.VIEW_URL,
                                    Connection.getInternetUrl(connection.getDbKey(), connection.getValue()));
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
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
                cardRef.delete()
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

    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            shareCard();
        }
    };

    private void shareCard(){
        Bundle bundle = new Bundle();
        bundle.putString("encryptedString", encryptedString);
        BottomSheet bottomSheet = new BottomSheet();
        bottomSheet.setArguments(bundle);
        bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
    }

    private void setQrString(String uID, String cID){
        qrObject user = new qrObject(uID, cID);
        String serializeString = new Gson().toJson(user);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        Log.d("CardInfoActivityLog", "CardInfoView: " + encryptedString);
    }
}
