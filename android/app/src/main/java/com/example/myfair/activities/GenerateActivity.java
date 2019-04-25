package com.example.myfair.activities;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.QRCodeHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class GenerateActivity extends AppCompatActivity {

    Button generateQRCodeButton;
    ImageView qrCodeImageView;
    EditText fullNameEditText;
    EditText ageEditText;

    // info send from onClick CardView -- sends cID & uID with putExtra.
    // private vars (CardView-> cID / uID) should be set properly
    // using database info at initialization.


    String c;
    String u;

    //String c = "Uam9fiPu3njaBxbkO3D7"; //Hard coded value for demo purposes
    //String u = "4j6fK7UvU7MLDXwqHoFCtWw97Qy2"; //Hard coded value for demo purposes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        Intent intent = getIntent();
        c = intent.getExtras().getString("cID");
        u = intent.getExtras().getString("uID");

        generateQRCodeButton = findViewById(R.id.generateQrCodeButton);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        fullNameEditText = findViewById(R.id.fullNameEditText); //Will be removed in future version
        ageEditText = findViewById(R.id.ageEditText); //Will be removed in future version


        /*
         * generateQRCodeButton
         * Sends the userId and cardId to be first serialized into a QRObject Structure
         * The QR Object is then sent to be Encrypted. The Encrypted QR Object is then sent as a
         * param to setImageBitmap
         *
         */
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

    /**
     * setImageBitmap creates the QR code via the QRCodeHelper Class
     * @param encrypted
     */
    private void setImageBitmap(String encrypted){
        Bitmap bitmap = QRCodeHelper.newInstance(this).setContent(encrypted).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        qrCodeImageView.setImageBitmap(bitmap);
    }

    /**
     * Utility function to hide the keyBoard following text input
     */
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0); // Hide Keyboard
        }
    }

    /**
     * Determines whether text has been input into the two entry fields on the screen.
     * Will be removed in next version of the generate activity to enhance streamline functionality
     * @return Boolean
     */
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

    /**
     * Return user to the main screen on back button
     */
    @Override
    public void onBackPressed(){
        Intent returnToMain = new Intent(this,MainActivity.class);
        startActivity(returnToMain);
    }

}
