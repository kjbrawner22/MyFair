package com.example.myfair.db;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class UserTest {

    private User user;
    private HashMap<String,Object> map;

    @Before
    public void setUser(){
        user = new User();
        map = new HashMap<>();

        map.put(User.FIELD_NAME, "Joshua Helms");
        map.put(User.FIELD_PROFILE_CREATED, User.VALUE_TRUE);
    }

    @Test
    public void profileCreated() {
        assertTrue(user.profileCreated());

        user.setValue(User.FIELD_PROFILE_CREATED, User.VALUE_FALSE);
        assertFalse(user.profileCreated());
    }

    @Test
    public void isPrivateField() {
        assertTrue(User.isPrivateField(User.FIELD_NAME));
        assertFalse(User.isPrivateField(User.FIELD_PROFILE_CREATED));
    }
}