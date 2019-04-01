package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class Login extends AppCompatActivity {

    private EditText phone;
    private Button submit;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.back_login);
        phone = findViewById(R.id.phone_input_id);
        submit = findViewById(R.id.submit_login_id);

        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenWidth(this) * Constants.fuckingRatio);
        back.requestLayout();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.submit_login_id): {
                Intent intent = new Intent(this, CodeActivity.class);
                startActivity(intent);
            }
        }
    }

}
