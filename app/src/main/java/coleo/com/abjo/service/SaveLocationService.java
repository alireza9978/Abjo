package coleo.com.abjo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.mrq.android.ibrary.FinalCountDownTimer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.TravelDataBase;
import coleo.com.abjo.data_base.UserLocation;
import coleo.com.abjo.data_base.locationRepository;
import im.delight.android.location.SimpleLocation;

import static coleo.com.abjo.constants.Constants.context;
import static coleo.com.abjo.constants.Constants.getLastAction;
import static coleo.com.abjo.constants.Constants.pause_resume;
import static coleo.com.abjo.constants.Constants.start_stop;

public class SaveLocationService extends Service implements SensorEventListener {

    private static final String TAG = "ForegroundService";

    private boolean isStep = false;

    private locationRepository repository;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private LocationManager locationManager;
    private SimpleLocation location;

    public boolean isStep() {
        return isStep;
    }

    private void writeToFile(String data, Context context) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(path, "secondMode" + System.currentTimeMillis() + ".txt");
            FileOutputStream fOut = new FileOutputStream(myFile, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {

//        if (isStep) {
//            startStepCounting();
//        }
        super.onCreate();
    }

    private void startAction() {
        if (isStep()) {
            Log.i(TAG, "startAction: ");
            location = new SimpleLocation(getApplicationContext(), true, false, 2000, true);
            location.setListener(new SimpleLocation.Listener() {
                @Override
                public void onPositionChanged() {
                    Log.i(TAG, "onPositionChanged: SimpleLocation");
                    repository.insert(UserLocation.makeDifference(location.getLatitude(),
                            location.getLongitude(), "" + System.currentTimeMillis(), 6));
                }
            });
            location.beginUpdates();
        }
    }

    private void startStepCounting() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (location != null) {
            location.endUpdates();
        }
//        if (isStep) {
//            Constants.start_stop.setText("" + stepCounted);
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

    private NotificationCompat.Builder notification = null;

    private void manageButton() {
        String lastAction = getLastAction();
        start_stop.setVisibility(View.VISIBLE);
        start_stop.setImageResource(R.mipmap.stop_icon);
        if (lastAction.equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE) ||
                lastAction.equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP)) {
            pause_resume.setImageResource(R.mipmap.pause_icon);
        }
        if (lastAction.equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE) ||
                lastAction.equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP)) {
            pause_resume.setImageResource(R.mipmap.play_icon);
        }
        if (lastAction.equals(Constants.ACTION.START_FOREGROUND_ACTION_BIKE)
                || lastAction.equals(Constants.ACTION.START_FOREGROUND_ACTION_STEP)) {
            pause_resume.setImageResource(R.mipmap.pause_icon);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (!Objects.requireNonNull(intent.getAction()).equals(Constants.ACTION.UPDATE_FOREGROUND_ACTION))
                Constants.saveLastAction(getBaseContext(), intent.getAction());
            switch (Objects.requireNonNull(intent.getAction())) {
                case Constants.ACTION.UPDATE_FOREGROUND_ACTION: {
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer = FinalCountDownTimer.createDefault(Constants.ONE_HOUR, OnCount.getNew());
                    } else {
                        countDownTimer = FinalCountDownTimer.createDefault(Constants.ONE_HOUR, OnCount.getNew());
                    }
                    if (!getLastAction().equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE) &&
                            !getLastAction().equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP))
                        countDownTimer.start();

                    break;
                }
                case Constants.ACTION.START_FOREGROUND_ACTION_STEP: {
//                    Log.i(LOG_TAG, "Received Start Foreground Intent step");

                    notification = Constants.showNotification("STEP", "we are calculating your steps"
                            , this, true, false, true, false);
                    startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                            notification.build());
                    isStep = true;
                    startService();
                    break;
                }
                case Constants.ACTION.START_FOREGROUND_ACTION_BIKE: {
//                    Log.i(LOG_TAG, "Received Start Foreground Intent bike");
                    notification = Constants.showNotification("BIKE", "we are calculating your cycle"
                            , this, true, false, false, false);
                    startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                            notification.build());
                    isStep = false;
                    startService();
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
        manageButton();
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
    public FinalCountDownTimer countDownTimer;

    private void startService() {
        makeDataBase();
        countDownTimer = FinalCountDownTimer.createDefault(Constants.ONE_HOUR, new OnCount(0, 0, 0, this));
        countDownTimer.start();
        start_stop.setImageResource(R.mipmap.stop_icon);
        pause_resume.setVisibility(View.VISIBLE);
        Constants.isWorking = true;
        Constants.isPause = false;
        startAction();
    }

    private void stopService() {
        Constants.isWorking = false;
        Constants.isPause = false;
        countDownTimer.cancel();
        repository.makeJsonAndSend();
        if (location != null) {
            location.endUpdates();
        }
        ((MainActivity) context).backToMain();
    }

    private void pauseService() {
        pause_resume.setImageResource(R.mipmap.play_icon);
        Constants.isWorking = true;
        Constants.isPause = true;
        countDownTimer.cancel();
        pause_resume.setEnabled(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                pause_resume.setEnabled(true);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private void resumeService() {
        pause_resume.setImageResource(R.mipmap.pause_icon);
        Constants.isPause = false;
        Constants.isWorking = true;
        countDownTimer.start();
    }

    private void makeDataBase() {
        TravelDataBase dataBase = Room.databaseBuilder(getApplicationContext(),
                TravelDataBase.class, "database-name").build();
        repository = new locationRepository(dataBase);
        repository.nukeTable();
    }

    public locationRepository getRepository() {
        return repository;
    }
}

