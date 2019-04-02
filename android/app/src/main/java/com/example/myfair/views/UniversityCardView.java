package com.example.myfair.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class UniversityCardView extends GenericCardView {
    private TextView name;
    private TextView university;
    private TextView major;

    public UniversityCardView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public UniversityCardView(@NonNull Context context, String cID, HashMap<String,Object> map){
        super(context);
        initialize(context);
        setFromMap(cID, map);
    }

    public UniversityCardView(@NonNull Context context, Card card){
        super(context);
        initialize(context);
        setFromCardModel(card);
    }

    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_university_card, this);

        name = findViewById(R.id.tvNameU);
        university = findViewById(R.id.tvUniversity);
        major = findViewById(R.id.tvMajor);
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
        String type = getValue(Card.FIELD_TYPE);
        if(type.equals(Card.VALUE_TYPE_BUSINESS))
            setBusinessCard();
        else
            setUniversityCard();
        setQrString();
    }

    private void setBusinessCard(){
        setName();
        setCompany();
        setPosition();
    }
    private void setUniversityCard(){
        setName();
        setUniversity();
        setMajor();
    }

    public void setName() {
        this.name.setText(getValue(Card.FIELD_NAME));
    }
    public void setUniversity() {
        this.university.setText(getValue(Card.FIELD_UNIVERSITY_NAME));
    }
    public void setMajor() {
        this.major.setText(getValue(Card.FIELD_UNIVERSITY_MAJOR));
    }
    public void setCompany() { this.university.setText(getValue(Card.FIELD_COMPANY_NAME)); }
    public void setPosition() { this.major.setText(getValue(Card.FIELD_COMPANY_POSITION)); }
    public String getName() {
        return name.getText().toString();
    }
    public String getUniversity() {
        return university.getText().toString();
    }
    public String getMajor() {
        return major.getText().toString();
    }
}
