package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class OperationActivity extends AppCompatActivity implements View.OnClickListener {

    //    private static final String TAG = "menu";
    private String lastAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        lastAction = Constants.getLastAction(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.checkPermission(this);
        Constants.checkLocation(this);
        lastAction = Constants.getLastAction(this);

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

    public void noPermission() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_step_button_id: {
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE, true);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.start_bycicle_button_id: {
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE, false);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.continue_last_action_id: {
                Intent intent = new Intent(this, CountActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.leader_board_button_id: {
                Intent intent = new Intent(this, LeaderBoard.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    }


}
