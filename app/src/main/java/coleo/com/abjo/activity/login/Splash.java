package coleo.com.abjo.activity.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.server_class.ServerClass;

public class Splash extends AppCompatActivity {

    private final Context context = this;
    private boolean fromNotification;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView version = findViewById(R.id.versionTextView);
        String versionText = "0.1.0";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionText = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version.setText(" نسخه " + versionText + " ");

        retry = findViewById(R.id.retry);
        retry.setVisibility(View.INVISIBLE);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startApp();
            }
        });


        Bundle extra = getIntent().getExtras();
        String key = Constants.FROM_NOTIFICATION;
        if (extra != null)
            fromNotification = extra.getBoolean(key, false);
        else fromNotification = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        startApp();
    }

    private void startApp() {
        retry.setEnabled(false);
        if (ServerClass.isNetworkConnected(this))
        {
            retry.setVisibility(View.INVISIBLE);
            String token = Constants.getToken(this);
            final boolean goPhone = token.equals(Constants.NO_TOKEN) || token.isEmpty();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Intent intent;
                    if (goPhone) {
                        intent = new Intent(context, Login.class);
                    } else {
                        intent = new Intent(context, MainActivity.class);
                        intent.putExtra(Constants.FROM_NOTIFICATION, fromNotification);
                    }
//                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra(Constants.FROM_NOTIFICATION, false);
                    startActivity(intent);
                    finish();
                }
            };
            new Handler().postDelayed(runnable, 1000);
        }
        else {
            enable();
            Toast.makeText(context, "اتصال اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show();
        }
    }

    public void enable(){
        retry.setEnabled(true);
        retry.setVisibility(View.VISIBLE);
    }

}
