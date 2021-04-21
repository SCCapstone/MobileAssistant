# MobileAssistant

https://sccapstone.github.io/MobileAssistant/

Our app will have an assistant that will be similar to Google Assistant. The user
will be able to interact with an animated character by talking to him/her with commands 
or conversations. The app will respond with an appropriate answer.

Your audience for the Readme.md are other developers who are joining your team.
Specifically, the file should contain detailed instructions that any developer
can follow to install, compile, run, and test your project. These are not only
useful to new developers, but also to you when you have to re-install everything
because your old laptop crashed. Also, the teachers of this class will be
following your instructions.

## External Requirements

No external requirements.

## Running

Running the app from within Android Studio
1. Clone repo to Android Studio
2. Open AVD Manager
3. Create Virtual Device (recommended to use Google Pixel 3 API 30, R system image)
4. Click Launch this AVD in the emulator
5. Ensure that you have given the 'MobileAssistant' app location permissions. The app should ask if you have not given it permissions and you ask for a location-related function, however if crashes occur when using mapping functions, this may be why.
6. The location the emulator says it is at is the Googleplex in California, but you can manually change your emulator location using the "extended controls" menu for the emulator.
7. If the app had been installed on this emulator from last semester(fall 2020), it has to be uninstalled first before installing the new release using the apk file, because the "sha-1 certificate fingerprint" has been changed this semester and will cause a problem when running commands that will access Google Calendar. (note. if using Android Studio, do not install the app use the "run" button instead run the emulator only and install the app use the apk file instead of using the repo from github.)

## Commands

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

## Testing
Unit Tests can be found in MobileAssistant/app/src/test/java/com/example/mobileassistant

Behvavioral Tests can be found in MobileAssistant/app/src/androidTest/java/com/example/mobileassistant

1. Complete the above steps in the Running section of the ReadMe to a point where the app has successfully launched at least to the Home Screen once. This means the user should fill the start screen information out once before they test the app.
2. Ensure that at least one command utilizing your Google account has been run, such as an "event" command. If you have successfully logged into your google account and linked it to the MobileAssistant app on the emulator, that should be sufficient.
3. Close the Virtual Device and navigate in Android Studio to either app/src/androidTest/java/com/example/mobileassistant (Behavioral Tests) or app/src/test/java/com/example/mobileassistant (Unit Tests) in the Project View.
4. Right click whatever test the user wishes to run and then left click Run 'TestName' where TestName is the name of whatever test the user wishes to run.
5. If the test is a Behavioral Test, the Virtual Device will pop up and the user will be able to spectate the test and the results (pass or fail) will be displayed in the command line window of Android Studio. If the test is a Unit Test, the Virtual Device will not pop up but the test results will be visible in the command line window of Android Studio.

# Authors

Cameron Brandenburg cpb2@email.sc.edu

Henry Vy vyh@email.sc.edu

Cooper Gregory cooperlg@email.sc.edu

Hui Wang huiw@email.sc.edu

Dongyu Chen dongyu@email.sc.edu

