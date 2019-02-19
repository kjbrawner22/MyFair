package com.example.myfair;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myfair.ModelsandHelpers.EncryptionHelper;
import com.example.myfair.ModelsandHelpers.QRCodeHelper;
import com.example.myfair.ModelsandHelpers.qrObject;
import com.google.gson.Gson;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import androidx.appcompat.app.AppCompatActivity;


public class GenerateActivity extends AppCompatActivity {

    Button generateQRCodeButton;
    ImageView qrCodeImageView;
    EditText fullNameEditText;
    EditText ageEditText;
    Button btnHome;

    String c = "Uam9fiPu3njaBxbkO3D7";
    String u = "4j6fK7UvU7MLDXwqHoFCtWw97Qy2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        btnHome = findViewById(R.id.genBtnHome);
        generateQRCodeButton = findViewById(R.id.generateQrCodeButton);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        ageEditText = findViewById(R.id.ageEditText);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenerateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        generateQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditText()){
                    hideKeyboard();
                    qrObject user = new qrObject(u, c);
                    String serializeString = new Gson().toJson(user);
                    String encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
                    setImageBitmap(encryptedString);
                }
            }
        });





    }

    private void setImageBitmap(String encrypted){
        Bitmap bitmap = QRCodeHelper.newInstance(this).setContent(encrypted).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        qrCodeImageView.setImageBitmap(bitmap);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private Boolean checkEditText(){
        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(this, "fullName field cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(ageEditText.getText().toString())){
            Toast.makeText(this,"Age field cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent returnToMain = new Intent(this,MainActivity.class);
        startActivity(returnToMain);
    }

}
