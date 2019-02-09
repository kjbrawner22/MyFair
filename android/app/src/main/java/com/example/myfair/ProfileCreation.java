package com.example.myfair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileCreation extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

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
                Toast.makeText(this, "" + user.getUid(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
