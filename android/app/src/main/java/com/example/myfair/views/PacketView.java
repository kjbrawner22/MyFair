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

    public PacketView(Context context, String pID, ViewGroup parent, HashMap<String, Object> map) {
        super(context);
        setPacketId(pID);
        this.map = map;
        initialize(context);
        parent.addView(this);
    }

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

    public void setPacketId(String pID){
        this.pID = pID;
    }

    public String getPacketId(){
        return pID;
    }

    public HashMap<String, Object> getMap(){
        return this.map;
    }
}
