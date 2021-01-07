package dev.regucorp.coronaboundary.time;

import android.os.SystemClock;
import android.widget.Chronometer;

/**
 *  Time management with the Timestamps and the chronometer widget
 *
 *  @author Arthur VIGOUROUX - alias Arthuino
 *  @version 1.0
 */
public class TimeHandler {

    //instance variables
    private Timestamp timestampCurrent;
    private TimeListener timeListener;
    private Chronometer chronometer;

    //debug variables
    private long minToMili = 60000;
    private long secToMili = 1000;


    /**
     * Constructor of TimeHandler
     * @param _chronometer
     * @param _timeListener
     */
    public TimeHandler(Chronometer _chronometer, TimeListener _timeListener){

        //init instance variables
        this.timeListener = _timeListener;
        this.chronometer = _chronometer;
        timestampCurrent = getTimeWithChrono(chronometer.getText());

        //init chronometer listener and set time Listener
        this.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Timestamp timestamp = getTimeWithChrono(chronometer.getText());

                ///  for seconds
                timeListener.onNewSecond(); //call the listener for seconds
                timestampCurrent.setSeconds(timestamp.getSeconds()); //update the seconds

                // for minutes
                if(timestamp.getMinutes()!=timestampCurrent.getMinutes()){  //only if minutes changed
                    timeListener.onNewMinute(); //call the listener for minutes
                    timestampCurrent.setMinutes(timestamp.getMinutes()); //update the minutes
                }

                // for hours
                if (timestamp.getHours() != timestampCurrent.getHours()){ //only if hours changed
                    timeListener.onNewHour(); //call the listener for hours
                    timestampCurrent.setHours(timestamp.getHours()); //update the hours
                }

            }
        });

    }


    /**
     * give the time before timestamp
     * @param targetTime
     * @return
     */
    public Timestamp timeUntil(Timestamp targetTime){

        targetTime.addSeconds(timestampCurrent.getSeconds()*-1);
        targetTime.addMinutes(timestampCurrent.getMinutes()*-1);
        targetTime.addHours(timestampCurrent.getHours()*-1);

        return targetTime;
    }


    /**
     * reset and start timer
     */
    public void startTimer(){
        chronometer.setBase(SystemClock.elapsedRealtime()); //reset chronometer

        //reset timestamp
        timestampCurrent.setHours(0);
        timestampCurrent.setMinutes(0);
        timestampCurrent.setHours(0);
        //start chronometer
        chronometer.start();
    }

    /**
     * stop timer
     */
    public void stopTimer(){
        chronometer.stop();
    }


    /**
     * transform charSequence given by the getText method of the chronometer class
     * @param charSequence
     * @return timestamp of the time given on param
     *
     * TODO : actually, if the hours is more than 9, it crash. Easy to fix but useless for corona boundary
     */
    private Timestamp getTimeWithChrono(CharSequence charSequence){
        int hours;
        int minutes;
        int seconds;

        Timestamp tempTimestamp = new Timestamp(0,0,0);

        String text = String.valueOf(charSequence);

        int lenght = text.length();
        boolean withHours=false;
        if(lenght>5){
            withHours=true;
        }

        if(withHours==true){
            String hourString = text.substring(0,1);
            hours = Integer.parseInt(hourString);
            tempTimestamp.setHours(hours);

            String minutesString = text.substring(2,4);
            minutes = Integer.parseInt(minutesString);
            tempTimestamp.setMinutes(minutes);

            String secondString = text.substring(5,7);
            seconds = Integer.parseInt(secondString);
            tempTimestamp.setSeconds(seconds);
        }else {
            tempTimestamp.setHours(0);
            String minutesString = text.substring(0,2);
            minutes = Integer.parseInt(minutesString);
            tempTimestamp.setMinutes(minutes);

            String secondString = text.substring(3,5);
            seconds = Integer.parseInt(secondString);
            tempTimestamp.setSeconds(seconds);
        }
        return tempTimestamp;
    }


}
