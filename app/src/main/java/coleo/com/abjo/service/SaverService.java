package coleo.com.abjo.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Objects;

import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.Action;
import coleo.com.abjo.data_base.UserLocation;
import coleo.com.abjo.data_base.locationRepository;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

import static coleo.com.abjo.constants.Constants.context;
import static coleo.com.abjo.constants.Constants.getLastAction;
import static coleo.com.abjo.constants.Constants.isActionKindStep;
import static coleo.com.abjo.constants.Constants.isPause;
import static coleo.com.abjo.constants.Constants.isWorking;
import static coleo.com.abjo.constants.Constants.manageButton;
import static coleo.com.abjo.constants.Constants.showNotification;
import static coleo.com.abjo.constants.Constants.updateNotification;

public class SaverService extends Service {

    private static final int id = Constants.NOTIFICATION_ID.FOREGROUND_SERVICE;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private NotificationCompat.Builder builder;
    private static final String workingMassage = "در حال ذخیره کردن اطلاعات ...";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (!action.equals(Constants.ACTION.UPDATE_FOREGROUND_ACTION))
                    Constants.saveLastAction(context, action);
                switch (action) {
                    case Constants.ACTION.UPDATE_FOREGROUND_ACTION: {
                        if (context != null) {
                            ((MainActivity) context).manageTimer();
                        }
                        break;
                    }
                    case Constants.ACTION.START_FOREGROUND_ACTION_BIKE:
                    case Constants.ACTION.START_FOREGROUND_ACTION_STEP: {
                        Constants.isWorking = true;
                        Constants.isPause = false;
                        boolean isStep = isActionKindStep(action);
                        String title = "دوچرخه";
                        if (isStep) {
                            title = "پیاده روی";
                        }
                        builder = showNotification(title, workingMassage, context, true, false, isStep, false);
                        startForeground(id, builder.build());
                        startJob();
                        break;
                    }
                    case Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE:
                    case Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP:
                        Constants.isWorking = true;
                        Constants.isPause = true;
                        Objects.requireNonNull(locationRepository.get(null)).insert(new Action("" + System.currentTimeMillis(), "pause"));
                        stopJob();
                        updateNotification(builder, "توقف", "در حال حاضر امتیاز شما محاسبه نمی شود.", context, true, isActionKindStep(action));
                        if (context != null) {
                            ((MainActivity) context).stopTimer();
                        }
                        break;
                    case Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE:
                    case Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP: {
                        Constants.isPause = false;
                        Constants.isWorking = true;
                        startJob();
                        Objects.requireNonNull(locationRepository.get(null)).insert(new Action("" + System.currentTimeMillis(), "resume"));
                        boolean isStep = isActionKindStep(action);
                        String title = "دوچرخه";
                        if (isStep) {
                            title = "پیاده روی";
                        }
                        if (context != null) {
                            ((MainActivity) context).startTimer();
                        }
                        updateNotification(builder, title, workingMassage, context, false, isActionKindStep(action));
                        break;
                    }
                    case (Constants.ACTION.STOP_FOREGROUND_ACTION):
                        Constants.isWorking = false;
                        Constants.isPause = false;
                        stopJob();
                        stopForeground(true);
                        stopSelf(id);
                        break;
                }
                manageButton(getLastAction());
            }
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    private void startJob() {
        SmartLocation.with(context).location().config(LocationParams.NAVIGATION).continuous()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        if (!isPause && isWorking)
                            Objects.requireNonNull(locationRepository.get(null))
                                    .insert(new UserLocation("" + location.getLatitude(),"" + location.getLongitude(),
                                            "" + System.currentTimeMillis(), "" + location.getAccuracy()));

                    }
                });
    }

    private void stopJob() {
        SmartLocation.with(context).location().stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!getLastAction().equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i("SAVER_SERVICE", "onDestroy: " + getLastAction());
//            Intent intent = new Intent(context, SaverReceiver.class);
//            context.startService(intent);
        }
    }
}
