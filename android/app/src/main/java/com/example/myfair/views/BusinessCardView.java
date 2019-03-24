package com.example.myfair.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class BusinessCardView extends GenericCardView {
    private TextView name;
    private TextView company;
    private TextView position;
    private TextView university;
    private TextView major;

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

    public void setFromCardModel(Card card) {
        setFromMap(card.getCardID(), card.getMap());
    }

    /*
            implementing generic methods
     */
    public void setFromMap(String cID, HashMap<String, Object> map){
        setMap(map);
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
}
