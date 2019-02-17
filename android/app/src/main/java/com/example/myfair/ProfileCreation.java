package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileCreation extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileCreation";


    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText etFname, etLname, etUsername;
    private ConstraintLayout lytName, lytSocial, lytWelcome;
    private String firstName, lastName, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Button btnBack = findViewById(R.id.btnBack);
        Button btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        lytName = findViewById(R.id.lytName);
        lytSocial = findViewById(R.id.lytSocial);
        lytWelcome = findViewById(R.id.lytWelcome);
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etUsername = findViewById(R.id.etUsername);

        changeForm(1);

        btnBack.setOnClickListener(this);
        btnUpdateInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        int state = getFormState();
        switch (state){
            case 1:
                if(id == R.id.btnUpdateInfo) {
                    firstName = etFname.getText().toString();
                    lastName = etLname.getText().toString();
                    changeForm(2);
                }
                break;
            case 2:
                if(id == R.id.btnUpdateInfo){
                    username = etUsername.getText().toString();
                    changeForm(3);
                }
                else if (id == R.id.btnBack){
                    changeForm(1);
                }
                break;
            case 3:
                if(id == R.id.btnUpdateInfo){
                    User usrObj = new User(firstName, lastName);
                    usrObj.setValue(User.FIELD_USERNAME, username);
                    usrObj.setValue(User.FIELD_PROFILE_CREATED, "true");
                    updateUser(usrObj);
                }
                else if(id == R.id.btnBack){
                    changeForm(2);
                }
                break;
        }
    }

    private void updateUser(User usrObj) {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.set(usrObj.getMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                    updateUI();
                } else {
                    Log.d(TAG, "Error updating document");
                }
            }
        });
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
        if(lytName.getVisibility() == View.VISIBLE) return 1;
        else if (lytSocial.getVisibility() == View.VISIBLE) return 2;
        else return 3;
    }

    // change form view
    private void changeForm(int state) {
        Button btnBack = findViewById(R.id.btnBack);
        switch (state){
            case 1:
                btnBack.setVisibility(View.GONE);
                lytName.setVisibility(View.VISIBLE);
                lytSocial.setVisibility(View.GONE);
                lytWelcome.setVisibility(View.GONE);
                break;
            case 2:
                btnBack.setVisibility(View.VISIBLE);
                lytName.setVisibility(View.GONE);
                lytSocial.setVisibility(View.VISIBLE);
                lytWelcome.setVisibility(View.GONE);
                break;
            case 3:
                btnBack.setVisibility(View.VISIBLE);
                lytName.setVisibility(View.GONE);
                lytSocial.setVisibility(View.GONE);
                lytWelcome.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("PC ChangeForm", "wrong input param");
        }

    }

    private void updateUI() {
        Intent intent = new Intent(ProfileCreation.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
