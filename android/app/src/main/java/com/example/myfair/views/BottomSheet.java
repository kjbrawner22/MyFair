package com.example.myfair.views;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.QRCodeHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import androidx.annotation.Nullable;

public class BottomSheet extends BottomSheetDialogFragment {
    private ImageView qrCode;
    //private TextView qrEncrypted;
    private String qrString;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        Bundle bundle = this.getArguments();

        qrString = bundle.getString("encryptedString");
        qrCode = v.findViewById(R.id.bottomSheetQRCode);
        //qrEncrypted = v.findViewById(R.id.tvEncrypted);
        //qrEncrypted.setText(qrString);
        int color = Color.parseColor("#000000");
        qrCode.setColorFilter( color, PorterDuff.Mode.MULTIPLY);

        Log.d("EncryptedString", "Value's value:)  ::: " + bundle);
        Log.d("EncryptedString", "Bottom Sheet: " + qrString);
        Log.d("BottomSheetLog", "qrCode id: " + qrCode);

        setQrCode();
        return v;
    }

    private void setQrCode(){
        Bitmap bitmap = QRCodeHelper.newInstance(getContext()).setContent(qrString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        qrCode.setImageBitmap(bitmap);
    }
}
