package dev.regucorp.coronaboundary.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import dev.regucorp.coronaboundary.R;



public class NotificationSender extends ContextWrapper {

    private static NotificationSender instance;

    private NotificationManager notifManager;

    private static final String CHANNEL_HIGH_ID = "com.infinisoftware.testnotifs.HIGH_CHANNEL";
    private static final String CHANNEL_HIGH_NAME = "High Channel";

    private static final String CHANNEL_DEFAULT_ID = "com.infinisoftware.testnotifs.DEFAULT_CHANNEL";
    private static final String CHANNEL_DEFAULT_NAME = "Default Channel";

    //ID of icon
    private int notifIcon;

    Resources res;

    /**
     * constructor
     * @param base
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationSender(Context base) {
        super( base );
        notifIcon = R.drawable.ic_launcher_foreground;

        init();
    }


    public static NotificationSender getInstance(Context base) {
        if(instance == null) instance = new NotificationSender(base);
        return instance;
    }

    public void setNotifIcon(int icon) {
        notifIcon = icon;
    }


    /**
     * common part of init
     */
    private void init(){
        notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        long [] swPattern = new long[] { 0, 500, 110, 500, 110, 450, 110, 200, 110,
                170, 40, 450, 110, 200, 110, 170, 40, 500 };

        NotificationChannel notificationChannelHigh = new NotificationChannel(
                CHANNEL_HIGH_ID, CHANNEL_HIGH_NAME, notifManager.IMPORTANCE_HIGH );
        notificationChannelHigh.enableLights( true );
        notificationChannelHigh.setLightColor( Color.RED );
        notificationChannelHigh.setShowBadge( true );
        notificationChannelHigh.enableVibration( true );
        notificationChannelHigh.setVibrationPattern( swPattern );
        notificationChannelHigh.setLockscreenVisibility( Notification.VISIBILITY_PUBLIC );
        notifManager.createNotificationChannel( notificationChannelHigh );

        NotificationChannel notificationChannelDefault = new NotificationChannel(
                CHANNEL_DEFAULT_ID, CHANNEL_DEFAULT_NAME, notifManager.IMPORTANCE_DEFAULT );
        notificationChannelDefault.enableLights( true );
        notificationChannelDefault.setLightColor( Color.WHITE );
        notificationChannelDefault.enableVibration( true );
        notificationChannelDefault.setShowBadge( true );

        notifManager.createNotificationChannel( notificationChannelDefault );

        //init resources
        res = getApplicationContext().getResources();
    }







    /**
     * simple notification
     * @param title
     * @param message
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notify(String title, String message ) {
       notify(title,message,1,false);
    }




    /**
     * more complete notification
     * @param title
     * @param message
     * @param id
     * @param prioritary
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notify(String title, String message, int id, boolean prioritary ) {

        String channelId = prioritary ? CHANNEL_HIGH_ID : CHANNEL_DEFAULT_ID;

        Notification notification = new Notification.Builder( getApplicationContext(), channelId )
                .setContentTitle( title )
                .setContentText( message )
                .setSmallIcon(notifIcon)
                .setLargeIcon( BitmapFactory.decodeResource(res, notifIcon) )
                .setAutoCancel( true )
                .build();

        notifManager.notify( id, notification );
    }

    public void delete(int id){
        notifManager.cancel(id);
    }

}
