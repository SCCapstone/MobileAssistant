package com.example.mobileassistant;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

public class ChatMessageUnitTest extends TestCase {

    @Test
    public void testGetContent() {
        ChatMessage cMsg = new ChatMessage("abc", true);
        String content = cMsg.getContent();
        assertEquals("abc", content);
    }

    @Test
    public void testSetContent() {
        ChatMessage cMsg = new ChatMessage("def", true);
        cMsg.setContent("ghi");
        assertEquals("ghi", cMsg.getContent());
    }

    @Test
    public void testIsUser() {
        ChatMessage cMsg = new ChatMessage("abc", true);
        boolean isUser = cMsg.isUser();
        assertTrue(isUser);
    }

    @Test
    public void testIsBot() {
        ChatMessage cMsg = new ChatMessage("abc", false);
        boolean isBot = cMsg.isUser();
        assertFalse(isBot);
    }

    @Test
    public void testSetUser() {
        ChatMessage cMsg = new ChatMessage("abc", true);
        cMsg.setIsUser(false);
        boolean isBot = cMsg.isUser();
        assertFalse(isBot);
    }
}