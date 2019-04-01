package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.service.SaveLocationService;

import static coleo.com.abjo.constants.Constants.pause_resume;
import static coleo.com.abjo.constants.Constants.start_stop;

public class CountActivity extends AppCompatActivity {

//    private static final String TAG = "mainActivity";

    private String startActionKind = "";
    private String pauseActionKind = "";
    private String resumeActionKind = "";
    private String lastAction = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        Constants.start_stop = findViewById(R.id.count_step_start_stop_button_id);
        Constants.pause_resume = findViewById(R.id.count_step_pause_button_id);
        TextView activityKind = findViewById(R.id.activity_kind_textView_id);

        lastAction = Constants.getLastAction(this);
        boolean isStep = !lastAction.equals(Constants.ACTION.STOP_FOREGROUND_ACTION) ?
                Constants.isActionKindStep(lastAction) :
                Objects.requireNonNull(getIntent().getExtras()).getBoolean(Constants.STEP_OR_BIKE, false);

        if (isStep) {
            activityKind.setText(getString(R.string.step_activity_name));
            startActionKind = Constants.ACTION.START_FOREGROUND_ACTION_STEP;
            pauseActionKind = Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP;
            resumeActionKind = Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP;
        } else {
            activityKind.setText(getString(R.string.bike_activity_name));
            startActionKind = Constants.ACTION.START_FOREGROUND_ACTION_BIKE;
            pauseActionKind = Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE;
            resumeActionKind = Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE;
        }

        manageButton();

        Constants.start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isWorking) {
                    stopService();
                } else {
                    startService();
                }
            }
        });

        Constants.pause_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isPause) {
                    resumeService();
                } else {
                    pauseService();
                }
            }
        });

    }

    private void startService() {
        Intent startIntent = new Intent(CountActivity.this, SaveLocationService.class);
        startIntent.setAction(startActionKind);
        startService(startIntent);
    }

    private void stopService() {
        Intent stopIntent = new Intent(CountActivity.this, SaveLocationService.class);
        stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        startService(stopIntent);
    }

    private void pauseService() {
        Intent pauseIntent = new Intent(CountActivity.this, SaveLocationService.class);
        pauseIntent.setAction(pauseActionKind);
        startService(pauseIntent);
    }

    private void resumeService() {
        Intent resumeIntent = new Intent(CountActivity.this, SaveLocationService.class);
        resumeIntent.setAction(resumeActionKind);
        startService(resumeIntent);
    }

    private void manageButton() {
        if (lastAction.equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            start_stop.setText(getString(R.string.start_service));
            pause_resume.setVisibility(View.INVISIBLE);
        } else {
            start_stop.setText(getString(R.string.stop_service));
            pause_resume.setVisibility(View.VISIBLE);
            if (lastAction.equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE) ||
                    lastAction.equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP)) {
                pause_resume.setText(getString(R.string.pause_service));
            }
            if (lastAction.equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE) ||
                    lastAction.equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP)) {
                pause_resume.setText(getString(R.string.resume_service));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, OperationActivity.class);
        intent.putExtra(Constants.FROM_NOTIFICATION, false);
        intent.putExtra(Constants.LAST_ACTION_INTENT, Constants.getLastAction(this));
        startActivity(intent);
        finish();
    }
}
