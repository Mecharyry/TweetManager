package com.github.mecharyry.tweetlist.request;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonArrayRequestTest {

    public static final int FIRST_INDEX = 0;
    public static final int SECOND_INDEX = 1;
    public static final int EXPECTED_LENGTH = 2;

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String RYAN = "Ryan";
    private static final String FELINE = "Feline";
    private static final String RACHEL = "Rachel";
    private static final String WORLEDGE = "Worledge";

    private static final String BASIC_JSON_ARRAY_INVALID = "Nonsense";
    private static final String BASIC_JSON_ARRAY_VALID = "[{" + FIRST_NAME + ":\"" + RYAN + "\", " + LAST_NAME + ":\"" + FELINE + "\"}," +
            "{" + FIRST_NAME + ":\"" + RACHEL + "\", " + LAST_NAME + ":\"" + WORLEDGE + "\"}]";

    private JsonArrayRequest jsonArrayRequest;

    @Before
    public void setUp() throws Exception {
        jsonArrayRequest = new JsonArrayRequest();
    }

    @Test
    public void createEmployeesReturnsExpected() throws Exception {
        JSONArray result = jsonArrayRequest.convertStreamTo(createInputStream(BASIC_JSON_ARRAY_VALID));

        assertNotNull(result);
        assertEquals(EXPECTED_LENGTH, result.length());

        JSONObject resultEmployeeOne = result.getJSONObject(FIRST_INDEX);
        JSONObject resultEmployeeTwo = result.getJSONObject(SECOND_INDEX);

        assertEquals(RYAN, resultEmployeeOne.getString(FIRST_NAME));
        assertEquals(FELINE, resultEmployeeOne.getString(LAST_NAME));

        assertEquals(RACHEL, resultEmployeeTwo.getString(FIRST_NAME));
        assertEquals(WORLEDGE, resultEmployeeTwo.getString(LAST_NAME));
    }

    @Test(expected = RequestException.class)
    public void createEmployeesThrowsException() throws Exception {
        jsonArrayRequest.convertStreamTo(createInputStream(BASIC_JSON_ARRAY_INVALID));
    }

    private InputStream createInputStream(String jsonArrayAsString) {
        return new ByteArrayInputStream(jsonArrayAsString.getBytes());
    }
}