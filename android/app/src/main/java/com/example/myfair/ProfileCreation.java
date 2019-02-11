package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileCreation extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileCreation";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText etFname, etLname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnUpdateInfo = findViewById(R.id.btnUpdateInfo);
        btnBack.setOnClickListener(this);
        btnUpdateInfo.setOnClickListener(this);
    }
    @Override
    public void onClick(final View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBack:
                Intent intent = new Intent(ProfileCreation.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnUpdateInfo:
                if(validProfileFields()) {
                    updateProfile();
                    break;
                }
                Toast.makeText(this, "" + "Fill required fields.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void updateProfile(){
        final String firstName = etFname.getText().toString();
        final String lastName = etLname.getText().toString();
        // final String universityName = etUniversityName.getText().toString();

        User user = new User(firstName, lastName);
        // user.setValue(User.FIELD_UNIVERSITY_NAME, universityName);
        updateUser(user);
    }

    private void updateUser(User usrObj) {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.set(usrObj.getMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "DocumentSnapshot successfully updated!");
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

}
