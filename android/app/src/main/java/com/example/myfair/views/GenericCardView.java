package com.example.myfair.views;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.google.gson.Gson;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class GenericCardView extends CardView {
    private String cID, uID;
    private String encryptedString;
    private HashMap<String,Object> map;

    public GenericCardView(@NonNull Context context) {
        super(context);
    }

    public void setMargins() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        params.setMargins(margin, 0,margin, margin);
        setLayoutParams(params);
        Log.d("MARGINS", "" + margin);
    }

    public void setQrString(){
        qrObject user = new qrObject(uID, cID);
        String serializeString = new Gson().toJson(user);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        Log.d("SetQrCode", "CardInfoView: " + encryptedString);
    }

    public void setMap(HashMap<String,Object> map){
        this.map = map;
    }

    public String getValue(String key){
        String str = (String) map.get(key);
        if(str == null) return "empty";
        return str;
    }

    public String getCardID(){return cID;}
    public void setCardID(String id){cID = id;}
    public String getUserID(){return uID;}
    public void setUserID(String id){uID = id;}

    public String getEncryptedString() { return encryptedString; }
}
