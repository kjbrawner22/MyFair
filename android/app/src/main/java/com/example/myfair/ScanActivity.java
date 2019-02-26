package com.example.myfair;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.android.gms.common.util.CollectionUtils.listOf;



public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView qrCodeScanner;
    //ImageView barcodeBackImageView;
    ImageView flashOnOffImageView;

    int MY_CAMERA_REQUEST_CODE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);

        qrCodeScanner = findViewById(R.id.qrCodeScanner);
        flashOnOffImageView = findViewById(R.id.flashOnOffImageView);
        MY_CAMERA_REQUEST_CODE = 6515;

        scannerInit(); // Initialize the QR scanning

        /*
         * Button for turning the flash on and off
         */
        flashOnOffImageView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Context cont = getApplicationContext();
                if(qrCodeScanner.getFlash()){
                    qrCodeScanner.setFlash(false);
                    flashOnOffImageView.setBackground(ContextCompat.getDrawable(cont,R.drawable.flash_off_vector_icon));
                }else{
                    qrCodeScanner.setFlash(true);
                    flashOnOffImageView.setBackground(ContextCompat.getDrawable(cont,R.drawable.flash_on_vector_icon));
                }
            }
        });



    }

    private void scannerInit(){
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
    }

    /**
     *  onResume overrides the built in onResume and adds the functionality
     *  of restarting the camera and reassigning the result Handler
     */
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){ //Determine permissions
                requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_REQUEST_CODE);
            }
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(ScanActivity.this);
    }

    /**
     * Pausing camera on pause
     */
    @Override
    protected void onPause(){
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    /**
     * Initializing camera and the result handler
     */
    private void openCamera(){
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

    /**
     * Code for assigning the result handler.
     * Takes a context which is the current activity as per the implementation of the result
     * handler
     * @param p0
     */
    @Override
    public void handleResult(Result p0){
        if(p0!=null){
            ScannedActivity s = new ScannedActivity();
            startActivity(s.getScannedActivity(this,p0.getText()));
            resumeCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == MY_CAMERA_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED){

            }
        }
    }

    /**
     * Timer for restarting the camera after a code has been scanned
     */
    private void resumeCamera(){
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                qrCodeScanner.resumeCameraPreview(ScanActivity.this);
            }
        };
        handler.postDelayed(r,2000);
    }


    /**
     * Return the user to the main menu on pressing the back button
     */
    @Override
    public void onBackPressed(){
        Intent returnToMain = new Intent(this,MainActivity.class);
        startActivity(returnToMain);
    }
}
