package com.example.myfair.qrcode;

/**
 * Basic object for serialization of user and card id to be encrypted in a QR code
 */

public class qrObject {
    private String userID;
    private String cardID;

    public qrObject(String uID, String cID){
        this.userID = uID;
        this.cardID = cID;
    }

    public String getUserID() {
        return userID;
    }

    public String getCardID() {
        return cardID;
    }
}
