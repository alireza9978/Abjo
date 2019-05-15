package coleo.com.abjo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import coleo.com.abjo.constants.Constants;

public class SaverReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent start = new Intent(context, SaverService.class);
        start.setAction(Constants.ACTION.START_FOREGROUND_ACTION_BIKE);
        context.startService(start);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
    }
}
