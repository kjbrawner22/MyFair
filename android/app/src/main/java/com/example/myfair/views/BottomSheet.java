package com.example.myfair.views;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.QRCodeHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * Bottom sheet class for initializing Bottom Sheets (QR codes/sharing cards)
 */
public class BottomSheet extends BottomSheetDialogFragment {
    private ImageView qrCode;
    //private TextView qrEncrypted;
    private String qrString;

    /**
     * Sets variables and setup for inflate layout
     * @return returns the inflated view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        Bundle bundle = this.getArguments();

        qrString = bundle.getString("encryptedString");
        qrCode = v.findViewById(R.id.bottomSheetQRCode);
        //qrEncrypted = v.findViewById(R.id.tvEncrypted);
        //qrEncrypted.setText(qrString);

        Log.d("EncryptedString", "Value's value:)  ::: " + bundle);
        Log.d("EncryptedString", "Bottom Sheet: " + qrString);
        Log.d("BottomSheetLog", "qrCode id: " + qrCode);



        Button btnPrint = v.findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeHelper.printQRCode(getContext(), ((BitmapDrawable)qrCode.getDrawable()).getBitmap());
            }
        });

        setQrCode();
        return v;
    }

    /**
     * Method for setting the QR code displayed on the bottom sheet
     * uses QR string (set in onCreateView)
     */
    private void setQrCode(){
        Bitmap bitmap = QRCodeHelper.newInstance(getContext()).setContent(qrString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        qrCode.setImageBitmap(bitmap);
    }
}
