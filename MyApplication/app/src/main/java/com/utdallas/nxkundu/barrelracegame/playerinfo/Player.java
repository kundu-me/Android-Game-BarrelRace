package com.utdallas.nxkundu.barrelracegame.playerinfo;

import com.utdallas.nxkundu.barrelracegame.util.Util;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by nxkundu on 4/21/18.
 */

public class Player implements Serializable, Comparable<Player>{

    public static final String DEFAULT_PLAYER_NAME = "Player";
    private int rank;
    private String score;
    private String playerName;
    private String playerDate;

    public Player(String score) {
        super();

        this.score = score;
        this.playerName = DEFAULT_PLAYER_NAME;
        this.playerDate = Util.getDate();
    }


    public Player(String score, String playerName, String playerDate) {
        super();

        this.score = score;
        this.playerName = playerName;
        this.playerDate = playerDate;
    }

    @Override
    public int compareTo(Player obj) {
        return (obj.score).compareTo(this.score);
    }

    @Override
    public String toString() {
        return  rank + "\t" + score + "\t" + playerName;
    }

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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
