package coleo.com.abjo.activity.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class RuleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        Constants.trackEvent("openRuleUsActivity");


    }
}
