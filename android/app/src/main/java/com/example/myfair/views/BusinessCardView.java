package com.example.myfair.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfair.R;
import com.example.myfair.activities.GenerateActivity;
import com.example.myfair.db.Card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class BusinessCardView extends CardView {
    private static final String TAG = "CardViewLog";
    private String cID, uID;

    private TextView name;
    private TextView company;
    private TextView position;
    private String strName, str2, str3;

    public BusinessCardView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BusinessCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
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
        setUserID(card.getValue(Card.FIELD_CARD_OWNER));
        setCardID(card.getCardID());
        strName = card.getValue(Card.FIELD_NAME);
        if(card.getValue(Card.FIELD_TYPE).equals(Card.VALUE_TYPE_BUSINESS)) {
            setName(strName);
            str2 = card.getValue(Card.FIELD_COMPANY_NAME);
            str3 = card.getValue(Card.FIELD_COMPANY_POSITION);
        }
        else{
            setName(strName);
            str2 = card.getValue(Card.FIELD_UNIVERSITY_NAME);
            str3 = card.getValue(Card.FIELD_UNIVERSITY_MAJOR);
        }
        setCompany(str2);
        setPosition(str3);
    }

    public String getName() { return this.name.toString(); }
    public void setName(String name) {
        this.name.setText(name);
    }
    public String getCompany() { return this.company.toString(); }
    public void setCompany(String company) {
        this.company.setText(company);
    }
    public String getPosition() { return this.position.toString(); }
    public void setPosition(String position) {
        this.position.setText(position);
    }
    public String getCardID(){return cID;}
    public void setCardID(String id){cID = id;}
    public String getUserID(){return uID;}
    public void setUserID(String id){uID = id;}

    public String getStrName() {return strName;}
    public String getStr2() {return str2;}

    public String getStr3() { return str3; }
}
