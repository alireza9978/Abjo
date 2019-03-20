package coleo.com.abjo;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import androidx.work.OneTimeWorkRequest;

public class MainActivity extends AppCompatActivity
//        implements SensorEventListener
{

    private TextView count;

    private OneTimeWorkRequest request;

    private SensorManager sensorManager;
    boolean activityRunning;
    private BroadcastReceiver broadcastReceiver;
    private static final String TAG = "mainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.count = findViewById(R.id.count_step);
        Constants.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, StepCountingService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, StepCountingService.class);
                stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                startService(stopIntent);
            }
        });
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // call updateUI passing in our intent which is holding the data to display.
////                Constants.showNotification("help", "never mind", getApplicationContext(),
////                        null, true, true);
//
//            }
//        };

//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        Log.i("cityWorker", "doWork: ");
//        activityRunning = true;
//        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        if (countSensor != null) {
//            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
//        } else {
//            Log.i(TAG, "CityWorker: ");
//        }

//        WorkManager.getInstance().cancelAllWork();
//        request = new OneTimeWorkRequest.Builder(CityWorker.class).setInitialDelay(30_000, TimeUnit.MILLISECONDS).build();
//        WorkManager.getInstance().enqueue(request);


    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        Constants.count.setText(String.valueOf(event.values[0]));
//        if (event.values[0] > Constants.lastCount + 10) {
//            float temp = event.values[0] - Constants.lastCount;
//            Constants.showNotification("wow","you step about " + temp
//                    ,this,null,true,true);
//            Constants.lastCount = event.values[0];
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//    }


}
