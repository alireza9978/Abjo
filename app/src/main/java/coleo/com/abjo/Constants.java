package coleo.com.abjo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

public class Constants {

    public interface ACTION {
        public static String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }



    public static TextView count = null;
    public static float lastCount = 0;

    public static String STEP = "start counting";
    public static String STEP_OR_BIKE = "twoInOne";//true == step & false == bike

    public static Notification showNotification(String title, String message,
                                        Context context, Intent intent, boolean makeSound, boolean canClose) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "com.coleo.abjo")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(!canClose);


        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.coleo.abjo");
            if (makeSound) {
                builder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
            } else {
                builder.setPriority(NotificationManager.IMPORTANCE_MIN);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_MIN;
            if (makeSound) {
                importance = NotificationManager.IMPORTANCE_DEFAULT;
            }

            NotificationChannel channel = new NotificationChannel(
                    "com.coleo.abjo",
                    context.getString(R.string.app_name),
                    importance
            );

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            return builder.build();
        } else {
            Log.i("Constants", "showNotification: null");
            return null;
        }
    }

}
