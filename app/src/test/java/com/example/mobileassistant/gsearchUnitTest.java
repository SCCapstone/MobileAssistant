package com.example.mobileassistant;

import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class gsearchUnitTest {

    @Test
    public void testSearchKeyword() {
        String searchQuery = "search stuff";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertNotNull(response);
    }

    @Test
    public void testGoogleKeyword() {
        String searchQuery = "Google stuff";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertNotNull(response);
    }

    @Test
    public void testLookUpKeyword() {
        String searchQuery = "look up stuff";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertNotNull(response);
    }

    @Test
    public void testSearchBlankKeyword() {
        String searchQuery = "search";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertNotNull(response);
    }

    @Test
    public void testGoogleBlankKeyword() {
        String searchQuery = "Google";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertNotNull(response);
    }

    @Test
    public void testLookUpBlankKeyword() {
        String searchQuery = "look up";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertNotNull(response);
    }

    @Test
    public void testNewsResult() {
        String searchQuery = "search covid 19 news";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertTrue(response.contains("https://"));
        assertTrue(response.contains("covid"));
    }

    @Test
    public void testSearchResult() {
        String searchQuery = "search bitcion price";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        assertTrue(response.contains("https://"));
        assertTrue(response.contains("bitcoin"));
    }

    @Test
    public void test2Results() {
        String searchQuery = "search University of South Carolina top 2 results";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        int linkCount = 0;

        for (int i=1; i<response.length(); ++i) {
            if (response.charAt(i-1) == ' ' && response.charAt(i) == '-') {
                ++linkCount;
            }
        }

        assertEquals(linkCount, 2);
    }

    @Test
    public void test10Results() {
        String searchQuery = "search University of South Carolina top 10 results";
        String response = "";

        gsearch g = new gsearch();
        response = g.doInBackground(searchQuery);

        int linkCount = 0;

        for (int i=1; i<response.length(); ++i) {
            if (response.charAt(i-1) == ' ' && response.charAt(i) == '-') {
                ++linkCount;
            }
        }

        assertEquals(linkCount, 10);
    }
}