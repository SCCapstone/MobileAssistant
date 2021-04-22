package com.example.mobileassistant;

import java.util.Random;
import junit.framework.TestCase;

import org.junit.Test;

import static org.junit.Assert.*;

public class RockPaperScissorsTest extends TestCase {
    RockPaperScissors rps = new RockPaperScissors();
    Random random = new Random();
    int rand = random.nextInt(3);
    @Test
    public void testCheckUserGestureRock() {
        String expectedRock = "rock";
        String returnedGesture = rps.checkUserGesture(expectedRock);
        assertEquals(expectedRock,returnedGesture);
    }

    @Test
    public void testCheckUserGesturePaper() {
        String expectedPaper = "paper";
        String returnedGesture = rps.checkUserGesture(expectedPaper);
        assertEquals(expectedPaper,returnedGesture);
    }

    @Test
    public void testCheckUserGestureInvalid() {
        String expectedInvalid = "Invalid input!";
        String randomInput = "this is a random input 12345";
        String returnedGesture = rps.checkUserGesture(randomInput);
        assertEquals(expectedInvalid,returnedGesture);
    }

    @Test
    public void testCheckUserGestureScissors() {
        String expectedScissors = "scissors";
        String returnedGesture = rps.checkUserGesture(expectedScissors);
        assertEquals(expectedScissors,returnedGesture);
    }

    @Test
    public void testBotMove() {
        int count = 1;
        int countExpected = 3;
        String expectedBotMoveRock = "rock";
        String expectedBotMovePaper = "paper";
        String expectedBotMoveScissors = "scissors";
        for (int i = 0; i < 2; i++) {
            String botMove = rps.botMove();
            if(botMove.equalsIgnoreCase(expectedBotMoveRock)||botMove.equalsIgnoreCase(expectedBotMovePaper)||botMove.equalsIgnoreCase(expectedBotMoveScissors)){
                count++;
            }
        }
        assertEquals(countExpected,count);
    }

    @Test
    public void testTestResultRR() {
        String userInput = "rock";
        String botMove = "rock";
        String expected = "It's a tie!";
        String resultRR = rps.result(userInput,botMove);
        assertEquals(expected,resultRR);
    }
    public void testTestResultRP() {
        String userInput = "rock";
        String botMove = "paper";
        String expected = "You lost!";
        String resultRP = rps.result(userInput,botMove);
        assertEquals(expected,resultRP);
    }
    public void testTestResultRS() {
        String userInput = "rock";
        String botMove = "scissors";
        String expected = "You won!";
        String resultRS = rps.result(userInput,botMove);
        assertEquals(expected,resultRS);
    }
    public void testTestResultPR() {
        String userInput = "paper";
        String botMove = "rock";
        String expected = "You won!";
        String resultPR = rps.result(userInput,botMove);
        assertEquals(expected,resultPR);
    }
    public void testTestResultPP() {
        String userInput = "paper";
        String botMove = "paper";
        String expected = "It's a tie!";
        String resultPP = rps.result(userInput,botMove);
        assertEquals(expected,resultPP);
    }
    public void testTestResultPS() {
        String userInput = "paper";
        String botMove = "scissors";
        String expected = "You lost!";
        String resultPS = rps.result(userInput,botMove);
        assertEquals(expected,resultPS);
    }
    public void testTestResultSR() {
        String userInput = "scissors";
        String botMove = "rock";
        String expected = "You lost!";
        String resultSR = rps.result(userInput,botMove);
        assertEquals(expected,resultSR);
    }
    public void testTestResultSP() {
        String userInput = "scissors";
        String botMove = "paper";
        String expected = "You won!";
        String resultSP = rps.result(userInput,botMove);
        assertEquals(expected,resultSP);
    }
    public void testTestResultSS() {
        String userInput = "scissors";
        String botMove = "scissors";
        String expected = "It's a tie!";
        String resultSS = rps.result(userInput,botMove);
        assertEquals(expected,resultSS);
    }

}