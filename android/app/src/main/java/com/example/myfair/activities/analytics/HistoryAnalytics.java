package com.example.myfair.activities.analytics;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfair.R;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;


public class HistoryAnalytics extends AppCompatActivity {

    String TAG = "History Analytic";

    TextView fromTimeFiller;
    TextView toTimeFiller;
    TextView fromText;
    TextView toText;

    Button refreshBtn;

    Calendar fromCalendar = Calendar.getInstance();
    Calendar toCalendar = Calendar.getInstance();

    DatePickerDialog fromDialog;
    DatePickerDialog toDialog;
    ScrollView cardScroller;
    LinearLayout cardList;

    FirebaseDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_analytics);
        db = new FirebaseDatabase();
        cardScroller = findViewById(R.id.scannedCardScroll);
        cardList = findViewById(R.id.scannedCardList);

        refreshBtn = findViewById(R.id.refreshButton);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardList.removeAllViews();
                getIdList(db.userContacts(), cardList);
            }
        });

        fromText = findViewById(R.id.textFrom);
        toText = findViewById(R.id.textTo);

        fromTimeFiller = findViewById(R.id.fromDateText);
        toTimeFiller = findViewById(R.id.toDateText);

        //This is the Date Picker Dialog box that opens up when the from text is clicked
        fromDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance(); // A calendar is initialized and set with the new date.
                newDate.set(year, month, dayOfMonth);
                int correctedMonth = newDate.get(Calendar.MONTH)+1;
                String d = correctedMonth+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR);
                fromTimeFiller.setText(d); // The filler text view is set with the text and then the fromCalendar is set to hold the new values
                fromTimeFiller.setTextSize(24);
                Log.e(TAG, "Inputed stuff year "+year+ " month " + month + " dayofMonth " + dayOfMonth);
                fromCalendar.set(Calendar.YEAR,year);
                fromCalendar.set(Calendar.MONTH,month);
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                fromCalendar.set(Calendar.HOUR_OF_DAY, 0 );
                fromCalendar.set(Calendar.MINUTE, 0);
                fromCalendar.set(Calendar.SECOND,0);

            }
        }, fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
        fromText.setOnClickListener(new View.OnClickListener() {
            @Override //On click listener for setting up the date picker dialog box
            public void onClick(View v) {
                fromDialog.show();
            }
        });

        toDialog = new DatePickerDialog(this); //Similar to above but for the to option
        toDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                int correctedMonth = newDate.get(Calendar.MONTH)+1;
                String d = correctedMonth+"/"+newDate.get(Calendar.DAY_OF_MONTH)+"/"+newDate.get(Calendar.YEAR);
                toTimeFiller.setText(d);
                toTimeFiller.setTextSize(24);
                toCalendar.set(Calendar.YEAR,year);
                toCalendar.set(Calendar.MONTH,month);
                toCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                toCalendar.set(Calendar.HOUR_OF_DAY, 23);
                toCalendar.set(Calendar.MINUTE, 59);
                toCalendar.set(Calendar.SECOND, 59);
            }
        }, toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDialog.show();
            }
        });

        fromCalendar.set(1990,0,1,0,0,0);

        //Pulling Card History history
        getIdList(db.userContacts(), cardList);
    }

    /**
     * Gets the list of IDs of cards and populates a linear layout with cardViews.
     * */
    private void getIdList(CollectionReference ref, final LinearLayout listView){
        ref.whereLessThanOrEqualTo("scan_date",new Timestamp(toCalendar.getTime()))
            .whereGreaterThanOrEqualTo("scan_date",new Timestamp(fromCalendar.getTime()))
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String cID = document.getId();
                        HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                        //String type = (String) map.get(Card.FIELD_TYPE);
                        new UniversityCardView(HistoryAnalytics.this, cID, map, listView);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }



}
