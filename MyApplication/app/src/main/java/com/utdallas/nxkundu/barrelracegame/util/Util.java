package com.utdallas.nxkundu.barrelracegame.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nxkundu on 4/21/18.
 */
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This class
 * provides all the Utility Functions
 * Which are commonly used by multiple classes
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/
public class Util {

    /**************************************************************************
     * Constructor
     *
     **************************************************************************/
    public Util() {
        super();
    }

    /**************************************************************************
     * Method
     * getUserReadableTime()
     *
     * This method converts the long time in millisec to
     * human readable time
     *
     **************************************************************************/
    public static String getUserReadableTime(long time) {

        int secs = (int) (time / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (time % 1000);

        String userReadableTime = mins + ":" + String.format("%02d", secs) + "." + String.format("%03d", milliseconds);

        return userReadableTime;
    }

    /**************************************************************************
     * Method
     * getDate()
     *
     * This method returns the current Date
     * in Human Readable format to save in file
     * when the user played the game
     * for future improvement
     * this is not shown to the user
     * so, localisation is not implemented
     *
     **************************************************************************/
    public static String getDate() {

        String strDate = "";

        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date today = new Date();

        try {

            Date date = formatter.parse(formatter.format(today));
            strDate = date.toString();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strDate;
    }
}
