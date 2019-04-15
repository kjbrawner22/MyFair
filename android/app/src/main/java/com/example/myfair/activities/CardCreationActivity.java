package com.example.myfair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import static com.example.myfair.activities.CardViewingActivity.INTENT_TOOLBAR_TITLE;

public class CardCreationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CardCreationActivity";
    private EditText etName, etCompany, etPosition;
    private String fullName, company, position;
    private Button btnDone;
    private ConstraintLayout lytCompany;
    LinearLayout lytPreview;
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

        setupToolbar();

        database = new FirebaseDatabase();
        user = FirebaseAuth.getInstance().getCurrentUser();

        lytCompany = findViewById(R.id.lytCompany);

        etName = findViewById(R.id.etName);
        etCompany = findViewById(R.id.etCompany);
        etPosition = findViewById(R.id.etPosition);
        lytPreview = findViewById(R.id.lytPreview);

        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(this);

        UniversityCardView v = new UniversityCardView(this, "empty", null);
        lytPreview.addView(v);
        v.setMargins();

        etName.addTextChangedListener(createTextWatcher(v.getNameView()));
        etCompany.addTextChangedListener(createTextWatcher(v.getUniversityView()));
        etPosition.addTextChangedListener(createTextWatcher(v.getMajorView()));

        changeForm(2);
        //initialize contents of text boxes to values inside database
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Log.d("ACTION_BAR_CARD_VIEWING", "Actionbar: " + actionBar);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * onClick override to handle all click listeners.
     * @param v - view that was clicked
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnDone:
                //send info to database
                setStrings();
                if (validFields()) {
                    updateData();
                }
                //update back to home fragment
                break;
            default:
                break;
        }
    }

    private TextWatcher createTextWatcher(final TextView textView) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textView.setText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
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
        fullName = etName.getText().toString();
        company = etCompany.getText().toString();
        position = etPosition.getText().toString();
    }

    private boolean validFields() {
        if(form == 2){
            return validate(etName, etCompany, etPosition);
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
        if (lytCompany.getVisibility() == View.VISIBLE) return 2;
        else return 3;
    }

    /**
     * change form view
     * @param formID - state of the desired view setting
     */
    private void changeForm(int formID){
        form = formID;
        switch(formID){
            case 2:
                btnDone.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                lytCompany.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("ChangeFormLog", "Form Not Yet Implemented");
        }
    }
}
