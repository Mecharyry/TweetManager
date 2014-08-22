package com.github.mecharyry.tweetlist.request;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;

public class JsonObjectRequestTest {

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String RYAN = "Ryan";
    private static final String FELINE = "Feline";
    private static final String BASIC_JSON_OBJECT_VALID = "{" + FIRST_NAME + ":\"" + RYAN + "\", " + LAST_NAME + ":\"" + FELINE + "\"}";
    private static final String BASIC_JSON_OBJECT_INVALID = "Nonsense";
    private JsonObjectRequest jsonObjectRequest;

    @Before
    public void setUp() throws Exception {
        jsonObjectRequest = new JsonObjectRequest();
    }

    @Test
    public void createEmployeeReturnsExpected() throws Exception {
        JSONObject result = jsonObjectRequest.convertStreamTo(createInputStream(BASIC_JSON_OBJECT_VALID));

        assertNotNull(result);
        assertEquals(RYAN, result.getString(FIRST_NAME));
        assertEquals(FELINE, result.getString(LAST_NAME));
    }

    @Test(expected = RequestException.class)
    public void createEmployeeThrowsException() throws Exception {
        jsonObjectRequest.convertStreamTo(createInputStream(BASIC_JSON_OBJECT_INVALID));
    }

    private InputStream createInputStream(String jsonArrayAsString) {
        return new ByteArrayInputStream((jsonArrayAsString.getBytes()));
    }
}