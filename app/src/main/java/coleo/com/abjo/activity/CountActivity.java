package coleo.com.abjo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import coleo.com.abjo.R;
import coleo.com.abjo.service.SaveLocationService;
import coleo.com.abjo.constants.Constants;

public class CountActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";

    private Button start;
    private Button pause;
    private TextView activityKind;
    private boolean isWorking = false;
    private boolean isPause = false;
    private String startActionKind = "";
    private String pauseActionKind = "";
    private String resumeActionKind = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        //todo add text view to constant to change with notification
        start = findViewById(R.id.count_step_start_stop_button_id);
        pause = findViewById(R.id.count_step_pause_button_id);
        activityKind = findViewById(R.id.activity_kind_textView_id);

        Bundle extra = getIntent().getExtras();
        boolean isStep = extra.getBoolean(Constants.STEP_OR_BIKE, false);
        boolean fromNotification = extra.getBoolean(Constants.FROM_NOTIFICATION, false);
        if (fromNotification)
            isPause = extra.getBoolean(Constants.FROM_NOTIFICATION, false);

        if (isStep) {
            activityKind.setText("Step count");
            startActionKind = Constants.ACTION.START_FOREGROUND_ACTION_STEP;
            pauseActionKind = Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP;
            resumeActionKind = Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP;
        } else {
            activityKind.setText("bike activity");
            startActionKind = Constants.ACTION.START_FOREGROUND_ACTION_BIKE;
            pauseActionKind = Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE;
            resumeActionKind = Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE;
        }
        if (fromNotification) {
            start.setText("stop");
            isWorking = true;
            if (isPause) {
                pause.setText("resume");
            }
        } else {
            pause.setVisibility(View.INVISIBLE);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWorking) {
                    stopService();
                } else {
                    startService();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    resumeService();
                } else {
                    pauseService();
                }
            }
        });

    }

    private void startService() {
        start.setText("stop");
        Intent startIntent = new Intent(CountActivity.this, SaveLocationService.class);
        startIntent.setAction(startActionKind);
        startService(startIntent);
        pause.setVisibility(View.VISIBLE);
        isWorking = true;
    }

    private void stopService() {
        start.setText("start");
        Intent stopIntent = new Intent(CountActivity.this, SaveLocationService.class);
        stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        startService(stopIntent);
        pause.setVisibility(View.INVISIBLE);
        isWorking = false;
    }

    private void pauseService() {
        pause.setText("resume");
        Intent pauseIntent = new Intent(CountActivity.this, SaveLocationService.class);
        pauseIntent.setAction(pauseActionKind);
        startService(pauseIntent);
        isPause = true;
    }

    private void resumeService() {
        Intent resumeIntent = new Intent(CountActivity.this, SaveLocationService.class);
        resumeIntent.setAction(resumeActionKind);
        startService(resumeIntent);
        pause.setText("pause");
        isPause = false;
    }

}
