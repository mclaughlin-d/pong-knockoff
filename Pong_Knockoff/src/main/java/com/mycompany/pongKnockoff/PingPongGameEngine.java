package com.mycompany.pongKnockoff;
// mouse imports
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
// log imports
import java.util.logging.Level;
import java.util.logging.Logger;
// keyboard imports
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.Random;

// sound imports
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Dani McLaughlin - 2021
 */
public class PingPongGameEngine implements Runnable, KeyListener, MouseMotionListener, GameConstants {
    private final PingPongTable table;
    public int kidRacket_Y = KID_RACKET_Y_START;
    
    private int computerRacket_Y = GameConstants.COMPUTER_RACKET_Y_START;
    private int numberServe = 0;
    Thread worker;
    
    // ball position
    private int ballX = BALL_START_X;
    private int ballY = BALL_START_Y;
    // ball boolean state variables
    private boolean ballServed = false;
    private boolean movingLeft = false;
    private boolean ballMovingUp = false;
    
    private int ballXIncrement = BALL_INCREMENT;
    private int ballYIncrement = 0;
    private int computerRacketIncrement = RACKET_INCREMENT + 11;
    
    // racket boolean state variables
    private boolean hitOffKidRacket = false;
    private boolean hitOffComputerRacket = false;
    
    // computer racket direction
    private boolean movingUp = true;
    
    // playing game 
    private boolean playingGame = false;
     
    boolean firstServe = true;
        
    // score variables
    private int playerScore = 0;
    private int computerScore = 0;
    
    PingPongGameEngine(PingPongTable t){
        table = t;
        table.addKeyListener(this);
        
        worker = new Thread(this);
        worker.start();
        
    }
    
    @Override
    // handle the mouse being moved
    public void mouseMoved(MouseEvent e){
        int mouse_Y = e.getY();
        handleMouseMovement(mouse_Y);
    }
    
    @Override
    // handle the mouse being dragged (clicked and moved)
    public void mouseDragged(MouseEvent e){
        int mouse_Y = e.getY();
        handleMouseMovement(mouse_Y);
    }
    
    // move the racket based on the y-position of the mouse
    public void handleMouseMovement(int mouse_Y) {
        if (mouse_Y < kidRacket_Y && kidRacket_Y > TABLE_TOP){
            kidRacket_Y -= RACKET_INCREMENT;
        }
        else if (mouse_Y > kidRacket_Y && kidRacket_Y >= TABLE_BOTTOM - 100){
            kidRacket_Y = kidRacket_Y;
        }
        else{
            kidRacket_Y += RACKET_INCREMENT;
        }
        table.setKidRacket_Y(kidRacket_Y);
        table.repaint();
    }

    @Override
    // main loop of execution 
    public void run() {
        while(true){
            table.requestFocus();
            if (ballServed){
                //computer racket stuff
                computerRacketAI();
                if (randomBoolean()){
                    computerRacketIncrement -= randomizeComputerRacket();
                }
                // handle computer racket movement
                if (movingUp){
                    computerRacket_Y -= computerRacketIncrement;
                }
                else {
                    computerRacket_Y += computerRacketIncrement;
                }
            
                if (computerRacket_Y <= TABLE_TOP && movingUp){
                    movingUp = false;
                }
                else if (computerRacket_Y >= TABLE_BOTTOM - 120 && !movingUp){
                    movingUp = true;
                }
                table.setComputerRacket_Y(computerRacket_Y);
                
                // handle horizontal movement of the ball
                ballMovingHorizontal();
                // handle vertical movement of the ball
                ballMovingVertical();
                // set the new ball position
                table.setBallPosition(ballX, ballY);
                
                // wait for a small amount of time
                try{
                    Thread.sleep(20);
                }
                catch (InterruptedException ex){
                    Logger.getLogger(PingPongGameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                // handle score
                table.setScore(computerScore, playerScore);
                try {
                    checkScore();
                } catch (InterruptedException ex) {
                    Logger.getLogger(PingPongGameEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    // handles horizontal movement of the ball
    public void ballMovingHorizontal() {
        // ball moving left (towards computer racket)
        if (movingLeft){
            // test if the ball is hit off the computer racket
            if (ballY <= computerRacket_Y + 120 && ballY >= computerRacket_Y){
                if (ballX >= BALL_MIN_X + 25 && ballX <= BALL_MIN_X + 40){
                    movingLeft = false;
                    hitOffComputerRacket = true;
                    setBallIncrements();
                    ballX += ballXIncrement;

                    playGameSound("ballbounceeffect.wav");
                }
                else if (ballX <= BALL_MIN_X){
                    // player gets point
                    playerScore++;
                    setBallIncrements();
                    ballServed = false;

                    playGameSound("kidpointsound.wav");
                }
                else{
                    ballX -= ballXIncrement;
                }
            }
            // ball hit computer's wall
            else{
                if (ballX <= BALL_MIN_X){
                    // player gets point
                    playerScore++;
                    ballServed = false;
                    // play sound
                    playGameSound("kidpointsound.wav");
                }
                else{
                    ballX -= ballXIncrement;
                }
            }
        }
        // ball moving right (towards player racket)
        else {
            if (ballY <= kidRacket_Y + 120 && ballY >= kidRacket_Y){
                if (ballX >= BALL_MAX_X - 40 && ballX <= BALL_MAX_X - 25){
                    movingLeft = true;
                    ballX -= ballXIncrement;
                    setBallIncrements();
                    hitOffKidRacket = true;
                    // play sound
                    playGameSound("ballbounceeffect.wav");
                }
                else if (ballX >= BALL_MAX_X){
                    // computer gets point
                    computerScore++;
                    ballServed = false;
                    setBallIncrements();
                    // play sound
                    playGameSound("ballstopsoundeffect.wav");
                }
                else{
                    ballX += ballXIncrement;
                }
            }
            else{
                if (ballX >= BALL_MAX_X){
                    // computer gets point
                    computerScore++;
                    ballServed = false;
                    setBallIncrements();
                    // play sound
                    playGameSound("ballstopsoundeffect.wav");
                }
                else{
                    ballX += ballXIncrement;
                }
            }
        }
    }
    
    // handles vertical movement of the ball
    public void ballMovingVertical() {
        //ball moving up
       if (ballMovingUp){
           if (ballY > BALL_MIN_Y){
               ballY -= ballYIncrement;
           }
           else if (ballY <= BALL_MIN_Y){
               setBallIncrements();
               ballMovingUp = false;
               ballY += ballYIncrement;
               // sound
               playGameSound("ballbounceeffect.wav");
           }
       }
       // ball moving down
       else if (!ballMovingUp){
           if (ballY < BALL_MAX_Y){
               ballY += ballYIncrement;
           }
           else if (ballY >= BALL_MAX_Y){
               setBallIncrements();
               ballMovingUp = true;
               ballY -= ballYIncrement;
               // sound
               playGameSound("ballbounceeffect.wav");
           }

       }
    }
    
    // plays the sound from a given filepath
    public void playGameSound(String filePath) {
        try {
            // play sound
            playSound(filePath);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(PingPongGameEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PingPongGameEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(PingPongGameEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    // start a new game by setting all appropriate values
    public void startNewGame(){
        playingGame = true;
        playerScore = 0;
        computerScore = 0;
        ballX = BALL_START_X;
        ballY = BALL_START_Y;
        table.setBallPosition(ballX, ballY);
        ballXIncrement = 20;
        ballYIncrement = 0;
        table.setScore(computerScore, playerScore);
    }
    
    // end the game by setting appopriate values
    public void endGame(){
        ballServed = false;
        playingGame = false;
        playerScore = 0;
        computerScore = 0;
        table.setScore(computerScore, playerScore);
    }

    @Override
    // handle keypresses for game actions
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        if ('n' == key || 'N' == key){
            if (ballServed == true){
                table.setMessageToText("You cannot start a new game mid-play.");
            }
            else{
                startNewGame();
                table.setScore(computerScore, playerScore);
            }
        }
        else if ('q' == key || 'Q' == key){
            endGame();
            table.setMessageToText("Game ended.");
        }
        else if ('s' == key || 'S' == key){
            if (playingGame == true){
                playerServe();
                table.setScore(computerScore, playerScore);
            }
            else{
                table.setMessageToText("You must start a new game (press 'n') before serving the ball.");
            }
        }
    }
    
    // player serves the ball
    private void playerServe(){
        ballServed = true;
        //movingLeft = true;
        if (numberServe == 0){
            movingLeft = true;
        }
        numberServe++;
        ballX = BALL_START_X;
        ballY = BALL_START_Y;
        
        ballYIncrement = 0;
        table.setBallPosition(ballX,ballY);
        table.setKidRacket_Y(kidRacket_Y);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
    // randomize the computer racket speed
    public int randomizeComputerRacket(){
        Random rand = new Random();
        int max = 5;
        int min = 0;
        
        int number = rand.nextInt(max);
        return number;
    } 
    
    // randomize the ball's speed
    public int randomizeBallDirection(){
        Random rand = new Random();
        int max = 10;
        int number = rand.nextInt(max);
        return number;
    }
    
    // set the ball increments for x & y directions
    public void setBallIncrements(){
        if (hitOffKidRacket){
            ballYIncrement = randomizeBallDirection() + 10;
            // two most extreme angles
            if (ballY <= kidRacket_Y + 20){
                ballYIncrement += 5;
            }
            else if (ballY >= kidRacket_Y + 100){
                ballYIncrement += 5;
            }
            // slightly less extreme
            else if (ballY <= kidRacket_Y + 40 && ballY > kidRacket_Y + 20){
                ballYIncrement += 2;
            }
            else if (ballY <= kidRacket_Y + 100  && ballY > kidRacket_Y + 80){
                ballYIncrement += 2;
            }
            // middle
            else{
                ballYIncrement += 1;
            }
            hitOffKidRacket = false;
        }
        else if (hitOffComputerRacket){
            ballYIncrement = randomizeBallDirection() + 10;
            // two most extreme angles
            if (ballY <= computerRacket_Y + 20){
                ballYIncrement += 4;
            }
            else if (ballY >= computerRacket_Y + 100){
                ballYIncrement += 4;
            }
            // slightly less extreme
            else if (ballY <= computerRacket_Y + 40 && ballY > computerRacket_Y + 20){
                ballYIncrement += 2;
            }
            else if (ballY <= computerRacket_Y + 100  && ballY > computerRacket_Y + 80){
                ballYIncrement += 2;
            }
            // middle
            else{
                ballYIncrement += 1;
            }
            hitOffComputerRacket = false;
        }
        else{
            ballYIncrement = randomizeBallDirection() + 10;
            if (randomBoolean() == true){
                 ballYIncrement += randomizeBallDirection();
            }
            else{
                ballYIncrement -= randomizeBallDirection();
            }
        }
        // randomly determine if ball will move up or down
        if (randomBoolean()){
            ballMovingUp = true;
        }
        else{
            ballMovingUp = false;
        }
        
    }
    
    // generates a boolean value based on a random number
    public boolean randomBoolean(){
        Random rand = new Random();
        int number = rand.nextInt(2);
        if (number == 0){
            return false;
        }
        else{
            return true;
        }
    }
    
    // have the computer racket track the ball's movement
    public void computerRacketAI(){
        computerRacketIncrement = RACKET_INCREMENT + 10;
        if (computerRacket_Y > ballY - 60){
            movingUp = true;
        }
        else if (computerRacket_Y < ballY + 60){
            movingUp = false;
        }
    }
    
    // check the score of the game
    public void checkScore() throws InterruptedException{
        if (computerScore >= 11 || playerScore >= 11){
            if (abs(computerScore - playerScore) >= 2){
                // game ends, greater score wins
                ballServed = false;
                Thread.sleep(100);
                if (playerScore > computerScore){
                    table.setMessageToText("Congratulations!");
                    playGameSound("winningsound.wav");
                }
                else{
                    table.setMessageToText("Better luck next time!");
                    playGameSound("losingsound.wav");
                }
                playerScore = 0;
                computerScore = 0;
                playingGame = false;
            }
        }
    }
    
    // play a sound from a file
    public void playSound(String soundFileName) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
        File file = new File(soundFileName);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }
}
