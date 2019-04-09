package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.server_class.ServerClass;

public class Login extends AppCompatActivity {

    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView back = findViewById(R.id.back_login);
        phone = findViewById(R.id.phone_input_id);

        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenWidth(this) * Constants.fuckingRatio);
        back.requestLayout();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.submit_login_id): {
                ServerClass.checkPhone(this, getPhoneNumber());
            }
        }
    }

    private String getPhoneNumber() {
        return phone.getText().toString().trim();
    }

    public void goCode() {
        findViewById(R.id.submit_login_id).setEnabled(false);
        Intent intent = new Intent(this, CodeActivity.class);
        startActivity(intent);
        finish();
    }
}
