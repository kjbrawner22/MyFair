package com.example.myfair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.views.UniversityCardView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CardCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CardCreationActivity";
    private EditText etName, etCompany, etPosition, etUniversity, etMajor, etName2;
    private String fullName, company, position, university, major;
    private Button btnBack;
    private Button btnDone;
    private ConstraintLayout lytSelect, lytUniversity, lytCompany;
    FirebaseDatabase database;
    FirebaseUser user;
    int form;

    /**
     * Standard onCreate override. Finds needed handles and initializes view.
     * @param savedInstanceState App's saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_creation);

        database = new FirebaseDatabase();
        user = FirebaseAuth.getInstance().getCurrentUser();

        lytSelect = findViewById(R.id.lytSelect);
        lytUniversity = findViewById(R.id.lytUniversity);
        lytCompany = findViewById(R.id.lytCompany);

        etName = findViewById(R.id.etName);
        etName2 = findViewById(R.id.etName2);
        etCompany = findViewById(R.id.etCompany);
        etPosition = findViewById(R.id.etPosition);
        etUniversity = findViewById(R.id.etUniversity);
        etMajor = findViewById(R.id.etMajor);

        Button btnUniversity = findViewById(R.id.btnUniversity);
        Button btnCompany = findViewById(R.id.btnCompany);
        btnBack = findViewById(R.id.btnBack1);
        btnDone = findViewById(R.id.btnDone);

        btnUniversity.setOnClickListener(this);
        btnCompany.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnDone.setOnClickListener(this);

        changeForm(1);
        //initialize contents of text boxes to values inside database
    }

    /**
     * onClick override to handle all click listeners.
     * @param v - view that was clicked
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBack1:
                changeForm(1);
                break;
            case R.id.btnCompany:
                changeForm(2);
                break;
            case R.id.btnUniversity:
                changeForm(3);
                break;
            case R.id.btnDone:
                //send info to database
                setStrings();
                if (validFields()) {
                    updateData();
                }
                //update back to home fragment
        }
    }

    /**
     * Helper function responsible for updating the information on a card
     * */
    private void updateData(){
        Card localCard = new Card(fullName);
        localCard.setValue(Card.FIELD_CARD_OWNER, user.getUid());
        if(getForm() == 2){
            localCard.setValue(Card.FIELD_TYPE, Card.VALUE_TYPE_BUSINESS);
            localCard.setValue(Card.FIELD_COMPANY_NAME, company);
            localCard.setValue(Card.FIELD_COMPANY_POSITION, position);
        }
        else if(getForm() == 3){
            localCard.setValue(Card.FIELD_TYPE, Card.VALUE_TYPE_UNIVERSITY);
            localCard.setValue(Card.FIELD_UNIVERSITY_NAME, university);
            localCard.setValue(Card.FIELD_UNIVERSITY_MAJOR, major);
        }
        Log.d("CardCreationLog", "Map for card: " + localCard.getMap());
        localCard.sendToDb(Card.VALUE_NEW_CARD);
        updateUI();
    }

    /**
     * Helper function responsible for updating the UI on a card
     * */
    private void updateUI() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Helper function responsible for setting the text strings on a card
     * */
    private void setStrings(){
        if(getForm() == 2){
             fullName = etName.getText().toString();
             company = etCompany.getText().toString();
             position = etPosition.getText().toString();
         }
         else {
             fullName = etName2.getText().toString();
             university = etUniversity.getText().toString();
             major = etMajor.getText().toString();
         }
    }

    private boolean validFields() {
        if(form == 2){
            return validate(etName, etCompany, etPosition);
        }
        else if (form == 3){
            return validate(etName2, etUniversity, etMajor);
        }
        else
        {
            return false;
        }
    }

    private boolean validate(EditText etOne, EditText etTwo, EditText etThree){
        if (fullName.isEmpty()) {
            etOne.setError("Name is required.");
            etOne.requestFocus();
            return false;
        } else if (company.isEmpty()) {
            etTwo.setError("Company Name is required.");
            etTwo.requestFocus();
            return false;
        } else if (position.isEmpty()) {
            etThree.setError("Position is required.");
            etThree.requestFocus();
            return false;
        }
        return true;
    }


    /**
     * get the form's state.
     * @return int state value
     */
    private int getForm(){
        if(lytSelect.getVisibility() == View.VISIBLE) return 1;
        else if (lytCompany.getVisibility() == View.VISIBLE) return 2;
        else return 3;
    }

    /**
     * change form view
     * @param formID - state of the desired view setting
     */
    private void changeForm(int formID){
        form = formID;
        switch(formID){
            case 1:
                btnBack.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                etName.setVisibility(View.GONE);
                lytSelect.setVisibility(View.VISIBLE);
                lytCompany.setVisibility(View.GONE);
                lytUniversity.setVisibility(View.GONE);
                break;
            case 2:
                btnBack.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                lytSelect.setVisibility(View.GONE);
                lytCompany.setVisibility(View.VISIBLE);
                lytUniversity.setVisibility(View.GONE);
                break;
            case 3:
                btnBack.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                lytSelect.setVisibility(View.GONE);
                lytCompany.setVisibility(View.GONE);
                lytUniversity.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("ChangeFormLog", "Form Not Yet Implemented");
        }
    }
}
