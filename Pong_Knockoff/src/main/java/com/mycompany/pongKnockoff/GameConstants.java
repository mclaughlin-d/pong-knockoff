package com.mycompany.pongKnockoff;

/**
 *
 * @author Dani McLaughlin, 2021
 */
public interface GameConstants {
    // table-related constants
    public final int TABLE_WIDTH = 1200;
    public final int TABLE_HEIGHT = 800;
    public final int TABLE_TOP = 24;
    public final int TABLE_BOTTOM = 760;
    
    // player racket related constants
    public final int KID_RACKET_Y_START = 340;
    public final int PLAYER_RACKET_Y_START = 1140;
    
    public final int COMPUTER_RACKET_X = 40;
    public final int COMPUTER_RACKET_Y_START = 340;
    
    public final int RACKET_INCREMENT = 8;
    
    // ball-related constants
    public final int BALL_INCREMENT = 20;
    
    public final int BALL_MIN_X = BALL_INCREMENT;
    public final int BALL_MIN_Y = BALL_INCREMENT + 20;
    public final int BALL_MAX_X = TABLE_WIDTH - 60;
    
    public final int BALL_MAX_Y = TABLE_HEIGHT - BALL_INCREMENT - 60;
    public final int BALL_START_X = 600;
    public final int BALL_START_Y = 400;
    public final int BALL_CENTER = 10;
    
    // color constants
    public final String TABLE_COLOR = "#164A41";
    public final String COMP_RACKET_COLOR = "#4D774E";
    public final String PLAYER_RACKET_COLOR = "#9DC88D";
    public final String BALL_COLOR = "#F1B24A";
}
