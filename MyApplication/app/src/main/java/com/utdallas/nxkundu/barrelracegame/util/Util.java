package com.utdallas.nxkundu.barrelracegame.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nxkundu on 4/21/18.
 */

public class Util {

    public Util() {
        super();
    }

    public static String getUserReadableTime(long time) {

        int secs = (int) (time / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (time % 1000);

        String userReadableTime = mins + ":" + String.format("%02d", secs) + "." + String.format("%03d", milliseconds);

        return userReadableTime;
    }

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
