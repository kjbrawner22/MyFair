package com.example.myfair.modelsandhelpers;

/**
 * Basic object for serialization of user and card id to be encrypted in a QR code
 */

public class qrObject {
    public static final String VALUE_TYPE_CARD = "card";
    public static final String VALUE_TYPE_PACKET = "packet";

    private String userID;
    private String cardID;
    private String type;

    public qrObject(String uID, String cID){
        this.userID = uID;
        this.cardID = cID;
        this.type = VALUE_TYPE_CARD;
    }

    public String getUserID() {
        return userID;
    }

    public String getCardID() {
        return cardID;
    }

    public String getType(){ return type; }

    public void setType(String type) {
        this.type = type;
    }
}
