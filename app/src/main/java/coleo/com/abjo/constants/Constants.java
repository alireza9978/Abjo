package coleo.com.abjo.constants;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.Menu;
import coleo.com.abjo.service.MyReceiver;

import static android.content.Context.MODE_PRIVATE;

public class Constants {

    public static final String LAST_ACTION_PRE_NAME = "unknown";
    public static final String LAST_ACTION_SAVE_NAME = "noName";
    public interface ACTION {
        public static String START_FOREGROUND_ACTION_STEP = "com.truiton.foregroundservice.action.start.foreground.step";
        public static String START_FOREGROUND_ACTION_BIKE = "com.truiton.foregroundservice.action.start.foreground.bike";
        public static String PAUSE_FOREGROUND_ACTION_STEP = "com.truiton.foregroundservice.action.pause.foreground.step";
        public static String PAUSE_FOREGROUND_ACTION_BIKE = "com.truiton.foregroundservice.action.pause.foreground.bike";
        public static String RESUME_FOREGROUND_ACTION_STEP = "com.truiton.foregroundservice.action.resume.foreground.step";
        public static String RESUME_FOREGROUND_ACTION_BIKE = "com.truiton.foregroundservice.action.resume.foreground.bike";
        public static String STOP_FOREGROUND_ACTION = "com.truiton.foregroundservice.action.stop.foreground";
    }
    public static boolean isActionKindStep(String action){
        return !action.equals(ACTION.START_FOREGROUND_ACTION_BIKE) &&
                !action.equals(ACTION.PAUSE_FOREGROUND_ACTION_BIKE) &&
                !action.equals(ACTION.RESUME_FOREGROUND_ACTION_BIKE);

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Button start_stop = null;
    public static Button pause_resume= null;
    public static boolean isWorking = false;
    public static boolean isPause = false;

    private static String NOTIFICATION_CHANELL_ID = "com.coleo.abjo";


    public static TextView count = null;
    public static float lastCount = 0;

    public static String STEP = "start counting";
    public static String STEP_OR_BIKE = "start counting";
    public static String LAST_ACTION_INTENT = "twoInOne";//true == step & false == bike
    public static String FROM_NOTIFICATION = "fromOtherSide";

    public static NotificationCompat.Builder showNotification(String title, String message,
                                                              Context context, boolean makeSound,
                                                              boolean canClose, boolean isStep,
                                                              boolean isPause) {
        //todo prevent open program many time
        Intent intent = new Intent(context, Menu.class);
        intent.putExtra(FROM_NOTIFICATION, true);
        intent.putExtra(LAST_ACTION_INTENT, getLastAction(context));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent pause = new Intent(context, MyReceiver.class);
        if (isPause) {
            if (isStep) {
                pause.setAction(ACTION.RESUME_FOREGROUND_ACTION_STEP);
            } else {
                pause.setAction(ACTION.RESUME_FOREGROUND_ACTION_BIKE);
            }
        } else {
            if (isStep) {
                pause.setAction(ACTION.PAUSE_FOREGROUND_ACTION_STEP);
            } else {
                pause.setAction(ACTION.PAUSE_FOREGROUND_ACTION_BIKE);
            }
        }
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(context, 0, pause, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANELL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setOnlyAlertOnce(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setOngoing(!canClose)
                .addAction(android.R.drawable.ic_media_pause, "pause", snoozePendingIntent);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_MIN;
//            if (makeSound) {
//                importance = NotificationManager.IMPORTANCE_DEFAULT;
//            }
//
//            NotificationChannel channel = new NotificationChannel(
//                    NOTIFICATION_CHANELL_ID,
//                    context.getString(R.string.app_name),
//                    importance
//            );
//
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(notificationManager);
            builder.setChannelId(NOTIFICATION_CHANELL_ID);
            if (makeSound) {
                builder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);
            } else {
                builder.setPriority(NotificationManager.IMPORTANCE_MIN);
            }
        }

        return builder;
    }

    @TargetApi(26)
    private static void createChannel(NotificationManager notificationManager) {
        String name = NOTIFICATION_CHANELL_ID;
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(name, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
    }

    @SuppressLint("RestrictedApi")
    public static void updateNotification(NotificationCompat.Builder notification,
                                          String title, String message,
                                          Context context,
                                          boolean isPause, boolean isStep) {

        Intent pause = new Intent(context, MyReceiver.class);
        int drawable;
        String actionName;
        if (isPause) {
            actionName = "resume";
            drawable = android.R.drawable.ic_media_play;
            if (isStep) {
                pause.setAction(ACTION.RESUME_FOREGROUND_ACTION_STEP);
            } else {
                pause.setAction(ACTION.RESUME_FOREGROUND_ACTION_BIKE);
            }
        } else {
            actionName = "pause";
            drawable = android.R.drawable.ic_media_pause;
            if (isStep) {
                pause.setAction(ACTION.PAUSE_FOREGROUND_ACTION_STEP);
            } else {
                pause.setAction(ACTION.PAUSE_FOREGROUND_ACTION_BIKE);
            }
        }
        PendingIntent pausePendingIntent =
                PendingIntent.getBroadcast(context, 0, pause, 0);

        if (notification != null) {
            Log.i("Constants", "updateNotification: ");
            if (notification.mActions != null)
                notification.mActions.clear();

            notification.addAction(drawable, actionName, pausePendingIntent);
            notification.setContentTitle(title);
            notification.setContentText(message);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID.FOREGROUND_SERVICE, notification.build());
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

    public static void saveLastAction(Context context,String action){
        SharedPreferences.Editor editor = context.getSharedPreferences(LAST_ACTION_PRE_NAME, MODE_PRIVATE).edit();
        editor.putString(LAST_ACTION_SAVE_NAME, action);
        editor.apply();
    }

    public static String getLastAction(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(LAST_ACTION_PRE_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(LAST_ACTION_SAVE_NAME, ACTION.STOP_FOREGROUND_ACTION);
    }

}
