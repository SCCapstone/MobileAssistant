package com.example.mobileassistant;
import java.util.Random;
public class RockPaperScissors {

    //make a random number to have the range 0, 1, 2
    Random random = new Random();
    int rand = random.nextInt(3);

    //check whether the user's input is one of the gestures
    //returns the gesture if valid and returns "Invalid input!" if not
    public String checkUserGesture(String message) {
        if ( message.equalsIgnoreCase("rock")) {
            return "rock";
        } else if (message.equalsIgnoreCase("paper")) {
            return "paper";
        } else if (message.equalsIgnoreCase("scissors")) {
            return "scissors";
        }
        else{
            return "Invalid input!";
        }
    }

    //randomly assign the bot a move
    public String botMove(){
        //Convert the random number to a string using conditionals and print the opponent's move
        String botMove = "no move";
        if (rand == 0) {
            botMove = "rock";
            return botMove;
        } else if (rand == 1) {
            botMove = "paper";
            return botMove;
        } else if(rand == 2){
            botMove = "scissors";
            return botMove;
        }
        return botMove;
    }

    //determine whether the user wins
    public String result(String message, String botMove){
        if (message.equalsIgnoreCase(botMove)) {
            return "It's a tie!";
        } else if ((message.equalsIgnoreCase("rock")
                && botMove.equalsIgnoreCase("scissors"))
                || (message.equalsIgnoreCase("scissors")
                && botMove.equalsIgnoreCase("paper"))
                || (message.equalsIgnoreCase("paper")
                && botMove.equalsIgnoreCase("rock"))) {
            return "You won!";
        } else {
            return "You lost!";
        }
    }

}

