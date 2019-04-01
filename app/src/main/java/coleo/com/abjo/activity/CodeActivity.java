package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class CodeActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        back = findViewById(R.id.code_back);
        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenWidth(this) * Constants.fuckingRatio);
        back.requestLayout();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_code_id: {
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
            }
        }
    }

}
