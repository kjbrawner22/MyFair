package com.example.myfair.views;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Base Class for UniversityCardView
 */
public class GenericCardView extends CardView {
    private String cID, uID;
    private String encryptedString;
    private HashMap<String,Object> map;

    /**
     * Standard Constructor
     * @param context - Context of the view
     */
    public GenericCardView(@NonNull Context context) {
        super(context);
    }

    /**
     * Helper method that sets the margins for the view
     */
    public void setMargins() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        params.setMargins(margin, 8,margin, margin);
        setLayoutParams(params);
        Log.d("MARGINS", "" + margin);
    }

    /**
     * Helper method that initializes the encrypted string variable used for the QR bitmap
     */
    public void setQrString(){
        qrObject user = new qrObject(uID, cID);
        String serializeString = new Gson().toJson(user);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        Log.d("SetQrCode", "Card: " + encryptedString);
    }

    /**
     * Setter for the map variable that holds the underlying card data
     * @param map - HashMap that holds card data
     */
    public void setMap(HashMap<String,Object> map){
        this.map = map;
    }
    public HashMap<String, Object> getMap() { return map; };

    public String getValue(String key){
        String str = (String) map.get(key);
        if(str == null) return "empty";
        return str;
    }


    /**
     * Getters for cID and uID
     * @return String
     */
    public String getCardID(){return cID;}
    public String getUserID(){return uID;}


    /**
     * Setters for cID and uID
     * @param id - String representing ID value
     */
    public void setCardID(String id){cID = id;}
    public void setUserID(String id){uID = id;}

    /**
     * Getter for QR string
     * @return String
     */
    public String getEncryptedString() { return encryptedString; }
}
