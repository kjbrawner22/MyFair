package com.example.myfair.activities;

import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.myfair.R;
import com.example.myfair.db.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileCreationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileCreationTag";


    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText etFname, etLname, etUsername;
    FloatingActionButton btnBack, btnForward;
    private ConstraintLayout lytName, lytSocial;
    private User dbUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        dbUser = new User();
        btnBack = findViewById(R.id.btnBack);
        btnForward = findViewById(R.id.btnForward);
        lytName = findViewById(R.id.lytName);
        lytSocial = findViewById(R.id.lytSocial);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etUsername = findViewById(R.id.etUsername);

        changeForm(1);

        btnBack.setOnClickListener(this);
        btnForward.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        int state = getFormState();
        switch (state) {
            case 1:
                if(id == R.id.btnForward) {
                    if (validProfileFields()) {
                        dbUser.setValue(User.FIELD_FIRST_NAME, etFname.getText().toString());
                        dbUser.setValue(User.FIELD_LAST_NAME, etLname.getText().toString());
                        changeForm(2);
                    }
                }
                break;
            case 2:
                if(id == R.id.btnForward){
                    dbUser.setValue(User.FIELD_USERNAME, etUsername.getText().toString());
                    updateUser();
                } else if (id == R.id.btnBack){
                    changeForm(1);
                }
                break;
        }
    }

    private void updateUser() {
        dbUser.setValue(User.FIELD_PROFILE_CREATED, User.VALUE_TRUE);
        dbUser.sendToDb();
        updateUI();
    }

    private boolean validProfileFields() {
        final String fname = etFname.getText().toString();
        final String lname = etLname.getText().toString();

        if (fname.isEmpty()) {
            etFname.setError("First name field is required.");
            etFname.requestFocus();
        } else if (lname.isEmpty()) {
            etLname.setError("Last name field is required.");
            etLname.requestFocus();
        } else {
            return true;
        }

        return false;
    }

    private int getFormState(){
        if(lytName.getVisibility() == View.VISIBLE) {
            return 1;
        } else {
            return 2;
        }
    }

    // change form view
    private void changeForm(int state) {
        switch (state){
            case 1:
                btnBack.hide();
                lytName.setVisibility(View.VISIBLE);
                lytSocial.setVisibility(View.GONE);
                break;
            case 2:
                btnBack.show();
                lytName.setVisibility(View.GONE);
                lytSocial.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("PC ChangeForm", "wrong input param");
        }

    }

    private void updateUI() {
        boolean returnToMain = getIntent().getBooleanExtra(User.FIELD_PROFILE_CREATED, false);

        // if profile hasn't already been created, create a new main activity instance
        // otherwise, just finish this activity and go back to the previous
        // main activity instance
        if (!returnToMain) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
