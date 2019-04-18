package com.example.myfair.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myfair.R;
import com.example.myfair.db.Card;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
/**
 * Custom card view that sets the university card view layout
 */
public class UniversityCardView extends GenericCardView {
    private TextView name;
    private TextView university;
    private TextView major;
    private ImageView ivBanner, ivProfile;

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

    public UniversityCardView(Context context, String cID, HashMap<String, Object> map, ViewGroup parent) {
        super(context);
        initialize(context);
        parent.addView(this);
        setMargins();
        if (cID != null && map != null) setFromMap(cID, map);
        renderImages(context);
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
        ivBanner = findViewById(R.id.ivBanner);
        ivProfile = findViewById(R.id.ivProfile);
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
        if(type.equals(Card.VALUE_TYPE_BUSINESS)) {
            setBusinessCard();
        } else {
            setUniversityCard();
        }

        setQrString();
    }

    private ImageLoadingListener getImageLoadingListener(String cardField) {
        return new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (cardField.equals(Card.FIELD_BANNER_URI)) {
                    ivBanner.setImageBitmap(loadedImage);
                } else {
                    ivProfile.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        };
    }

    private void renderImages(Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        String bannerUri = getValue(Card.FIELD_BANNER_URI);
        if (!bannerUri.equals(Card.VALUE_DEFAULT_IMAGE)) {
            imageLoader.displayImage(bannerUri, (ImageView) findViewById(R.id.ivBanner));
        }

        String profileUri = getValue(Card.FIELD_PROFILE_URI);
        if (!profileUri.equals(Card.VALUE_DEFAULT_IMAGE)) {
            imageLoader.displayImage(profileUri,(ImageView) findViewById(R.id.ivProfile));
        }
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
        return ivBanner;
    }

    public ImageView getProfileView() {
        return ivProfile;
    }
}
