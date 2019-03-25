package coleo.com.abjo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CountActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";

    private Button start;
    private Button pause;
    private TextView activityKind;
    private boolean isWorking = false;
    private boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        start = findViewById(R.id.count_step_start_stop_button_id);
        pause = findViewById(R.id.count_step_pause_button_id);
        activityKind = findViewById(R.id.activity_kind_textView_id);

        Bundle extra = getIntent().getExtras();
        boolean isStep = extra.getBoolean(Constants.STEP_OR_BIKE,false);
        if (isStep){
            activityKind.setText("Step count");
        }else {
            activityKind.setText("bike activity");
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWorking) {
                    startService();
                } else {
                    stopService();
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
//        Intent startIntent = new Intent(CountActivity.this, SaveLocationService.class);
//        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//        startService(startIntent);
        pause.setVisibility(View.VISIBLE);
        isWorking = true;
    }

    private void stopService() {
        start.setText("start");
//        Intent stopIntent = new Intent(CountActivity.this, SaveLocationService.class);
//        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
//        startService(stopIntent);
        pause.setVisibility(View.INVISIBLE);
        isWorking = false;
    }

    private void pauseService(){
        pause.setText("resume");
        isPause = true;
    }

    private void resumeService(){
        pause.setText("pause");
        isPause = false;
    }

}
