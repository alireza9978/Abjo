package coleo.com.abjo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_step_button_id:{
                Log.i(TAG, "onClick: step");
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE,true);
                startActivity(intent);
                break;
            }
            case R.id.start_bycicle_button_id:{
                Log.i(TAG, "onClick: bike");
                Intent intent = new Intent(this, CountActivity.class);
                intent.putExtra(Constants.STEP_OR_BIKE,false);
                startActivity(intent);
                break;
            }
            case R.id.leader_board_button_id:{
                Log.i(TAG, "onClick: leader");
                break;
            }
        }
    }
}
