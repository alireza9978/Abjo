package coleo.com.abjo.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Objects;

import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.UserLocation;
import coleo.com.abjo.data_base.locationRepository;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

import static coleo.com.abjo.constants.Constants.context;
import static coleo.com.abjo.constants.Constants.showNotification;

public class SaverService extends Service {

    private static int id = 1;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationCompat.Builder builder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            if (action.equals(Constants.ACTION.START_FOREGROUND_ACTION_BIKE)) {
                builder = showNotification("fuck android", "please don't close my app", context, false, false, false, false);
                startForeground(id, builder.build());
                startJob();
            }
            if (action.equals((Constants.ACTION.STOP_FOREGROUND_ACTION))) {
                SmartLocation.with(context).location().stop();
                stopForeground(true);
                stopSelf(id);
            }
        }
        return START_STICKY;
    }

    private void startJob() {
        SmartLocation.with(context).location().config(LocationParams.NAVIGATION).continuous()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Log.i("Location", "onLocationUpdated: ");
                        Objects.requireNonNull(locationRepository.get(null))
                                .insert(UserLocation.makeDifference(location,
                                        "" + System.currentTimeMillis(), 0));
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(context, SaverReceiver.class);
        context.startService(intent);
    }
}
