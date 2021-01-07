package dev.regucorp.coronaboundary.position;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by Guillaume ROUSSIN on 12/26/20
 */
class LocationService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void start() {

    }

    public void stop() {

    }
}
