package coleo.com.abjo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import java.util.Objects;

import coleo.com.abjo.constants.Constants;

public class SaverReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Receiver", "onReceive: real");
        Intent start = new Intent(context, SaverService.class);
        if (Objects.equals(intent.getAction(), Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE) ||
                Objects.equals(intent.getAction(), Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP)) {
            start.setAction(intent.getAction());
        } else if (intent.getAction().equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE) ||
                intent.getAction().equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP)) {
            start.setAction(intent.getAction());
        } else {
            start.setAction(Constants.ACTION.START_FOREGROUND_ACTION_BIKE);
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(start);
        } else {
            context.startService(start);
        }
    }
}
