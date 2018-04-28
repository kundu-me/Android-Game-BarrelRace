package com.utdallas.nxkundu.barrelracegame.gamesettings;

import android.graphics.Color;

/**
 * Created by nxkundu on 4/20/18.
 */
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This class contains all the constants to the Game Settings
 *
 * When the Game starts the settings are read from file,
 * When the user changes any settings, all the settings are written to file
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class GameSettings {

    public static final String LABEL_PLAY = "Play";

    public static final String LABEL_PAUSE = "Pause";

    public static final String LABEL_RESET = "Reset";

    public static final String LABEL_PLAYER_SCORE = "PlayerScore";

    public static final String ERR_PLAYER_NAME = "Please Enter the Player Name";

    public static final String LABEL_SAVED_SUCCESSFULLY = "Saved Successfully";

    public static final String FILE_NAME_SCORES = "BarrelRaceScores_NK.txt";

    public static final String FILE_NAME_SETTINGS = "BarrelRaceSettings_NK.txt";

    public static float BARREL_RADIUS = 35;

    public static float HORSE_RADIUS = 35;

    public static float BARREL_BOUNDARY_RADIUS = BARREL_RADIUS + HORSE_RADIUS + 150;

    public static float BARREL_BOUNDARY_MIN_DISTANCE = 5;

    public static int BARREL_COLOR = Color.RED;

    public static int HORSE_COLOR = Color.GREEN;

    public static int COURSE_COLOR = Color.RED;

    public static int BACKGROUND_COLOR = Color.BLACK;

    public static int BARREL_COLOR_COMPLETED = Color.WHITE;

    public static float ACCELEROMETER_RATE = 1.45f;

    public static float COURSE_LINE_WIDTH = 15;

    public static float WAIT_ON_COURSE_TOUCH_LONG_TIME = 5000;

    public static float MAX_GAME_LONG_TIME = 60000;

    public static final int MAX_COUNT_SCORES = 10;

}
