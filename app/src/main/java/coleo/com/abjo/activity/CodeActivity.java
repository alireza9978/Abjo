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

public class CodeActivity extends AppCompatActivity {

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        Bundle extra = getIntent().getExtras();
        phone = extra.getString(Constants.PHONE_FROM_LOGIN," ");

        ImageView back = findViewById(R.id.code_back);
        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenWidth(this) * Constants.fuckingRatio);
        back.requestLayout();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_code_id: {
                findViewById(R.id.submit_code_id).setEnabled(false);
                String temp = ((EditText) findViewById(R.id.code_input_id)).getText().toString().trim();
                ServerClass.sendCode(this,phone,temp);
            }
        }
    }

    public void goMainPage() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.FROM_NOTIFICATION, false);
        startActivity(intent);
        finish();
    }

    public void wrongCode(){

    }

    public void goSignUp(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
