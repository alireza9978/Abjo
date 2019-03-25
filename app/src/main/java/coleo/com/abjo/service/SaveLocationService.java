package coleo.com.abjo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Objects;

import coleo.com.abjo.constants.Constants;

public class SaveLocationService extends Service implements SensorEventListener {

    private static final String LOG_TAG = "ForegroundService";
    private String TAG = "counter";
    private SensorManager sensorManager;

    @Override
    public void onCreate() {
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        Log.i("cityWorker", "doWork: ");
//        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        if (countSensor != null) {
//            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
//        } else {
//            Log.i(TAG, "CityWorker: ");
//        }
        //todo save location and send to server
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged: " + location.toString());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private NotificationCompat.Builder notification = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //todo save last action
        switch (Objects.requireNonNull(intent.getAction())) {
            case Constants.ACTION.START_FOREGROUND_ACTION_STEP: {
                Log.i(LOG_TAG, "Received Start Foreground Intent step");
                notification = Constants.showNotification("wow", "we are calculating your steps"
                        , this, true, false, true, false);
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification.build());

                break;
            }
            case Constants.ACTION.START_FOREGROUND_ACTION_BIKE: {
                Log.i(LOG_TAG, "Received Start Foreground Intent bike");
                notification = Constants.showNotification("wow", "we are calculating your steps"
                        , this, true, false, false, false);
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification.build());

                break;
            }
            case Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP: {
                Log.i(LOG_TAG, "Received pause Foreground Intent step");

                Constants.updateNotification(notification, "wow", "we are calculating your steps"
                        , this, true,true);
                break;
            }
            case Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE: {
                Log.i(LOG_TAG, "Received pause Foreground Intent ");

                Constants.updateNotification(notification, "wow", "we are calculating your steps"
                        , this, true,false);
                break;
            }
            case Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP: {
                Log.i(LOG_TAG, "Received resume Foreground Intent ");

                Constants.updateNotification(notification, "wow", "we are calculating your steps"
                        , this, false,true);
                break;
            }
            case Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE: {
                Log.i(LOG_TAG, "Received resume Foreground Intent ");

                Constants.updateNotification(notification, "wow", "we are calculating your steps"
                        , this, false,false);
                break;
            }
            case Constants.ACTION.STOP_FOREGROUND_ACTION:
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
                break;
        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Constants.count.setText(String.valueOf(event.values[0]));
        if (event.values[0] > Constants.lastCount + 10) {
            float temp = event.values[0] - Constants.lastCount;
            //todo send request to server or save in phone
            Constants.lastCount = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

