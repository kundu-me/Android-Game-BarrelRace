package com.utdallas.nxkundu.barrelracegame.scores;

import android.content.Context;

import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;
import com.utdallas.nxkundu.barrelracegame.playerinfo.Player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nxkundu on 4/21/18.
 */
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This class
 * READ
 * WRITE
 * the user Top 10 Score
 *
 * Everytime a user completes the Game and wins the Game
 * The score is updated and written to the File.
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class Score {

    private List<Player> lstScores;

    private static Score score;

    private Context context = null;

    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    private Score(Context context) {
        super();

        this.context = context;
        lstScores = new ArrayList<>();

        createFileIfNotExists();
        readScores();
    }

    /**************************************************************************
     * Method
     * getInstance()
     * Get the Singleton Class Object
     **************************************************************************/
    public static Score getInstance(Context context) {

        if(score == null) {
            score = new Score(context);
        }

        return score;
    }

    /**************************************************************************
     * Method
     *
     * readScores()
     *
     * This method reads Scores from the file
     **************************************************************************/
    private void readScores() {

        BufferedReader objBufferedReader = null;

        try {

            FileInputStream objFileInputStream = context.openFileInput(GameSettings.FILE_NAME_SCORES);
            InputStreamReader objInputStreamReader = new InputStreamReader(objFileInputStream);
            objBufferedReader = new BufferedReader(objInputStreamReader);
            String strLine = "";

            while((strLine = objBufferedReader.readLine()) != null) {
                String[] arrWords = strLine.split("\t");

                String score = arrWords.length >= 1? arrWords[0] : "0";
                String playerName = arrWords.length >= 2? arrWords[1] : Player.DEFAULT_PLAYER_NAME;
                String playerDate = arrWords.length >= 3? arrWords[2] : "0";

                Player player = new Player(score, playerName, playerDate);

                lstScores.add(player);
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

        rankPlayers();
    }

    /**************************************************************************
     * Method
     * writeScores()
     * This method writes scores to the file
     **************************************************************************/
    private void writeScores() {

        try {

            FileOutputStream objFileOutputStream = context.openFileOutput(GameSettings.FILE_NAME_SCORES, Context.MODE_PRIVATE);

            BufferedWriter objBufferedWriter = new BufferedWriter(new OutputStreamWriter(objFileOutputStream));

            for (int index = 0; index < GameSettings.MAX_COUNT_SCORES && index < lstScores.size(); index++) {

                Player player = lstScores.get(index);

                objBufferedWriter.write(player.toLine());
                objBufferedWriter.newLine();
            }
            objBufferedWriter.close();
            objFileOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**************************************************************************
     * Method
     * addScore()
     * This method add the new player scores to the list
     **************************************************************************/
    public void addScore(Player player) {

        if(lstScores.size() > GameSettings.MAX_COUNT_SCORES) {
            lstScores.remove(GameSettings.MAX_COUNT_SCORES);
        }
        lstScores.add(player);
        rankPlayers();
        writeScores();
    }

    /**************************************************************************
     * Method
     * rankPlayers()
     * This method sorts the list based on the
     * Override comapreTo method in class Player
     **************************************************************************/
    private void rankPlayers() {

        Collections.sort(lstScores);
    }

    /**************************************************************************
     * Method
     * createFileIfNotExists()
     * This method creates the Score File If not exists
     **************************************************************************/
    private void createFileIfNotExists()  {

        File objFileDirs = new File(String.valueOf(context.getFilesDir()));
        File objFile = new File(objFileDirs, GameSettings.FILE_NAME_SCORES);

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

    /**************************************************************************
     * Method
     * getPlayerRank()
     *
     * This method return the Player Rank
     **************************************************************************/
    public int getPlayerRank(Player player) {

        int index = 0;

        for(index = 0; index < lstScores.size(); index++) {

            if(lstScores.get(index).getId().equalsIgnoreCase(player.getId())) {
                return index + 1;
            }
        }

        return index+1;
    }

    /**************************************************************************
     * Method
     * updatePlayer()
     * This method updates the player Name
     **************************************************************************/
    public void updatePlayer(int rank, Player player) {

        Player savedPlayer = lstScores.get(rank - 1);

        if(savedPlayer.getId().equalsIgnoreCase(player.getId())) {

            savedPlayer.setPlayerName(player.getPlayerName());
        }

        writeScores();
    }

    /**************************************************************************
     * Method
     * Getters and Setters
     **************************************************************************/
    public List<Player> getLstScores() {
        return lstScores;
    }

    public void setLstScores(List<Player> lstScores) {
        this.lstScores = lstScores;
    }
}
