package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProfileCreation extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "ProfileCreation";
    private TextView txtFname, txtLname;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        txtFname = findViewById(R.id.txtFname);
        txtLname = findViewById(R.id.txtLname);

        addItemsOnSpinner();
        addSpinnerListener();

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
                    updateClickRoutine();
                    break;
                }
                Toast.makeText(this, "" + "Fill required fields.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void updateClickRoutine(){
        spinner = (Spinner) findViewById(R.id.spinner);
        String strType = spinner.getSelectedItem().toString();
        if(strType.equals("Student")){
            Student studentObj = new Student(txtFname.getText().toString(), txtLname.getText().toString(),"","","");
            updateUser(studentObj);
        }
        else {
            Worker workerObj = new Worker(txtFname.getText().toString(), txtLname.getText().toString(), "", "");
            updateUser(workerObj);
        }
    }

    public void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Student");
        list.add("Recruiter");
        list.add("Employer");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void addSpinnerListener() {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new OnItemSelectedCustom());
    }

    private void updateUser(User usrObj) {
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.set(usrObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

    private boolean validProfileFields() {
        spinner = (Spinner) findViewById(R.id.spinner);

        final String fname = txtFname.getText().toString();
        final String lname = txtLname.getText().toString();
        final String usrType = spinner.getSelectedItem().toString();

        if (fname.isEmpty()) {
            txtFname.setError("First name field is required.");
            txtFname.requestFocus();
        } else if (lname.isEmpty()) {
            txtLname.setError("Last name field is required.");
            txtLname.requestFocus();
        } else if (usrType.isEmpty()) {
            spinner.setPrompt("Select item");
            spinner.requestFocus();
        }
        else {
            return true;
        }

        return false;
    }

}
