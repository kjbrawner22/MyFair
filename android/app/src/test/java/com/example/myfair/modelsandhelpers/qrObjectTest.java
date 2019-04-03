package com.example.myfair.modelsandhelpers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class qrObjectTest {

    private qrObject qrObject;

    @Before
    public void setUp(){
        qrObject = new qrObject("12345", "23456");
    }

    @Test
    public void getUserID() {
        assertEquals(qrObject.getUserID(),"12345");
    }

    @Test
    public void getCardID() {
        assertEquals(qrObject.getCardID(), "23456");
    }
}