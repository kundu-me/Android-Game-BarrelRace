package com.utdallas.nxkundu.barrelracegame.gamesettings;

import android.content.Context;
import android.graphics.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by nxkundu on 4/22/18.
 */
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This Class Handles the
 * READ
 * WRITE
 * function of the Game Settings
 *
 * Whenever a User Changes the Settings it is Updated
 * and also written to the Settings file
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class ReadWriteGameSettings {

    Context context;
    public static ReadWriteGameSettings readWriteGameSettings;

    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    private ReadWriteGameSettings(Context context) {

        super();
        this.context = context;
        createFileIfNotExists();
    }

    /**************************************************************************
     * Method
     * getInstance()
     * get Singleton class Object
     **************************************************************************/
    public static ReadWriteGameSettings getInstance(Context context) {

        if(readWriteGameSettings == null) {
            readWriteGameSettings = new ReadWriteGameSettings(context);
        }

        return readWriteGameSettings;
    }

    /**************************************************************************
     * Method
     *
     * readGameSettings()
     *
     * This method reads Game Settings from the pre defined file
     **************************************************************************/
    public void readGameSettings() {

        BufferedReader objBufferedReader = null;

        try {

            FileInputStream objFileInputStream = context.openFileInput(GameSettings.FILE_NAME_SETTINGS);
            InputStreamReader objInputStreamReader = new InputStreamReader(objFileInputStream);
            objBufferedReader = new BufferedReader(objInputStreamReader);
            String strLine = "";

            if((strLine = objBufferedReader.readLine()) != null) {
                String[] arrWords = strLine.split("\t");

                String horseColor = arrWords.length >= 1? arrWords[0] : "";
                String barrelColor = arrWords.length >= 2? arrWords[1] : "";
                String gameLevel = arrWords.length >= 3? arrWords[2] : "";

                if("green".equalsIgnoreCase(horseColor)) {
                    GameSettings.HORSE_COLOR = Color.GREEN;
                }
                else if("yellow".equalsIgnoreCase(horseColor)){
                    GameSettings.HORSE_COLOR = Color.YELLOW;
                }
                else if("magneta".equalsIgnoreCase(horseColor)) {
                    GameSettings.HORSE_COLOR = Color.MAGENTA;
                }

                if("red".equalsIgnoreCase(barrelColor)) {
                    GameSettings.BARREL_COLOR = Color.RED;
                }
                else if("blue".equalsIgnoreCase(barrelColor)){
                    GameSettings.BARREL_COLOR = Color.BLUE;
                }
                else if("white".equalsIgnoreCase(barrelColor)) {
                    GameSettings.BARREL_COLOR = Color.WHITE;
                }

                if("easy".equalsIgnoreCase(gameLevel)) {
                    GameSettings.ACCELEROMETER_RATE = 1.45f;
                }
                else if("medium".equalsIgnoreCase(gameLevel)){
                    GameSettings.ACCELEROMETER_RATE = 2.50f;
                }
                else if("difficult".equalsIgnoreCase(gameLevel)) {
                    GameSettings.ACCELEROMETER_RATE = 2.90f;
                }
            }
        }
        catch(IOException e) {

            e.printStackTrace();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {

            if(objBufferedReader != null) {
                try {
                    objBufferedReader.close();
                }
                catch(Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    /**************************************************************************
     * Method
     *
     * writeGameSettings()
     *
     * This methods writes Game Settings to file
     * Whenever the user changes any Game Settings
     **************************************************************************/
    public void writeGameSettings() {

        try {

            FileOutputStream objFileOutputStream = context.openFileOutput(GameSettings.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);

            BufferedWriter objBufferedWriter = new BufferedWriter(new OutputStreamWriter(objFileOutputStream));

            String line = "";

            if(GameSettings.HORSE_COLOR == Color.GREEN) {
                line += "green";
            }
            else if(GameSettings.HORSE_COLOR == Color.YELLOW){
                line += "yellow";
            }
            else if(GameSettings.HORSE_COLOR == Color.MAGENTA) {
                line += "magneta";
            }

            line += "\t";

            if(GameSettings.BARREL_COLOR == Color.RED) {
                line += "red";
            }
            else if(GameSettings.BARREL_COLOR == Color.BLUE){
                line += "blue";
            }
            else if(GameSettings.BARREL_COLOR == Color.WHITE) {
                line += "white";
            }

            line += "\t";

            if(GameSettings.ACCELEROMETER_RATE < 1.5) {
                line += "easy";
            }
            else if(GameSettings.ACCELEROMETER_RATE > 2.0 && GameSettings.ACCELEROMETER_RATE< 2.6) {
                line += "medium";
            }
            else if(GameSettings.ACCELEROMETER_RATE > 2.6) {
                line += "difficult";
            }

            objBufferedWriter.write(line);
            objBufferedWriter.newLine();
            objBufferedWriter.close();
            objFileOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    /**************************************************************************
     * Method
     * createFileIfNotExists()
     *
     * This method Creates the GameSettings File if it is not present
     **************************************************************************/
    private void createFileIfNotExists()  {

        File objFileDirs = new File(String.valueOf(context.getFilesDir()));
        File objFile = new File(objFileDirs, GameSettings.FILE_NAME_SETTINGS);

        try{

            if(!objFile.exists()) {

                objFile.createNewFile();
            }
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
