package com.example.mobileassistant;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class Home_screenUnitTest extends TestCase {
    private Home_screen mockHs;
    @Before
    public void setUp() {
        mockHs = mock(Home_screen.class);
    }
    @Test
    public void testCheckForAction() {
        doCallRealMethod().when(mockHs).checkForAction(anyString());
        // events
        assertEquals(1,mockHs.checkForAction("show me event"));
        assertEquals(1,mockHs.checkForAction("show me events"));
        assertEquals(2,mockHs.checkForAction("create event"));
        assertEquals(2,mockHs.checkForAction("create recurring events"));

        // google search
        assertEquals(3,mockHs.checkForAction("search panda"));
        assertEquals(3,mockHs.checkForAction("can you look up panda?"));
        assertEquals(3,mockHs.checkForAction("show me the news"));
        assertEquals(3,mockHs.checkForAction("What are the headlines?"));

        // weather
        assertEquals(4,mockHs.checkForAction("Show me the forecast"));
        assertEquals(4,mockHs.checkForAction("Show me the weather"));
        assertEquals(9,mockHs.checkForAction("How is the weather today?"));
        assertEquals(9,mockHs.checkForAction("What is the temperature now?"));
        assertEquals(9,mockHs.checkForAction("What's the wind speed right now"));
        assertEquals(9,mockHs.checkForAction("How's today's humidity?"));
        assertEquals(9,mockHs.checkForAction("What will be the highest temperature for tomorrow?"));

        // map
        assertEquals(5,mockHs.checkForAction("show me the directions to home"));
        assertEquals(5,mockHs.checkForAction("I'm heading home, show me the directions"));

        // traffic
        assertEquals(7,mockHs.checkForAction("How's today's traffic?"));
        assertEquals(7,mockHs.checkForAction("show traffic"));
        assertEquals(11,mockHs.checkForAction("show me the traffic to Theater"));
        assertEquals(11,mockHs.checkForAction("How's the traffic near home"));
        assertEquals(11,mockHs.checkForAction("show me the traffic in South Carolina"));

        // game
        assertEquals(8,mockHs.checkForAction("game 1"));
        assertEquals(8,mockHs.checkForAction("Let's play rock paper scissors"));

        // help
        assertEquals(10,mockHs.checkForAction("show me the tutorials"));
        assertEquals(10,mockHs.checkForAction("help"));
        assertEquals(10,mockHs.checkForAction("instructions to use this app"));
        assertEquals(10,mockHs.checkForAction("what can you do?"));
        assertEquals(10,mockHs.checkForAction("show me the commands that I can use"));
        assertEquals(10,mockHs.checkForAction("What functions do you have?"));

    }

    @Test
    public void testFindChatFlag() {
        doCallRealMethod().when(mockHs).findChatFlag(anyString(),anyInt());
        // search event
        mockHs.findChatFlag("Show me event",1);
        verify(mockHs, times(1)).searchEvents("Show me event");

        // create event
        mockHs.findChatFlag("create event \"event\"",2);
        verify(mockHs, times(1)).createEvents("create event \"event\"");

        // weather search
        mockHs.findChatFlag("How's the weather today?",4);
        verify(mockHs, times(1)).runWeatherSearch("How's the weather today?");

        // game
        mockHs.findChatFlag("game",8);
        verify(mockHs, times(1)).confirmGamesAction("game");

        // help
        mockHs.findChatFlag("show me the tutorials",10);
        verify(mockHs, times(1)).confirmHelpAction("show me the tutorials");
    }

    @Test
    public void testSearchEvents() {
        doCallRealMethod().when(mockHs).searchEvents(anyString());
        String events = "show me \"event\" event";
        String id = "event";
        mockHs.searchEvents(events);
        assertEquals(id,mockHs.id);

        events = "show me event";
        mockHs.searchEvents(events);
        assertEquals(1,mockHs.number);

        events = "show me upcoming events";
        mockHs.searchEvents(events);
        assertEquals(5,mockHs.number);

        events = "show me next 12 events";
        mockHs.searchEvents(events);
        assertEquals(12,mockHs.number);
    }

    @Test
    public void testCreateEvents() {
        doCallRealMethod().when(mockHs).createEvents(anyString());
        mockHs.createEventsAction = 0;
        mockHs.createEvents("create event \"event\"");
        verify(mockHs, times(1)).sendBotMessage("Do you want to add a location? yes/no");

        // yes, yes
        mockHs.createEvents("yes");
        verify(mockHs, times(1)).sendBotMessage("Please enter the location");
        mockHs.createEventsAction = 2;
        mockHs.createEvents("create event \"event\"");
        verify(mockHs, times(1)).sendBotMessage("Do you want to add attendees? yes/no");
        mockHs.createEvents("yes");
        mockHs.createEventsAction = 3;
        verify(mockHs, times(1)).sendBotMessage("Please enter list of attendees' emails, separate by comma");

        // reset
        Home_screen mockHs_1 = mock(Home_screen.class);
        doCallRealMethod().when(mockHs_1).createEvents(anyString());
        mockHs_1.createEventsAction = 1;
        // yes, no
        mockHs_1.createEvents("yes");
        verify(mockHs_1, times(1)).sendBotMessage("Please enter the location");
        mockHs_1.createEventsAction = 2;
        mockHs_1.createEvents("Columbia, SC");
        verify(mockHs_1, times(1)).sendBotMessage("Do you want to add attendees? yes/no");
        mockHs_1.createEvents("no");
        assertEquals(0,mockHs_1.createEventsAction);

        // reset
        Home_screen mockHs_2 = mock(Home_screen.class);
        doCallRealMethod().when(mockHs_2).createEvents(anyString());
        mockHs_2.createEventsAction = 1;
        // no, yes
        mockHs_2.createEvents("no");
        verify(mockHs_2, times(1)).sendBotMessage("Do you want to add attendees? yes/no");
        mockHs_2.createEvents("yes");
        mockHs_2.createEventsAction = 3;
        verify(mockHs_2, times(1)).sendBotMessage("Please enter list of attendees' emails, separate by comma");
        mockHs_2.createEvents("abc@gmail.com");
        assertEquals(0,mockHs_2.createEventsAction);

        // reset
        Home_screen mockHs_3 = mock(Home_screen.class);
        doCallRealMethod().when(mockHs_3).createEvents(anyString());
        mockHs_3.createEventsAction = 1;
        // no, no
        mockHs_3.createEvents("no");
        verify(mockHs_3, times(1)).sendBotMessage("Do you want to add attendees? yes/no");
        mockHs_3.createEvents("no");
        assertEquals(0,mockHs_2.createEventsAction);
    }

    @Test
    public void testSendUSerMessage() {
        doCallRealMethod().when(mockHs).sendUserMessage(anyString());
        doCallRealMethod().when(mockHs).checkForAction(anyString());
        int count = 1;

        // search event
        mockHs.sendUserMessage("show me next event");
        verify(mockHs, times(1)).searchEvents("show me next event");

        //  create event
        mockHs.sendUserMessage("create event \"event\"");
        verify(mockHs, times(1)).createEvents("create event \"event\"");

        mockHs.sendUserMessage("create event event");
        verify(mockHs, times(1)).sendBotMessage("Please re-enter the request with the event label inside double quotes");
        count++;

        // google search
        mockHs.sendUserMessage("search panda");
        verify(mockHs, times(count)).sendBotMessage(anyString());

        // weather
        mockHs.sendUserMessage("How's the weather?");
        verify(mockHs, times(1)).runWeatherSearch("How's the weather?");

        // game
        mockHs.sendUserMessage("game 1");
        verify(mockHs, times(1)).confirmGamesAction("game 1");

        // help
        mockHs.sendUserMessage("help");
        verify(mockHs, times(1)).confirmHelpAction("help");
    }

    @Test
    public void testConfirmGamesAction() {
        doCallRealMethod().when(mockHs).confirmGamesAction(anyString());

        mockHs.confirmGamesAction("yes");
        verify(mockHs, times(1)).sendBotMessage("To make a move, enter rock, paper, or scissors.");
        assertEquals(1,mockHs.gameAction);
        mockHs.confirmGamesAction("rock");
        verify(mockHs, times(1)).rockPaperScissors();

        mockHs.confirmGamesAction("no");
        assertEquals(0,mockHs.gameAction);
    }

    @Test
    public void testConfirmHelpAction() {
        doCallRealMethod().when(mockHs).confirmHelpAction(anyString());
        int count = 1;
        //mockHs.HelpActionIter = 0;
        mockHs.confirmHelpAction("help");
        verify(mockHs, times(1))
                .sendBotMessage("I can help you with understanding what I can do," +
                        " if you would like. I have a variety of capabilities that you can learn about including event management," +
                        " weather, news, and many others. For info on events, please type events." +
                        " For weather, please type weather. For news or general searching, please type search. " +
                        "For traffic and maps, please type map. " +
                        "Finally, if you would like to see the tutorial which you received when you first opened the app, " +
                        "you can type tutorial to open it. When you no longer need help, simply type exit.");

        mockHs.confirmHelpAction("events");
        verify(mockHs, times(1)).sendBotMessage("I can help you create and check your events within my app!" +
                " To create an event, type create event. I will then ask you a series of questions about the event you wish to create." +
                " To show your already made events, type show event. Make sure that your google account is linked to my app!");

        mockHs.confirmHelpAction("weather");
        verify(mockHs, times(1)).sendBotMessage("If you would like information on the weather in your area or in any area," +
                " simply type weather or something like \"What is the forecast in Columbia, South Carolina?\"" +
                " This will trigger my weather protocol and I can help you find specific or general information about any city you wish!");

        mockHs.confirmHelpAction("search");
        verify(mockHs, times(1)).sendBotMessage("This is my default response if it does not seem like we are conversing," +
                " but I do not understand what you would like me to do. You can manually initiate a search by including the word search in your message. " +
                "You can also specify the number of results, by including a stipulation such as \"top 5\" or \"5 results\" in your message to me." +
                " Asking me for news works in a similar way, you should include the word news, or headlines, in your request.");

        mockHs.confirmHelpAction("map");
        verify(mockHs, times(1)).sendBotMessage("You can ask me for directions to a location near (or far from) you." +
                " To do so, simply include \"directions to\" in your request, along with the location which you would like to reach." +
                " You can also ask for traffic in your area by just including \"traffic\" or traffic around a certain place by asking for \"traffic near/to/in\" a place.");

        mockHs.confirmHelpAction("exit");
        verify(mockHs, times(1)).sendBotMessage("Let me know if you need help again! Just type \"help\" at any time!");

        mockHs.HelpActionIter = 1;
        mockHs.confirmHelpAction("idk");
        verify(mockHs, times(1)).sendBotMessage("I did not recognize what you are asking for help with, please respond with either \"events\", \"weather\", \"search\", or \"map\". You can also type \"exit\" to exit. Thank you!");
    }

}
