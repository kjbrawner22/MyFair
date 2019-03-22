package com.example.myfair.views;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.example.myfair.modelsandhelpers.EncryptionHelper;
import com.example.myfair.modelsandhelpers.qrObject;
import com.google.gson.Gson;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class BusinessCardView extends CardView {
    private String cID, uID;
    private String encryptedString;
    private TextView name;
    private TextView company;
    private TextView position;
    private HashMap<String,Object> map;

    public BusinessCardView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, String cID, HashMap<String,Object> map){
        super(context);
        initialize(context);
        setFromMap(cID, map);
    }

    public BusinessCardView(@NonNull Context context, Card card){
        super(context);
        initialize(context);
        setFromCardModel(card);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_business_card, this);

        name = findViewById(R.id.tvName);
        company = findViewById(R.id.tvCompany);
        position = findViewById(R.id.tvPosition);

    }

    public void setMargins() {
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        params.setMargins(margin, 0,margin, margin);
        setLayoutParams(params);
        Log.d("MARGINS", "" + margin);
    }

    public void setFromCardModel(Card card) {
        setFromMap(card.getCardID(), card.getMap());
    }

    private void setQrString(){
        qrObject user = new qrObject(uID, cID);
        String serializeString = new Gson().toJson(user);
        encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
        Log.d("SetQrCode", "CardInfoView: " + encryptedString);
    }

    /*
            implementing generic methods
     */
    public void setFromMap(String cID, HashMap<String, Object> map){
        this.map = map;
        setUserID(getValue(Card.FIELD_CARD_OWNER));
        setUserID(getValue(Card.FIELD_CARD_OWNER));
        setCardID(cID);
        setBusinessCard();
        setQrString();
    }

    private void setBusinessCard(){
        setName();
        setCompany();
        setPosition();
    }

    public String getValue(String key){
        return (String) map.get(key);
    }


    public void setName() {
        this.name.setText(getValue(Card.FIELD_NAME));
    }
    public void setCompany() {
        this.company.setText(getValue(Card.FIELD_COMPANY_NAME));
    }
    public void setPosition() {
        this.position.setText(getValue(Card.FIELD_COMPANY_POSITION));
    }
    public String getName() {
        return name.getText().toString();
    }
    public String getCompany() {
        return company.getText().toString();
    }
    public String getPosition() {
        return position.getText().toString();
    }

    public String getCardID(){return cID;}
    public void setCardID(String id){cID = id;}
    public String getUserID(){return uID;}
    public void setUserID(String id){uID = id;}

    public String getEncryptedString() { return encryptedString; }
}
