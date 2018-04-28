package com.utdallas.nxkundu.barrelracegame.playerinfo;

import com.utdallas.nxkundu.barrelracegame.util.Util;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by nxkundu on 4/21/18.
 */
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This Class contains the Player Object
 * Which we save when a Player wins and is within the Top 10 Rank
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

public class Player implements Serializable, Comparable<Player>{

    public static final String DEFAULT_PLAYER_NAME = "Player";
    private String id;
    private String score;
    private String playerName;
    private String playerDate;

    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    public Player(String score) {
        super();

        this.id = UUID.randomUUID().toString();
        this.score = score;
        this.playerName = DEFAULT_PLAYER_NAME;
        this.playerDate = Util.getDate();
    }


    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    public Player(String score, String playerName, String playerDate) {
        super();

        this.id = UUID.randomUUID().toString();
        this.score = score;
        this.playerName = playerName;
        this.playerDate = playerDate;
    }

    /**************************************************************************
     * Method
     *
     **************************************************************************/
    @Override
    public int compareTo(Player obj) {
        return (this.score).compareTo(obj.score);
    }

    /**************************************************************************
     * Method
     *
     **************************************************************************/
    @Override
    public String toString() {
        return  score + "\t" + playerName;
    }

    /**************************************************************************
     * Method
     * Getters and Setters
     **************************************************************************/
    public String toLine() {
        return score + "\t" + playerName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerDate() {
        return playerDate;
    }

    public void setPlayerDate(String playerDate) {
        this.playerDate = playerDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
