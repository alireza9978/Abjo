package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class SignUpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ImageView back = findViewById(R.id.top_image_id);
        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenWidth(this) * Constants.fuckingRatioTop);
        back.requestLayout();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_sign_up_id: {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }
        }
    }

}
