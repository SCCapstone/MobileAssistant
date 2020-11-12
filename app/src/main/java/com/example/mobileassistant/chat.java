package com.example.mobileassistant;

import java.io.File;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

public class chat {
    private static final boolean TRACE_MODE = false;
    static String botname="mybot";

    public static void main(String[] args) {
        Bot bot = new Bot(botname);
        Chat chatSession = new Chat(bot);
        String request = "Hello. Are you alive? What is your name?";
        String response = chatSession.multisentenceRespond(request);
        System.out.println(response);
    }
}
