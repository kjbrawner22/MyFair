package com.example.myfair.db;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CardTest {

    private HashMap<String, Object> map;
    private Card card;

    @Before
    public void dbExpectedVals(){
        card = new Card();
        map = new HashMap<>();
        map.put("card_owner", "4j6fK7UvU7MLDXwqHoFCtWw97Qy2");
        map.put("card_type", "business_card");
        map.put("company_name", "Northrop Grumman");
        map.put("company_position", "Engineer");
        map.put("name", "Josh G. Helms");
        card.setCardID("1234");
        card.setMap(map);
    }

    @Test
    public void getCardID() {
        assertEquals("1234", card.getCardID());
    }

    @Test
    public void setCardID() {
        card.setCardID("12345");
        assertEquals("12345", card.getCardID());
    }
}