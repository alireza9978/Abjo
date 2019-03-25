package coleo.com.abjo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent.getAction());
        Intent pauseIntent = new Intent(context, SaveLocationService.class);
        pauseIntent.setAction(intent.getAction());
        context.startService(pauseIntent);
    }
}
