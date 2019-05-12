package coleo.com.abjo.activity.login;

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

//
//        Glide.with(this).load(R.mipmap.temp_pic)
//                .override(back., back.getLayoutParams().height)
//                .into(back);
//        back.getLayoutParams().width = Constants.getScreenWidth(this);
        back.getLayoutParams().height = (int) (Constants.getScreenHeight(this) - Constants.dpToPx(this, 150));
        back.requestLayout();



    }

    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.submit_login_id): {
                findViewById(R.id.submit_login_id).setEnabled(false);
                ServerClass.checkPhone(this, getPhoneNumber());
            }
        }
    }

    private String getPhoneNumber() {
        return phone.getText().toString().trim();
    }

    public void goCode(String phone) {
        findViewById(R.id.submit_login_id).setEnabled(false);
        Intent intent = new Intent(this, CodeActivity.class);
        intent.putExtra(Constants.PHONE_FROM_LOGIN,phone);
        startActivity(intent);
        finish();
    }

    public void enable(){
        findViewById(R.id.submit_login_id).setEnabled(true);
    }

}
