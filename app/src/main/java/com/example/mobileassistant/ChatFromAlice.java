package com.example.mobileassistant;

import java.io.File;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

public class ChatFromAlice {
    private final boolean TRACE_MODE = false;
    static String botName="my bot";

    public String getChatFromAlice() {
        Bot bot = new Bot(botName);
        org.alicebot.ab.Chat chatSession = new org.alicebot.ab.Chat(bot);
        String request = "Hello. Are you alive? What is your name?";
        String response = chatSession.multisentenceRespond(request);
        //System.out.println(response);
        return response;
    }
}
