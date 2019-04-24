package com.example.myfair.db;

import java.util.HashMap;
import java.util.Map;

public class Packet extends DatabaseObject {
    public static final String FIELD_PACKET_NAME = "packet_name";
    public static final String FIELD_CARD_LIST = "card_list";
    public static final String FIELD_DOCUMENT_LIST = "document_list";

    public Packet() {
        super();
        HashMap<String, Object> cards = new HashMap<>();
        HashMap<String, Object> documents = new HashMap<>();

        super.setValue(Packet.FIELD_CARD_LIST, cards);
        super.setValue(Packet.FIELD_DOCUMENT_LIST, documents);
    }

    public Packet(Map<String, Object> newMap) {
        super(newMap);
        HashMap<String, Object> cards = getValueHashMap(Packet.FIELD_CARD_LIST);
        HashMap<String, Object> documents = getValueHashMap(Packet.FIELD_DOCUMENT_LIST);

        super.setValue(Packet.FIELD_CARD_LIST, cards);
        super.setValue(Packet.FIELD_DOCUMENT_LIST, documents);
    }

    public void addCard(String cID, HashMap<String, Object> map){
        HashMap<String, Object> cards = super.getValueHashMap(Packet.FIELD_CARD_LIST);
        cards.put(cID, map);
    }

    public void addDocument(String name, String link){
        HashMap<String, Object> documents = super.getValueHashMap(Packet.FIELD_DOCUMENT_LIST);
        documents.put(name, link);
    }

    public boolean containsCard(String cID){
        HashMap<String, Object> cards = super.getValueHashMap(Packet.FIELD_CARD_LIST);
        for (String key : cards.keySet()) {
            if(key.equals(cID)){
                return true;
            }
        }
        return false;
    }

    public boolean containsDocument(String documentName){
        HashMap<String, Object> documents = super.getValueHashMap(Packet.FIELD_DOCUMENT_LIST);
        for (String key : documents.keySet()) {
            if(key.equals(documentName)){
                return true;
            }
        }
        return false;
    }
}
