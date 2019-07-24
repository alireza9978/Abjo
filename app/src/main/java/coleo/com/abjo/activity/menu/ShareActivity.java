package coleo.com.abjo.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle extra = getIntent().getExtras();
        final String inviteCode = extra.getString(Constants.DATA_INVITE_CODE);

        Constants.trackEvent("sharePageOpened");

        ImageView back = findViewById(R.id.inviteBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button share = findViewById(R.id.share);
        TextView code = findViewById(R.id.inviteCode);

        code.setText(inviteCode);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, inviteCode);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }
}
