package dev.regucorp.coronaboundary.time;


/**
 * listener to make methods at specific time
 */
public interface TimeListener {
    /**
     * called each time a second pass
     */
    public void onNewSecond();

    /**
     * called each time a minute pass
     */
    public void onNewMinute();

    /**
     * called each time a hour pass
     */
    public void onNewHour();
}
