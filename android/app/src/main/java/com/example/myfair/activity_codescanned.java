package com.example.myfair;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class activity_codescanned extends AppCompatActivity {

    String SCANNED_STRING;
    Intent getScannedActivity(Context callingClassContext, String encryptedString){
        return new Intent(callingClassContext,activity_codescanned.class).putExtra(SCANNED_STRING,encryptedString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codescanned);
    }
}
