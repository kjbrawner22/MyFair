package com.example.myfair.activities.analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.BusinessCardView;
import com.example.myfair.views.GenericCardView;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.jjoe64.graphview.GraphView;


import java.sql.Time;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserCardAnalyticsActivity extends AppCompatActivity {

    FirebaseDatabase db;
    String cardId;
    DocumentReference card;
    LinearLayout cardSpot;
    String TAG = "UserCardAnalyticsActivity";
    GraphView graph;
    DatePickerDialog fromDialog;
    DatePickerDialog toDialog;
    TextView fromText;
    TextView toText;
    TextView fromTimeFiller;
    TextView toTimeFiller;
    TextView numberOfScansText;
    TextView dateCreatedFiller;

    ArrayList<Timestamp> scanDates;
    Timestamp creationDate;
    Long numberOfShares;


    final Calendar fromCalendar = Calendar.getInstance();
    final Calendar toCalendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_analytics);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cardId = null;
            } else {
                cardId = extras.getString("cId");
            }
        } else {
            cardId = (String) savedInstanceState.getSerializable("cId");
        }

        db = new FirebaseDatabase();
        card  = db.userCards().document(cardId);
        cardSpot = findViewById(R.id.cardPreviewLayout);
        graph = findViewById(R.id.scansGraph);

        fromTimeFiller = findViewById(R.id.fromTimeFiller);
        toTimeFiller = findViewById(R.id.toTimeFiller);



        fromDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                String d = newDate.get(Calendar.MONTH)+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR);
                fromTimeFiller.setText(d);
                fromTimeFiller.setTextSize(24);
            }
        }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
        fromText = findViewById(R.id.fromText);
        fromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDialog.show();
            }
        });

        toDialog = new DatePickerDialog(this);
        toDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                String d = newDate.get(Calendar.MONTH)+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR);
                toTimeFiller.setText(d);
                toTimeFiller.setTextSize(24);
            }
        }, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
        toText = findViewById(R.id.toText);
        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDialog.show();
            }
        });



        makePreview(card, cardSpot);
        numberOfScansText = findViewById(R.id.numberOfScansFiller);
        dateCreatedFiller = findViewById(R.id.dateCreatedFiller);

        DocumentReference metadata = card.collection("cdata").document("metadata");
        metadata.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map<String,Object> metaData= task.getResult().getData();
                    numberOfShares = (Long) metaData.get("shared");
                    String placeHolder = "" + numberOfShares;
                    numberOfScansText.setText(placeHolder);

                    scanDates = (ArrayList<Timestamp>) metaData.get("scanRegistry");
                    creationDate = (Timestamp) metaData.get("created");
                    Calendar creationDatePH = Calendar.getInstance();
                    creationDatePH.setTime(creationDate.toDate());
                    String creationDateString = creationDatePH.get(Calendar.MONTH)+"/"+creationDatePH.get(Calendar.DAY_OF_MONTH)+"/"+creationDatePH.get(Calendar.YEAR);
                    dateCreatedFiller.setText(creationDateString);
                }
            }
        });
    }

    private void addCardView(GenericCardView v, LinearLayout listView) {
        listView.addView(v);
        v.setMargins();
    }

    private void makePreview(DocumentReference ref, final LinearLayout listView){
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String cID = document.getId();
                    HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                    String type = (String) map.get(Card.FIELD_TYPE);
                    if(type != null && type.equals(Card.VALUE_TYPE_BUSINESS)) {
                        BusinessCardView v = new BusinessCardView(UserCardAnalyticsActivity.this, cID, map);
                        addCardView(v, listView);
                    }
                    else if(type != null){
                        UniversityCardView v = new UniversityCardView(UserCardAnalyticsActivity.this, cID, map);
                        addCardView(v, listView);
                    }
                    Log.d(TAG, document.getId() + " => " + document.getData());

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
