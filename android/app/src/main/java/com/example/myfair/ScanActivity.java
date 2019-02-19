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
import android.widget.Button;
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
        //barcodeBackImageView = findViewById(R.id.barcodeBackImageView);
        flashOnOffImageView = findViewById(R.id.flashOnOffImageView);
        MY_CAMERA_REQUEST_CODE = 6515;

        scannerInit();


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


        Button goHome = findViewById(R.id.scanbtnHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                onPause();
                startActivity(intent);
                finish();
            }
        });
    }

    private void scannerInit(){
        qrCodeScanner.setFormats(listOf(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA},MY_CAMERA_REQUEST_CODE);
            }
        }
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(ScanActivity.this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    private void openCamera(){
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }

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

    @Override
    public void onBackPressed(){
        Intent returnToMain = new Intent(this,MainActivity.class);
        startActivity(returnToMain);
    }
}
