package com.example.myfair.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.PacketView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class PacketViewingActivity extends AppCompatActivity {
    FirebaseDatabase db;

    public static final String INTENT_TOOLBAR_TITLE = "Your Packets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packet_viewing);

        setupToolbar();

        db = new FirebaseDatabase();
        LinearLayout packetList = findViewById(R.id.lytListView);

        getIdList(db.userPackets(), packetList);
    }

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
                        String pID = document.getId();
                        HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                        PacketView v = new PacketView(PacketViewingActivity.this, pID, listView, map);
                        v.setOnClickListener(packetClickListener);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private View.OnClickListener packetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle extras = new Bundle();
            extras.putSerializable("packet_map", ((PacketView) view).getMap());
            extras.putString("packet_id", ((PacketView) view).getPacketId());

            Intent intent = new Intent(PacketViewingActivity.this, PacketInfoActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    };
}