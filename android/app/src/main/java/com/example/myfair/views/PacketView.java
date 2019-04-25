package com.example.myfair.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myfair.R;
import com.example.myfair.db.Packet;

import java.util.HashMap;

public class PacketView extends LinearLayout {
    private HashMap<String, Object> map;
    private String pID;

    /**
     * Default constructor
     * @param context - desired context
     */
    public PacketView(Context context) {
        super(context);
        map = new HashMap<>();
        initialize(context);
    }

    public PacketView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        map = new HashMap<>();
        initialize(context);
    }

    public PacketView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        map = new HashMap<>();
        initialize(context);
    }

    public PacketView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        map = new HashMap<>();
        initialize(context);
    }

    /**
     * Custom constructor creates view using map
     * @param context - desired context variable
     * @param pID - String packet ID
     * @param parent - Layout variable parent ViewGroup
     * @param map - HashMap variable for packet contents
     */
    public PacketView(Context context, String pID, ViewGroup parent, HashMap<String, Object> map) {
        super(context);
        setPacketId(pID);
        this.map = map;
        initialize(context);
        parent.addView(this);
    }

    /**
     * helper function for initialization, inflates the view
     * @param context - desired context
     */
    public void initialize(Context context){
        setOrientation(VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_packet, this);

        HashMap<String, Object> cards = (HashMap<String, Object>) map.get(Packet.FIELD_CARD_LIST);
        HashMap<String, Object> documents = (HashMap<String, Object>) map.get(Packet.FIELD_DOCUMENT_LIST);

        int cardCount = cards.size();
        int documentCount = documents.size();
        String name = (String) map.get(Packet.FIELD_PACKET_NAME);

        TextView tvName = findViewById(R.id.tvPacketName);
        TextView tvCardCount = findViewById(R.id.tvCardCount);
        TextView tvDocumentCount = findViewById(R.id.tvDocumentCount);

        String str1 = "" + cardCount;
        String str2 = "" + documentCount;

        tvName.setText(name);
        tvCardCount.setText(str1);
        tvDocumentCount.setText(str2);
    }

    /**
     * Basic setter for packet ID
     * @param pID - String for new packet ID
     */
    public void setPacketId(String pID){
        this.pID = pID;
    }

    /**
     * Basic getter for packet ID
     * @return - returns String for current packet ID
     */
    public String getPacketId(){
        return pID;
    }

    /**
     * Basic getter for the map
     * @return - HashMap - returns the map
     */
    public HashMap<String, Object> getMap(){
        return this.map;
    }
}
