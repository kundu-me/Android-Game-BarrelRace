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
import java.util.TreeMap;

/**
 * Created by nxkundu on 4/21/18.
 */

public class Score {

    private List<Player> lstScores;

    private static Score score;

    private Context context = null;

    private Score(Context context) {
        super();

        this.context = context;
        lstScores = new ArrayList<>();

        createFileIfNotExists();
        readScores();
    }

    public static Score getInstance(Context context) {

        if(score == null) {
            score = new Score(context);
        }

        return score;
    }

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

    private void writeScores() {

        try {

            FileOutputStream objFileOutputStream = context.openFileOutput(GameSettings.FILE_NAME_SCORES, Context.MODE_PRIVATE);

            BufferedWriter objBufferedWriter = new BufferedWriter(new OutputStreamWriter(objFileOutputStream));

            for (Player player : lstScores) {

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

    public void addScore(Player player) {

        lstScores.add(player);
        rankPlayers();
        writeScores();
    }

    private void rankPlayers() {

        Collections.sort(lstScores);

        int rank = 1;

        for(Player player : lstScores) {

            player.setRank(rank);
            rank ++;
        }
    }

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

    public List<Player> getLstScores() {
        return lstScores;
    }

    public void setLstScores(List<Player> lstScores) {
        this.lstScores = lstScores;
    }
}
