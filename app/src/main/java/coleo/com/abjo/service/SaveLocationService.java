package coleo.com.abjo.service;

import android.Manifest;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.TravelDataBase;
import coleo.com.abjo.data_base.UserLocation;
import coleo.com.abjo.data_base.locationRepository;

import static coleo.com.abjo.constants.Constants.isPause;
import static coleo.com.abjo.constants.Constants.pause_resume;
import static coleo.com.abjo.constants.Constants.start_stop;
import static coleo.com.abjo.constants.Constants.stepCounted;

public class SaveLocationService extends Service implements SensorEventListener {

//    private static final String LOG_TAG = "ForegroundService";

    private boolean isStep = false;

    private locationRepository repository;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private LocationManager locationManager;
    public Location previousBestLocation = null;
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (isPause) {
                if (isBetterLocation(location, previousBestLocation)) {
                    repository.insert(new UserLocation(location.getLatitude(), location.getLongitude(),
                            "" + System.currentTimeMillis()));
                }
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {

        if (isStep) {
            startStepCounting();
        }

        startLocationWithNetwork();

        super.onCreate();
    }

    private void startStepCounting() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void startLocationWithNetwork() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 5, locationListener);
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else return isNewer && !isSignificantlyLessAccurate && isFromSameProvider;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
        if (isStep) {
            Constants.start_stop.setText("" + stepCounted);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private NotificationCompat.Builder notification = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Constants.saveLastAction(getBaseContext(), intent.getAction());
            switch (Objects.requireNonNull(intent.getAction())) {
                case Constants.ACTION.START_FOREGROUND_ACTION_STEP: {
//                    Log.i(LOG_TAG, "Received Start Foreground Intent step");

                    notification = Constants.showNotification("STEP", "we are calculating your steps"
                            , this, true, false, true, false);
                    startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                            notification.build());
                    startService();
                    isStep = true;
                    break;
                }
                case Constants.ACTION.START_FOREGROUND_ACTION_BIKE: {
//                    Log.i(LOG_TAG, "Received Start Foreground Intent bike");
                    notification = Constants.showNotification("BIKE", "we are calculating your cycle"
                            , this, true, false, false, false);
                    startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                            notification.build());
                    startService();
                    isStep = false;
                    break;
                }
                case Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP: {
//                    Log.i(LOG_TAG, "Received pause Foreground Intent step");

                    Constants.updateNotification(notification, "wow", "we are calculating your steps"
                            , this, true, true);
                    pauseService();
                    break;
                }
                case Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE: {
//                    Log.i(LOG_TAG, "Received pause Foreground Intent ");

                    Constants.updateNotification(notification, "wow", "we are calculating your steps"
                            , this, true, false);
                    pauseService();
                    break;
                }
                case Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP: {
//                    Log.i(LOG_TAG, "Received resume Foreground Intent ");

                    Constants.updateNotification(notification, "wow", "we are calculating your steps"
                            , this, false, true);
                    resumeService();
                    break;
                }
                case Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE: {
//                    Log.i(LOG_TAG, "Received resume Foreground Intent ");

                    Constants.updateNotification(notification, "wow", "we are calculating your steps"
                            , this, false, false);
                    resumeService();

                    break;
                }
                case Constants.ACTION.STOP_FOREGROUND_ACTION:
//                    Log.i(LOG_TAG, "Received Stop Foreground Intent");
                    stopForeground(true);
                    stopService();
                    stopSelf();
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] > Constants.lastCount + 10) {
            float temp = event.values[0] - Constants.lastCount;
            Constants.stepCounted += temp;
            Constants.lastCount = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void startService() {
        makeDataBase();
        start_stop.setText(getBaseContext().getString(R.string.stop_service));
        pause_resume.setVisibility(View.VISIBLE);
        Constants.isWorking = true;
        Constants.isPause = false;
    }

    private void stopService() {
        start_stop.setText(getBaseContext().getString(R.string.start_service));
        pause_resume.setVisibility(View.INVISIBLE);
        Constants.isWorking = false;
        Constants.isPause = false;
        //todo make json and send to server
    }

    private void pauseService() {
        pause_resume.setText(getBaseContext().getString(R.string.resume_service));
        Constants.isWorking = true;
        Constants.isPause = true;
    }

    private void resumeService() {
        pause_resume.setText(getBaseContext().getString(R.string.pause_service));
        Constants.isPause = false;
        Constants.isWorking = true;
    }

    private void makeDataBase() {
        TravelDataBase dataBase = Room.databaseBuilder(getApplicationContext(),
                TravelDataBase.class, "database-name").build();
        repository = new locationRepository(dataBase);
        repository.nukeTable();
    }

}

