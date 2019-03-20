package com.example.myfair.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myfair.modelsandhelpers.QRCodeHelper;

import com.example.myfair.R;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CardInfoView extends ConstraintLayout {
    private TextView name;
    private TextView company;
    private TextView position;
    private ImageView qrCode;

    public CardInfoView(Context context) {
        super(context);
        initialize(context);
    }

    public CardInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CardInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_card_info, this);

        name = findViewById(R.id.tvInfoName);
        company = findViewById(R.id.tvInfoCompany);
        position = findViewById(R.id.tvInfoPosition);
        qrCode = findViewById(R.id.ImageQRCode);

        Log.d("CardInfoView", "Initialization called.");
        Log.d("CardInfoView", "name field: " + name);
    }

    public void setFromBusinessCardView(BusinessCardView cardView, Context context){
        Log.d("SetName", cardView.getStrName());
        setName(cardView.getStrName());
        setCompany(cardView.getStr2());
        setPosition(cardView.getStr3());
        setQR(cardView.getEncryptedString(), context);
    }

    private void setQR(String encrypted, Context context){
        Bitmap bitmap = QRCodeHelper.newInstance(context).setContent(encrypted).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        qrCode.setImageBitmap(bitmap);
        Log.d("SetQRCode", encrypted);
    }
    public void setName(String name) {
        this.name.setText(name);
    }
    public void setCompany(String name) {
        this.company.setText(name);
    }
    public void setPosition(String name) { this.position.setText(name); }
}
