package com.example.myfair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myfair.ModelsandHelpers.EncryptionHelper;
import com.example.myfair.ModelsandHelpers.qrObject;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;


public class ScannedActivity extends AppCompatActivity {

    String SCANNED_STRING = "scanned_string";
    Intent getScannedActivity(Context callingClassContext, String encryptedString){
        return new Intent(callingClassContext,ScannedActivity.class).putExtra(SCANNED_STRING,encryptedString);
    }
    TextView scannedNameTextView;
    TextView scannedUniCompTextView;
    TextView scannedMajPosTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned);

        //SCANNED_STRING = "scanned_string";

        scannedNameTextView = findViewById(R.id.scannedNameTextView);
        scannedUniCompTextView = findViewById(R.id.scannedUniCompTextView);
        scannedMajPosTextView = findViewById(R.id.scannedMajPosTextView);

        if(getIntent().getSerializableExtra(SCANNED_STRING) == null) throw new RuntimeException("No encrypted string found in intent");

        String decryptedString = EncryptionHelper.getInstance().getDecryptionString(getIntent().getStringExtra(SCANNED_STRING));
        qrObject qrObject = new Gson().fromJson(decryptedString, qrObject.class);
        String cID = qrObject.getCardID();
        String uID = qrObject.getUserID();

        //Run the request to get the info from firebase

        /*
        If detect Company instead of University{
            set String in unicomp to Company
            set String in MajPos to Position
        }
        else{
            set the other way
        }
        Present data
        */


    }
}
