package coleo.com.abjo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import coleo.com.abjo.R;

public class Login extends AppCompatActivity {

    private EditText phone;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone_input_id);
        submit = findViewById(R.id.submit_login_id);

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
