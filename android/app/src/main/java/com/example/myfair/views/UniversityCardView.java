package com.example.myfair.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;

import java.util.HashMap;

import androidx.annotation.NonNull;
/**
 * Custom card view that sets the university card view layout
 */
public class UniversityCardView extends GenericCardView {
    public TextView name;
    public TextView university;
    public TextView major;

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
        if (cID != null && map != null) setFromMap(cID, map);
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
        inflater.inflate(R.layout.view_card, this);

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
        this.name.setText(getValue(Card.FIELD_NAME));
        this.university.setText(getValue(Card.FIELD_COMPANY_NAME));
        this.major.setText(getValue(Card.FIELD_COMPANY_POSITION));
    }
    private void setUniversityCard(){
        this.name.setText(getValue(Card.FIELD_NAME));
        this.university.setText(getValue(Card.FIELD_UNIVERSITY_NAME));
        this.major.setText(getValue(Card.FIELD_UNIVERSITY_MAJOR));
    }

    /**
     * Helper functions for setting the Edit Text values
     */
    public void setName(String name) { this.name.setText(name); }
    public void setUniversity(String university) { this.university.setText(university); }
    public void setMajor(String major) { this.major.setText(major); }
    public void setCompany(String company) { this.university.setText(company); }
    public void setPosition(String position) { this.major.setText(position); }

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

    public TextView getNameView() {
        return name;
    }
    public TextView getUniversityView() {
        return university;
    }
    public TextView getMajorView() {
        return major;
    }

    public ImageView getBannerView() {
        return findViewById(R.id.ivBanner);
    }

    public ImageView getProfileView() {
        return findViewById(R.id.ivProfile);
    }
}
