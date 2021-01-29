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

