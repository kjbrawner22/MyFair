package com.example.myfair;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView qrCodeScanner = findViewById(R.id.qrCodeScanner);
    ImageView barcodeBackImageView = findViewById(R.id.barcodeBackImageView);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);
        scannerInit();

        


        Button goHome = findViewById(R.id.scanbtnHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
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
            Toast.makeText(this,p0.getText(),Toast.LENGTH_LONG).show();
        }
    }


}
