
## Demo Video
{% include youtubePlayer.html id="NpEaa2P7qZI" %}

## What is MobileAssistant?
X

## Why use MobileAssistant?
X

## How to Use:
You can send any message to the assistant, and it will converse with you, but it will default to searching the internet for what you asked it for if it does not understand what you are talking to it about. 

**In order to access your google calender to edit or get information about events, user would have to give the app permission to access the calendar. When commands about events entered in the chat, the app will ask the user to either login into your gmail or to pick an existing gmail and ask for permissions. (Please note: The app is still under "testing", so if you would like to test the app please give us your gmail address because we have to add your gmail into the "testers" list for the Google API Console, otherwise you won't have the permission to use the Google Calendar API and will cause error when run commands relate to events.)**

1. If the user types in something with the keywords "show event(s)" then by default you will receive information about your next event.Or you can specify the number of events you would like to see by including a number in your message. Example Commands:
* show me next event
* show me next 5 events
2. The user can specify which event they would like to see with the "show event" command by including the name of the event in quotations. Example commands :
* show event "event"

**(Please note: event label has to be between double quotes.)**

3. If the user sends a message with the keywords "create event" then you will create an event on your Google Calender. You can specify information about the Google Calender when you use the "create event" keyword. You can create a event with a specific date and time by using a keyword "on" for the date and "am/pm" for the time. Also, the user will be asked if he/she wants to add a location and attendees for the creating event. Example Commands:
* create event for a specific date & time     -    create event "event" on Feb 27th from 3pm to 4pm   
* create event for a specific date only (whole day event)    - create event "whole" on Feb 28th

**(Please note: the event label has to be within double quotes and date has to be after the keyword "on", time has to be followed by either "am" or "pm")**

4. The user can also create recurring events by specifying the day of the week when the event will occur and a stop date by using the keyword "until" follow by the end date (month and year). Example commands:
* create event "Event" repeat every Tuesday and Thursday until April, 2021

**(Please note: Reccuring events must have a stop date! the event label has to be within double quotes, time has to be followed by either "am" or "pm", end date has to be after the keyword "until" with the format "month, year")**

5. The user can ask the assistant to search the internet using the "search" keyword or "look up" keyword. This will return a set of links and their HTML titles if they are available. Example :
* search Columbia SC
6. The user can also specify the number of results you would like to receive from your search if you include the words 'top' or 'results' and a number. Example :
* search University of South Carolina top 5 results
7. The user can ask for news by entering the "news" keyword or "headlines" keyword anywhere in your message. The first time the user asks for news, they will be informed that they can ask for news relating to a specific/topic or area, by specifying the subject or location they would like in their request. Example :
* covid 19 news
8. Typing "weather", "forecast", or "temperature" will initiate a series of questions to the user related to what weather information that they would like to receive, such as the specific information they would like (if any) and the location they would like to know about.
9. If the user asks for certain weather information by stating "what is" or "how is" along with one of the above weather related terms and the location they would like to know about, they will not need to go through the prompts to ascertain their location and request. For example :
* what is the forecast in Philadelphia PA
11. If the user asks the assistant for "directions to" a location using the aforementioned keyword Google Maps will open with directions to that location.
12. If the user asks about traffic using the "traffic" keyword, they will receive a map with a traffic information layer in their general vicinity. The user can also specify where they would like to see traffic by inputting a phrase such as "traffic in X".
13. If the user requests "rock paper scissors", they will open up a text-based "rock, paper, scissors" game. The user then can pick a gesture and press either "yes" or "no" to continue or quit the game, respectively.
14. (Bonus Feature) When the user first opens the app, information relating to the usage of the app will be displayed. This is the full set of instructions for the app. If the user types "help" or various other keywords into the chat box, the assistant will prompt them with a set of inputs that will reiterate methods by which the user can use the app, though not all at once. The user can also use this prompt and type "tutorial" to re-open the introductory tutorial if they so wish.
