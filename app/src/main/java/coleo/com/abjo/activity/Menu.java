package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Objects;

import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "menu";
    private boolean fromNotification = false;
    private boolean fromStep = false;
    private boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extra = getIntent().getExtras();
        fromNotification = Objects.requireNonNull(extra).getBoolean(Constants.FROM_NOTIFICATION, false);
        if (fromNotification) {
            fromStep = extra.getBoolean(Constants.STEP_OR_BIKE, false);
            isPause = extra.getBoolean(Constants.IS_PAUSE, false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.checkPermission(this);
        Constants.checkLocation(this);
        if (fromNotification) {
            Intent intent = new Intent(this, CountActivity.class);
            intent.putExtra(Constants.FROM_NOTIFICATION,true);
            intent.putExtra(Constants.IS_PAUSE,isPause);
            intent.putExtra(Constants.STEP_OR_BIKE, fromStep);
            startActivity(intent);
            fromNotification = false;
        }
    }

    public void noPermission() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_step_button_id: {
                Log.i(TAG, "onClick: step");
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.FROM_NOTIFICATION,false);
                intent.putExtra(Constants.STEP_OR_BIKE, true);
                startActivity(intent);
                break;
            }
            case R.id.start_bycicle_button_id: {
                Log.i(TAG, "onClick: bike");
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.FROM_NOTIFICATION,false);
                intent.putExtra(Constants.STEP_OR_BIKE, false);
                startActivity(intent);
                break;
            }
            case R.id.leader_board_button_id: {
                Log.i(TAG, "onClick: leader");
                break;
            }
        }
    }
}
