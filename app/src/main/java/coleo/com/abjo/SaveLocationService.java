package coleo.com.abjo;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.Objects;

import coleo.com.abjo.constants.Constants;

public class SaveLocationService extends Service implements SensorEventListener {

    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private static final String LOG_TAG = "ForegroundService";
    private String TAG = "counter";
    private SensorManager sensorManager;

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Log.i("cityWorker", "doWork: ");
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.i(TAG, "CityWorker: ");
        }

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (Objects.requireNonNull(intent.getAction())) {
            case Constants.ACTION.STARTFOREGROUND_ACTION:
                Log.i(LOG_TAG, "Received Start Foreground Intent ");

                Notification notification = Constants.showNotification("wow", "we are calculating your steps"
                        , this, null, true, false);
                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                        notification);

                break;
            case Constants.ACTION.STOPFOREGROUND_ACTION:
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

