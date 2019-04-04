package com.example.myfair;

import com.example.myfair.R;
import com.example.myfair.modelsandhelpers.Connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

public class ConnectionTest {
    private Connection connection;

    @Before
    public void setUp() {
        connection = new Connection(R.drawable.ic_twitter,"twitter_username",  "Twitter");
    }

    @Test
    public void getIconId() {
        assertEquals(connection.getIconId(), R.drawable.ic_twitter);
    }

    @Test
    public void getDbKey() {
        assertEquals(connection.getDbKey(), "twitter_username");
    }

    @Test
    public void getName() {
        assertEquals(connection.getName(), "Twitter");
    }

}