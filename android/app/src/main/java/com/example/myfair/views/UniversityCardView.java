package com.example.myfair.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;

import java.util.HashMap;

import androidx.annotation.NonNull;
/**
 * Custom card view that sets the university card view layout
 */
public class UniversityCardView extends GenericCardView {
    private TextView name;
    private TextView university;
    private TextView major;

    /**
     * Default constructor
     * @param context - Context for the view
     */
    public UniversityCardView(@NonNull Context context) {
        super(context);
        initialize(context);
    }

    /**
     * Custom constructor that takes a HashMap for initialization
     * @param context - Context for the view
     * @param cID - String representing the Card ID of the card
     * @param map - HashMap that represents the underlying card Data
     */
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

    /**
     * Helper function that allows initialization of the card view
     * @param context - Context variable that represents the context for the card
     */
    private void initialize(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_university_card, this);

        name = findViewById(R.id.tvNameU);
        university = findViewById(R.id.tvUniversity);
        major = findViewById(R.id.tvMajor);
    }

    /**
     * Helper function that allows the card view to be set from a card model
     * @param card - Card variable that represents the card model for the card view
     */
    public void setFromCardModel(Card card) {
        setFromMap(card.getCardID(), card.getMap());
    }

    /**
     * Helper function that allows the card view to be initialized from a HashMap
     * @param cID - String that represents the cID of the card
     * @param map - HashMap that represents the contents of the card
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

    /**
     * Helper function for setting the Edit Text Values
     */
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

    /**
     * Helper functions for setting the Edit Text values
     */
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

    /**
     * Helper functions for getting the Edit Text Values
     * @return Returns a String that represents the contents of the Edit Text field
     */
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
