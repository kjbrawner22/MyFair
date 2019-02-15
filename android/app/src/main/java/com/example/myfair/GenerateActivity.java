package com.example.myfair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class GenerateActivity extends AppCompatActivity {

    String TAG = "QRgen";
    EditText qrInput;
    ImageView qrImage;
    String input;
    Button start, save;
    String savePath = Environment.getExternalStorageDirectory().getPath();
    Bitmap bm;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        qrImage = findViewById(R.id.qrImage);
        qrInput = findViewById(R.id.qrInput);
        start = findViewById(R.id.btnGen);
        save = findViewById(R.id.btnSave);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                input = qrInput.getText().toString().trim();
                if(input.length() > 0){
                    WindowManager mngr = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display disp = mngr.getDefaultDisplay();
                    Point point = new Point();
                    disp.getSize(point);
                    int w = point.x;
                    int h = point.y;
                    int smaller = w < h ? w : h;
                    smaller = smaller * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            input, null, QRGContents.Type.TEXT, smaller);
                    try {
                        bm = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bm);
                    } catch (WriterException e){
                        Log.v(TAG,e.toString());
                    }
                }
                else{
                    qrInput.setError("Required");
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean save;
                String res;
                try{
                    save = QRGSaver.save(savePath, qrInput.getText().toString().trim(), bm, QRGContents.ImageType.IMAGE_JPEG);
                    res = save ? "Image Saved" : "Image Not Saved";
                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void goHome(View view){
        Intent intent = new Intent(GenerateActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
}
