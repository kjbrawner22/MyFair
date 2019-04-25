package com.example.myfair.activities.analytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserCardAnalyticsActivity extends AppCompatActivity {

    //Initializing Variables
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
    ArrayList<Calendar> translatedScanDates;
    Timestamp creationDate;
    Long numberOfShares;

    Button generateGraphBtn;

    Calendar fromCalendar = Calendar.getInstance();
    Calendar toCalendar = Calendar.getInstance();
    Calendar creationDatePH;

    long msInDay = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card_analytics);
        if (savedInstanceState == null) { // Collecting the Card Id set from the list in the User Analytics Activity
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                cardId = null;
            } else {
                cardId = extras.getString("cId");
            }
        } else {
            cardId = (String) savedInstanceState.getSerializable("cId");
        }

        db = new FirebaseDatabase(); //Database and XML Initialization
        card  = db.userCards().document(cardId);
        cardSpot = findViewById(R.id.cardPreviewLayout);
        graph = findViewById(R.id.scansGraph);


        generateGraphBtn = findViewById(R.id.genGraphBtn); //Setting listener for the graph button
        generateGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpGraph();
            }
        });

        fromTimeFiller = findViewById(R.id.fromTimeFiller);
        toTimeFiller = findViewById(R.id.toTimeFiller);


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
        fromText = findViewById(R.id.fromText);
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

                Log.e(TAG, "Inputed stuff year "+year+ " month " + month + " dayofMonth " + dayOfMonth);
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
        toText = findViewById(R.id.toText);
        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDialog.show();
            }
        });



        makePreview(card, cardSpot); // Adding the Card display to the top of the view
        numberOfScansText = findViewById(R.id.numberOfScansFiller); // Gathering locations for more XML text views
        dateCreatedFiller = findViewById(R.id.dateCreatedFiller);

        DocumentReference metadata = card.collection("cdata").document("metadata");
        metadata.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map<String,Object> metaData= task.getResult().getData();

                    //collecting the number of shares from the metadata
                    numberOfShares = (Long) metaData.get("shared");
                    String placeHolder = "" + numberOfShares;
                    numberOfScansText.setText(placeHolder);

                    //Collecting and then translating the list of timestamps to Calendars for easier use

                    scanDates = (ArrayList<Timestamp>) metaData.get("scanRegistry");
                    translatedScanDates = translateTimestamps(scanDates);

                    //Collecting the creation date
                    creationDate = (Timestamp) metaData.get("created");
                    creationDatePH = Calendar.getInstance();
                    creationDatePH.setTime(creationDate.toDate());
                    int correctedMonth = creationDatePH.get(Calendar.MONTH)+1;
                    String creationDateString = correctedMonth+"/"+creationDatePH.get(Calendar.DAY_OF_MONTH)+"/"+creationDatePH.get(Calendar.YEAR);
                    dateCreatedFiller.setText(creationDateString);
                    fromTimeFiller.setText(creationDateString);
                    fromCalendar = creationDatePH;
                    fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    fromCalendar.set(Calendar.MINUTE, 0);
                    fromCalendar.set(Calendar.SECOND, 0);
                    fromDialog.updateDate(creationDatePH.get(Calendar.YEAR),creationDatePH.get(Calendar.MONTH),creationDatePH.get(Calendar.DATE));
                    toCalendar = Calendar.getInstance();
                    String temp = (toCalendar.get(Calendar.MONTH)+1)+"/"+toCalendar.get(Calendar.DAY_OF_MONTH)+"/"+toCalendar.get(Calendar.YEAR);
                    toTimeFiller.setText(temp);
                    setUpGraph();
                }
            }
        });



    }

    /**
     * setUpGraph uses the translated Scan Dates arraylist and filters out selections.
     * The selections are then filtered through to create an array of frequencies based on dividing the amount of time between the to and from by 12.
     * These are then sent to the graph as a series to be displayed
     */
    private void setUpGraph(){
        graph.removeAllSeries();


        ArrayList<Calendar> filteredScanDates = translatedScanDates;
        filteredScanDates.removeIf( c -> (c.getTime().before(fromCalendar.getTime())||c.getTime().after(toCalendar.getTime()))); // This statement purges the Scan Times that are outside of the bounds of the to and from

        long timeBetween = (toCalendar.getTimeInMillis()-fromCalendar.getTimeInMillis()); //timeBetween is the time in milliseconds that is between the to and from calendars
        Log.e(TAG, "Checking time between " + timeBetween);

        if(toCalendar.getTimeInMillis()-fromCalendar.getTimeInMillis()<= msInDay){ // If the time frame is only one day
            Log.e(TAG,"24 hours");
            DataPoint[] set = new DataPoint[24];
            int []subDivisions = new int[24];
            for (int i = 0; i < 24; i++){
                subDivisions[i]=0;
            }

            filteredScanDates.forEach(c -> { //This foreach takes each of the remaining scan dates and categorizes them into one of the 12 subDivisions based on the time
                long diff = c.getTimeInMillis() - fromCalendar.getTimeInMillis();
                Log.e(TAG, "Checking the diff "+ diff);
                subDivisions[(int) (diff/3600000)]++;

                if(subDivisions[(int) (diff/3600000)] > 0) Log.e(TAG, "We got 1");
            });

            for (int i = 0; i < 24 ; i++) {
                set[i] = new DataPoint(i,subDivisions[i]);
            }

            NumberFormat nf = NumberFormat.getInstance();

            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf,nf));
            graph.getGridLabelRenderer().setNumHorizontalLabels(5);

            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(24);

            graph.getViewport().setXAxisBoundsManual(true);

            graph.getGridLabelRenderer().setHumanRounding(true);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(set);
            graph.addSeries(series); //Setting up the Series
        }
        else{
            //Time frame takes place over multiple days
            Log.e(TAG,"Multi Day");
            int numberOfDays = toCalendar.get(Calendar.DAY_OF_YEAR)-fromCalendar.get(Calendar.DAY_OF_YEAR);
            int subDivisions[] = new int[numberOfDays];
            DataPoint[] set = new DataPoint[numberOfDays];
            for(int i = 0; i< subDivisions.length; i++){
                subDivisions[i] = 0;
            }
            filteredScanDates.forEach(c -> { //This foreach takes each of the remaining scan dates and categorizes them into one of the 12 subDivisions based on the time
                long diff = c.getTimeInMillis() - fromCalendar.getTimeInMillis();
                if(diff/msInDay < subDivisions.length){
                    Log.e(TAG, "Checking the diff "+ diff);
                    subDivisions[(int) (diff/msInDay)]++;
                    if(subDivisions[(int) (diff/msInDay)] > 0) Log.e(TAG, "We got 1");
                }
            });

            for(int i = 0; i<subDivisions.length; i++){ //This for set up the Series for the graph
                set[i]= new DataPoint(i, subDivisions[i]);
            }


            NumberFormat nf = NumberFormat.getInstance();
            graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf,nf));
            graph.getGridLabelRenderer().setNumHorizontalLabels(6);

            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(numberOfDays);


            graph.getViewport().setXAxisBoundsManual(true);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(set);
            graph.addSeries(series); //Setting up the Series

        }

    }

    private ArrayList<Calendar> translateTimestamps(ArrayList<Timestamp> list){
        ArrayList<Calendar> temp = new ArrayList<>(list.size());
        for( Timestamp elem : list){ //Doing necessary transformations
            Calendar x = Calendar.getInstance();
            x.setTime(elem.toDate());

            temp.add(x);
        }
        return temp;
    }

    private void addCardView(GenericCardView v, LinearLayout listView) {
        listView.addView(v);
        v.setMargins();
    }

    private void makePreview(DocumentReference ref, final LinearLayout listView){
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) { //Read the card into a cardview so it can be displayed
                    DocumentSnapshot document = task.getResult();
                    String cID = document.getId();
                    HashMap<String,Object> map = (HashMap<String,Object>) document.getData();
                    UniversityCardView v = new UniversityCardView(UserCardAnalyticsActivity.this, cID, map, listView);
                    Log.d(TAG, document.getId() + " => " + document.getData());

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
