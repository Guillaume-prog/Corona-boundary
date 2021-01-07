package dev.regucorp.coronaboundary.time;

import java.util.Date;

/**
 * Class to manipulate timestamp.
 * Use the Date class, with some deprecate methods, but that seems very useful
 *
 * @author Arthur VIGOUROUX - alias Arthuino
 * @version 1.1
 * the 1.1 version add setters for hours, minutes and seconds
 */
public class Timestamp {

    //instance variable
    private Date timestampDate;

    /**
     * Constructor for Timestamp
     * @param hours
     * @param minutes
     * @param seconds
     */
    public Timestamp(int hours, int minutes, int seconds){

        timestampDate = new Date(0,0,0,hours,minutes,seconds);
    }

    /**
     * getter for seconds
     * @return seconds of the timestamp
     */
    public int getSeconds() {
        return timestampDate.getSeconds();
    }

    /**
     * getter for minutes
     * @return minutes of the timestamps
     */
    public int getMinutes() {
        return timestampDate.getMinutes();
    }


    /**
     * getter for hours
     * @return hours of the timestamps
     */
    public int getHours() {
        return timestampDate.getHours();
    }


    /*  Incrementer for instances variables */

    /**
     * Incrementer for seconds
     * @param dSeconds
     */
    public void addSeconds(int dSeconds){
        long time = timestampDate.getTime();
        Date dTime = new Date(0,0,0,0,0,dSeconds);
        timestampDate.setTime(time+dTime.getTime());
    }

    /**
     * Incrementer for minutes
     * @param dMinutes
     */
    public void addMinutes(int dMinutes){
        long time = timestampDate.getTime();
        Date dTime = new Date(0,0,0,0,dMinutes,0);
        timestampDate.setTime(time+dTime.getTime());
    }

    /**
     * Incrementer for hours
     * @param dHours
     */
    public void addHours(int dHours){
        long time = timestampDate.getTime();
        Date dTime = new Date(0,0,0,dHours,0,0);
        timestampDate.setTime(time+dTime.getTime());
    }


    public void setHours(int hours){
        timestampDate.setHours(hours);
    }

    public void setMinutes(int minutes){
        timestampDate.setMinutes(minutes);
    }

    public void setSeconds(int seconds){
        timestampDate.setSeconds(seconds);
    }

}
