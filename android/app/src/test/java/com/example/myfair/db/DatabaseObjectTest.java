package com.example.myfair.db;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DatabaseObjectTest {

    HashMap<String, Object> map;
    DatabaseObject object;

    @Before
    public void dbExpectedVals(){
        object = new DatabaseObject();
        map = new HashMap<>();
        map.put(Card.FIELD_CARD_OWNER, "4j6fK7UvU7MLDXwqHoFCtWw97Qy2");
        map.put(Card.FIELD_TYPE, "business_card");
        map.put(Card.FIELD_COMPANY_NAME, "Northrop Grumman");
        map.put(Card.FIELD_COMPANY_POSITION, "Engineer");
        map.put(Card.FIELD_NAME, "Josh G. Helms");
        object.setId("1234");
        object.setMap(map);
    }

    @Test
    public void getMap() {
        assertEquals(object.getMap(), map);
    }

    @Test
    public void setMap() {
        Card newCard = new Card();
        newCard.setMap(map);
        assertEquals(map, newCard.getMap());
    }

    @Test
    public void setValue() {
        String str = "Lockheed Martin", str2 = "";

        object.setValue(Card.FIELD_COMPANY_NAME, str);
        assertEquals(str, object.getValue("company_name"));

        object.setValue(Card.FIELD_TYPE, Card.VALUE_TYPE_UNIVERSITY);
        assertEquals(Card.VALUE_TYPE_UNIVERSITY, object.getValue(Card.FIELD_TYPE));

        assertTrue(object.setValue(Card.FIELD_COMPANY_POSITION, str2));
    }

    @Test
    public void getValue() {
        assertEquals("Northrop Grumman", object.getValue(Card.FIELD_COMPANY_NAME));
        assertEquals("Engineer", object.getValue(Card.FIELD_COMPANY_POSITION));

        assertTrue(object.getValue("apple").isEmpty());
        assertFalse(object.getValue(Card.FIELD_COMPANY_POSITION).isEmpty());
    }

    @Test
    public void containsKey() {
        assertTrue(object.containsKey(Card.FIELD_COMPANY_POSITION));
        assertTrue(object.containsKey(Card.FIELD_TYPE));
        assertTrue(object.containsKey(Card.FIELD_COMPANY_NAME));
        assertTrue(object.containsKey(Card.FIELD_NAME));
        assertTrue(object.containsKey(Card.FIELD_CARD_OWNER));
        assertFalse(object.containsKey("apple"));
    }

    @Test
    public void getId(){
        assertEquals("1234", object.getId());
    }

    @Test
    public void setId(){
        object.setId("12345");
        assertEquals("12345", object.getId());
    }

    @Test
    public void display(){
        String expected = "{name=Josh G. Helms, company_position=Engineer, card_type=business_card, card_owner=4j6fK7UvU7MLDXwqHoFCtWw97Qy2, company_name=Northrop Grumman}";
        String displayStr = object.display();
        assertEquals(expected,displayStr);
    }


}