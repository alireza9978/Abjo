package coleo.com.abjo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class Splash extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Constants.FROM_NOTIFICATION,false);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable, 2000);

    }
}
