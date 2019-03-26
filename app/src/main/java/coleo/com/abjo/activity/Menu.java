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
    private String lastAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lastAction = Constants.getLastAction(this);

        Bundle extra = getIntent().getExtras();
        fromNotification = Objects.requireNonNull(extra).getBoolean(Constants.FROM_NOTIFICATION, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.checkPermission(this);
        Constants.checkLocation(this);
        lastAction = Constants.getLastAction(this);
        if (fromNotification) {
            Intent intent = new Intent(this, CountActivity.class);
            //age az notification biad hatman last action == stop nabode
            startActivity(intent);
            fromNotification = false;
        } else {
            if (!lastAction.equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
                findViewById(R.id.start_bycicle_button_id).setVisibility(View.INVISIBLE);
                findViewById(R.id.start_step_button_id).setVisibility(View.INVISIBLE);
                findViewById(R.id.continue_last_action_id).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.start_bycicle_button_id).setVisibility(View.VISIBLE);
                findViewById(R.id.start_step_button_id).setVisibility(View.VISIBLE);
                findViewById(R.id.continue_last_action_id).setVisibility(View.INVISIBLE);
            }
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
                intent.putExtra(Constants.STEP_OR_BIKE, true);
                startActivity(intent);
                break;
            }
            case R.id.start_bycicle_button_id: {
                Log.i(TAG, "onClick: bike");
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE, false);
                startActivity(intent);
                break;
            }
            case R.id.continue_last_action_id: {
                Intent intent = new Intent(this, CountActivity.class);
//                intent.putExtra(Constants.STEP_OR_BIKE, Constants.isActionKindStep(lastAction));
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
