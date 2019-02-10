package com.example.myfair;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileCreation extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "ProfileCreation";
    private User2 usrObj = new User2("student", "Josh", "Helms");
    private TextView txtFname, txtLname, txtType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        txtFname = findViewById(R.id.txtFname);
        txtLname = findViewById(R.id.txtLname);
        txtType = findViewById(R.id.txtType);

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
                usrObj.setFirstName(txtFname.getText().toString());
                usrObj.setLastName(txtLname.getText().toString());
                usrObj.setType(txtType.getText().toString());
                updateUser();
                Toast.makeText(this, "" + user.getUid(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateUser() {
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

}
