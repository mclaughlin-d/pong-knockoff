package com.mycompany.pongKnockoff;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.WindowConstants;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JLabel;
/**
 *
 * @author Dani McLaughlin - 2021
 */
public class PingPongTable extends JPanel implements GameConstants{
    JLabel label;
    public Point point = new Point(0,0);
    private int ComputerRacket_X = COMPUTER_RACKET_X;
    private int playerRacket_Y = KID_RACKET_Y_START;
    
    private int computerRacket_Y = COMPUTER_RACKET_Y_START;
    
    Dimension preferredSize = new Dimension(TABLE_WIDTH, TABLE_HEIGHT);
    
    private int ballX = GameConstants.BALL_START_X;
    private int ballY = GameConstants.BALL_START_Y;
    
    PingPongTable(){
        PingPongGameEngine gameEngine = new PingPongGameEngine(this);
        addMouseMotionListener(gameEngine);
    }
    
    // get dimension of panel to be drawn (necessary for proper dimensions)
    public Dimension getPreferredSize(){
        return preferredSize;
    }
    
    // add a panel to the container
    public void addPaneltoFrame(Container container){
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(this);
        label = new JLabel("Press 'n' to start the game, then press 's' to serve.");
        container.add(label);
    }
    
    // change the message at the bottom of the table
    public void setMessageToText(String text){
        label.setText(text);
    }
    
    // draw the components onto the screen
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        // draw the table background
        Color tableColor = Color.decode(TABLE_COLOR);
        g.setColor(tableColor);
        g.fillRect( 0,0,TABLE_WIDTH, TABLE_HEIGHT);
        
        // draw the computer racket
        Color computerRacketColor = Color.decode(COMP_RACKET_COLOR);
        Color racketColor = Color.decode(PLAYER_RACKET_COLOR);
//        g.fillRect( 0,0,TABLE_WIDTH, TABLE_HEIGHT);
        g.setColor(computerRacketColor);
        g.fillRect(ComputerRacket_X,computerRacket_Y,20,120);
        
        // draw the player racket
        g.setColor(racketColor);
        g.fillRect(PLAYER_RACKET_Y_START, playerRacket_Y,20,120);
        
        // draw the ball
        Color ballColor = Color.decode(BALL_COLOR);
        g.setColor(ballColor);
        g.fillOval(ballX,ballY,40,40);
        
        // draw the middle dividing line
        g.setColor(Color.WHITE);
        g.drawRect(20,20,1160,760);
        g.drawLine(600,20,600,780);
    }
   
    // set the ball's position and re-paint it onto screen
    public void setBallPosition(int xPos, int yPos){
        ballX = xPos;
        ballY = yPos;
        repaint();
    }
    
    // set the score text
    public void setScore(int computerScore, int kidScore){
        label.setText("Computer: " + computerScore + "; Player: "+ kidScore);
    }
    
    // set the player's racket y-position
    public void setKidRacket_Y(int xCoordinate){
        this.playerRacket_Y = xCoordinate;
    }
    
    // get the player's racket y-position
    public int getKidRacket_Y(int xCoordinate){
        return playerRacket_Y;
    }
    
    
    public static void main(String[] args){
        JFrame f = new JFrame("Ping Pong Green Table");
        
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // initialize ping pong table
        PingPongTable table = new PingPongTable();
        // add the table to the frame (draw it)
        table.addPaneltoFrame(f.getContentPane());
        // pack the frame
        f.pack();
        // make it visible
        f.setVisible(true);
        
    }
    
    // set the computer racket y-position and repaint
    public void setComputerRacket_Y(int yCoordinate){
        this.computerRacket_Y = yCoordinate;
        repaint();
    }
}
