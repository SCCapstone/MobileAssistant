# MobileAssistant

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
5. The emulator automatically denies location access so go into the Settings of the emulated phone and give 'MobileAssistant' the location permission 'Always allow'.
6. The location the emulator says it is at is the Googleplex in California, but you can manually change your emulator location using the "extended controls" menu for the emulator.
7. If the app had been installed on this emulator from last semester(fall 2020), it has to be uninstalled first before install this new release use the apk file, because the "sha-1 certificate fingerprint" has been changed this semester and will cause a problem when running commands that will access Google Calendar. (note. if using Android Studio, do not install the app use the "run" button instead run the emulator only and install the app use the apk file instead of using the repo from github.)

## Commands

You can send any message to the assistant, and it will converse with you, but it will default to searching the internet for what you asked it for if it does not understand what you are talking to it about. 

1. If the user types in something with the keywords "show event(s)" then you will receive information about your next event. You can specify the number of events you would like to see by including a number in your message. Examples:
* show me next event
* show me next 5 events
3. The user can specify which event they would like to see with the "show event" command by including the name of the event in quotations. Example :
* show event "event"
4. If the user sends a message with the keywords "create event" then you will create an event on your Google Calender. You can specify information about the Google Calender when you use the "create event" keyword. You can create a event with a specific date and time by using a keyword "on" for the date and "am/pm" for the time. Also, the user will be asked if he/she wants to add a location and attendees for the creating event. Example :
* create event for a specific date & time     -    create event "event" on Feb 27th from 3pm to 4pm   
* create event for a specific date only (whole day event)    - create event "whole" on Feb 28th
5. The user can also specify an event that will repeat by specifying a day of the week which the event will occur and a stopping date by using the day of week and the keyword "until" for the end date. Example:
* create event "Event" repeat every Tuesday and Thursday until April, 2021
6. The user can ask the assistant to search the internet using the "search" keyword or "look up" keyword. This will return a set of links and their HTML titles if they are available. Example :
* search Columbia SC
7. The user can also specify the number of results you would like to receive from your search if you include the words 'top' or 'results' and a number. Example :
* search University of South Carolina top 5 results
8. The user can ask for news by entering the "news" keyword or "headlines" keyword anywhere in your message. You will be prompted for what news you would like to receive and after specifying what news that is, you will receive relevant results about it. Example :
* covid 19 news
9. Typing "weather", "forecast", or "temperature" will initiate a series of questions to the user related to what weather information that they would like to receive, such as the specific information they would like (if any) and the location they would like to know about.
10. If the user asks the assistant for "directions to" a location using the aforementioned keyword Google Maps will open with directions to that location.
11. If the user asks about traffic using the "traffic" keyword, they will receive a map with a traffic information layer in their general vicinity. 
12. If the user sends "game 2", they will open up a text-based "rock, paper, scissors" game. The user then can pick a gesture and press either "yes" or "no" to continue or quit the game, respectively.

## Testing
Unit Tests can be found in MobileAssistant/app/src/test/java/com/example/mobileassistant

Behvavioral Tests can be found in MobileAssistant/app/src/androidTest/java/com/example/mobileassistant

1. Complete the above steps in the Running section of the ReadMe to a point where the app has successfully launched at least to the Start Screen once.
2. Close the Virtual Device and navigate in Android Studio to either app/src/androidTest/java/com/example/mobileassistant (Behavioral Tests) or app/src/test/java/com/example/mobileassistant (Unit Tests) in the Project View.
3. Right click whatever test the user wishes to run and then left click Run 'TestName' where TestName is the name of whatever test the user wishes to run.
4. If the test is a Behavioral Test, the Virtual Device will pop up and the user will be able to spectate the test and the results (pass or fail) will be displayed in the command line window of Android Studio. If the test is a Unit Test, the Virtual Device will not pop up but the test results will be visible in the command line window of Android Studio.

# Authors

Cameron Brandenburg cpb2@email.sc.edu

Henry Vy vyh@email.sc.edu

Cooper Gregory cooperlg@email.sc.edu

Hui Wang huiw@email.sc.edu

Dongyu Chen dongyu@email.sc.edu

