package com.example.myfair;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myfair.ModelsandHelpers.EncryptionHelper;
import com.example.myfair.ModelsandHelpers.UserObject;
import com.google.gson.Gson;


public class ScannedActivity extends AppCompatActivity {

    String SCANNED_STRING = "scanned_string";
    Intent getScannedActivity(Context callingClassContext, String encryptedString){
        return new Intent(callingClassContext,ScannedActivity.class).putExtra(SCANNED_STRING,encryptedString);
    }
    TextView scannedFullNameTextView;
    TextView scannedAgeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned);

        //SCANNED_STRING = "scanned_string";
        scannedAgeTextView = findViewById(R.id.scannedAgeTextView);
        scannedFullNameTextView = findViewById(R.id.scannedFullNameTextView);

        if(getIntent().getSerializableExtra(SCANNED_STRING) == null) throw new RuntimeException("No encrypted string found in intent");

        String decryptedString = EncryptionHelper.getInstance().getDecryptionString(getIntent().getStringExtra(SCANNED_STRING));
        UserObject userObject = new Gson().fromJson(decryptedString,UserObject.class);
        String age = Integer.toString(userObject.getAge());
        String fN = userObject.getFullName();

        scannedFullNameTextView.setText(fN);
        scannedAgeTextView.setText(age);


    }
}
