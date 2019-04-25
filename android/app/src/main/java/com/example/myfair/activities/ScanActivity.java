package com.example.myfair.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.db.FirebaseDatabase;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.android.gms.common.util.CollectionUtils.listOf;



public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "Scan Activity";

    ZXingScannerView qrCodeScanner;
    //ImageView barcodeBackImageView;
    ImageView flashOnOffImageView;
    private CardView cvProgressBar;

    int MY_CAMERA_REQUEST_CODE;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);

        database = new FirebaseDatabase();

        cvProgressBar = findViewById(R.id.cvProgressBar);
        cvProgressBar.setVisibility(View.GONE);

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

    /**
     * Initializes the scanner
     */
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
            String decryptedString = EncryptionHelper.getInstance().getDecryptionString(p0.getText());
            qrObject qr = new Gson().fromJson(decryptedString, qrObject.class);
            String cID = qr.getCardID();
            String uID = qr.getUserID();
            String type = qr.getType();

            //Run the request to get the info from firebase
            if(type.equals(qrObject.VALUE_TYPE_CARD)){
                dbCardCall(uID, cID);
            }
            else{
                dbPacketCall(uID, cID);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == MY_CAMERA_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
        }
    }

    /**
     * Return the user to the main menu on pressing the back button
     */
    @Override
    public void onBackPressed(){
        finish();
    }

    /**
     * Database call that sends card info into the card collection
     * @param uID - uID of the appropriate user
     * @param cID - cID of the card
     */
    public void dbCardCall(String uID, String cID){
        cvProgressBar.setVisibility(View.VISIBLE);
        Log.d("Scanned", "Checking Ids "+uID+" "+cID);
        final Card sharedCard = new Card();
        DocumentReference ref = sharedCard.setFromDb(uID,cID);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Scanned", "Listen failed.");
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Scanned", "User snapshot updated");
                    Map<String,Object> temp = snapshot.getData();
                    temp.put("scan_date", new Timestamp(Calendar.getInstance().getTime()));
                    Log.d("Scanned","Object info" + sharedCard.getMap());

                    //push card to collections folder
                    Bundle bundle = new Bundle();
                    HashMap<String, Object> map = new HashMap<>();
                    final String metaPushed = "meta_pushed";
                    final String mainCardPushed = "main_card_pushed";

                    DocumentReference docRef = database.userContacts().document(sharedCard.getCardID());

                    docRef.set(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "DocumentSnapshot card added to collection!");
                                map.put(mainCardPushed, 1);
                                bundle.putSerializable(CardInfoActivity.INTENT_CARD_MAP, (HashMap) temp);
                                bundle.putString(CardInfoActivity.INTENT_CARD_ID, snapshot.getId());
                            } else {
                                Log.d(TAG, "Error adding card info document to collection");
                            }
                        }
                    });

                    DocumentReference metaRef = database.getCardRef(uID,cID).collection("cdata").document("metadata");
                    metaRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                Map<String,Object> metadata = task.getResult().getData();
                                ArrayList<Timestamp> timeStamp = (ArrayList<Timestamp>) metadata.get("scanRegistry");
                                long scans = (long) metadata.get("shared");
                                scans++;
                                timeStamp.add(new Timestamp(Calendar.getInstance().getTime()));

                                metaRef.update("scanRegistry", timeStamp, "shared", scans).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.e("pushing a moose", "We Updated");
                                            map.put(metaPushed, 1);
                                        }
                                        else{
                                            Log.e("pushing a moose", "oops something went wrong");
                                        }
                                    }
                                });
                            }
                        }
                    });

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!map.containsKey(metaPushed) || !map.containsKey(mainCardPushed)) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent intent = new Intent(ScanActivity.this, CardInfoActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    });
                    t.start();
                }
            }
        });
    }

    /**
     * Database call that sends packet info into the packet collection
     * @param uID - uID of the appropriate user
     * @param pID - pID of the packet
     */
    public void dbPacketCall(String uID, String pID){
        cvProgressBar.setVisibility(View.VISIBLE);
        DocumentReference packetRef = database.getPacketRef(uID, pID);
        packetRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Map<String, Object> data = task.getResult().getData();
                    Log.d(TAG, "Map: " + data);
                    String id = task.getResult().getId();
                    if (data != null)
                        database.packetsLibrary().document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "DocumentSnapshot packet added to collection!");
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(PacketInfoActivity.INTENT_PACKET_MAP, (HashMap) data);
                                    bundle.putString(PacketInfoActivity.INTENT_PACKET_ID, id);
                                    Intent intent = new Intent(ScanActivity.this, PacketInfoActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.d(TAG, "Error adding packet info document to collection");
                                }
                            }
                        });
                }
            }
        });
    }
}
