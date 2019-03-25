package coleo.com.abjo.constants;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import coleo.com.abjo.activity.Menu;
import coleo.com.abjo.R;

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


    private static void displayPromptForEnablingGPS(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Do you want open GPS setting?";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                                ((Menu) activity).noPermission();
                            }
                        });
        builder.create().show();
    }

    private static void displayPromptForGettingPermission(final Activity activity) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final String message = "A need your permission to work probebly\n" +
                "Do you want open App setting?";
        final Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);


        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(intent);
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                                ((Menu) activity).noPermission();
                            }
                        });
        builder.create().show();
    }

    public static void checkPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Constants.displayPromptForGettingPermission(context);
        }
    }

    public static void checkLocation(Activity context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {
            Constants.displayPromptForEnablingGPS(context);
        }
    }

}
