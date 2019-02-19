package com.example.myfair.ModelsandHelpers;

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
