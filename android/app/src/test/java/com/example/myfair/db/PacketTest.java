package com.example.myfair.db;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PacketTest {

    private Packet packet;

    @Before
    public void setup(){
        HashMap<String, Object> cards = new HashMap<>();
        HashMap<String, Object> documents = new HashMap<>();

        HashMap<String, Object> map = new HashMap<>();
        map.put(Packet.FIELD_PACKET_NAME, "Name");
        map.put(Packet.FIELD_PACKET_OWNER, "1234");
        map.put(Packet.FIELD_CARD_LIST, cards);
        map.put(Packet.FIELD_DOCUMENT_LIST, documents);

        packet = new Packet(map);
    }

    @Test
    public void addCard() {
        final String cID = "1234";
        HashMap<String, Object> testCard = new HashMap<>();
        testCard.put(Card.FIELD_CARD_OWNER, cID);
        packet.addCard(cID, testCard);
        assertTrue(packet.containsCard(cID));

        HashMap<String,Object> cardMap = packet.getCardMap();
        assertTrue(cardMap.containsKey(cID));
        assertEquals(cardMap.get(cID), testCard);
    }

    @Test
    public void addDocument() {
        final String name = "testName";
        final String URL = "https://documentContents.com";
        packet.addDocument(name, URL);
        assertTrue(packet.containsDocument(name));

        HashMap<String,Object> documentMap = packet.getDocumentMap();
        assertTrue(documentMap.containsKey(name));
        assertEquals(documentMap.get(name), URL);
    }

    @Test
    public void containsCard() {
        final String cID = "1234";
        HashMap<String, Object> testCard = new HashMap<>();
        testCard.put(Card.FIELD_CARD_OWNER, cID);
        packet.addCard(cID, testCard);
        packet.addCard("12345", testCard);

        assertTrue(packet.containsCard(cID));
        assertTrue(packet.containsCard("12345"));
        assertFalse(packet.containsCard("apple"));
    }

    @Test
    public void containsDocument() {
        final String name = "testName";
        final String URL = "https://documentContents.com";
        packet.addDocument(name, URL);
        packet.addDocument("dorito", URL);
        assertTrue(packet.containsDocument(name));
        assertTrue(packet.containsDocument("dorito"));
        assertFalse(packet.containsDocument("apple"));
    }
}