package coleo.com.abjo.activity.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.ColorOfProgress;
import com.robinhood.ticker.TickerUtils;

import java.util.Objects;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.TravelDataBase;
import coleo.com.abjo.data_base.UserLocation;
import coleo.com.abjo.data_base.locationRepository;
import coleo.com.abjo.data_class.ProfileData;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

import static coleo.com.abjo.constants.Constants.context;
import static coleo.com.abjo.constants.Constants.hour;
import static coleo.com.abjo.constants.Constants.minute;
import static coleo.com.abjo.constants.Constants.pause_resume;
import static coleo.com.abjo.constants.Constants.second;
import static coleo.com.abjo.constants.Constants.start_stop;


public class AfterStartFragment extends Fragment {


    private String startActionKind = "";
    private String pauseActionKind = "";
    private String resumeActionKind = "";
    private String lastAction = "";

    private long startTime;
    private long sumOfPause = 0;
    private long lastPause;

    private TextView name;
    private TextView level;
    private TextView point;
    private TextView coin;
    private TextView hourText;
    private BootstrapProgressBar progressBar;

    public AfterStartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_after_start, container, false);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setBootstrapBrand(new ColorOfProgress());
        hour = view.findViewById(R.id.hour);
        hour.setAnimationDuration(1500);
        hour.setCharacterLists(TickerUtils.provideNumberList());
        minute = view.findViewById(R.id.minuet);
        minute.setAnimationDuration(1500);
        minute.setCharacterLists(TickerUtils.provideNumberList());
        second = view.findViewById(R.id.second);
        second.setAnimationDuration(500);
        second.setCharacterLists(TickerUtils.provideNumberList());

        name = view.findViewById(R.id.user_name_text_view_id);
        coin = view.findViewById(R.id.coin_of_activity_text);
        point = view.findViewById(R.id.point_text_id);
        level = view.findViewById(R.id.level_text_id);
        hourText = view.findViewById(R.id.hour_of_activity_text);

        Constants.start_stop = view.findViewById(R.id.start_service_button_id);
        Constants.pause_resume = view.findViewById(R.id.pause_service_button_id);

        manageButton();

        Constants.start_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

        Constants.pause_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.isPause) {
                    resumeService();
                } else {
                    pauseService();
                }
            }
        });


        return view;
    }

    private void manageButton() {
        start_stop.setVisibility(View.VISIBLE);
        start_stop.setImageResource(R.mipmap.stop_icon);
        if (lastAction.equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE) ||
                lastAction.equals(Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP)) {
            pause_resume.setImageResource(R.mipmap.pause_icon);
        }
        if (lastAction.equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE) ||
                lastAction.equals(Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP)) {
            pause_resume.setImageResource(R.mipmap.play_icon);
        }
        if (lastAction.equals(Constants.ACTION.START_FOREGROUND_ACTION_BIKE)
                || lastAction.equals(Constants.ACTION.START_FOREGROUND_ACTION_STEP)) {
            pause_resume.setImageResource(R.mipmap.pause_icon);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        lastAction = Constants.getLastAction();
        manageButton();
    }

    public void startServiceFromNotification(ProfileData data) {
        lastAction = Constants.getLastAction();
        boolean isStep = Constants.isActionKindStep(lastAction);
        setActionKind(isStep);
//        updateProfile(data);
//        updateProfile(ServerClass.getProfile(getContext()));
    }

    public void startServiceFromOut(boolean isStep, ProfileData data) {
        setActionKind(isStep);
//        updateProfile(data);
        startService();
    }

    private void setActionKind(boolean isStep) {
        if (isStep) {
            startActionKind = Constants.ACTION.START_FOREGROUND_ACTION_STEP;
            pauseActionKind = Constants.ACTION.PAUSE_FOREGROUND_ACTION_STEP;
            resumeActionKind = Constants.ACTION.RESUME_FOREGROUND_ACTION_STEP;
        } else {
            startActionKind = Constants.ACTION.START_FOREGROUND_ACTION_BIKE;
            pauseActionKind = Constants.ACTION.PAUSE_FOREGROUND_ACTION_BIKE;
            resumeActionKind = Constants.ACTION.RESUME_FOREGROUND_ACTION_BIKE;
        }
    }


    private void startJob() {
        SmartLocation.with(context).location().config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Log.i("Location", "onLocationUpdated: ");
                        Objects.requireNonNull(locationRepository.get(null))
                                .insert(UserLocation.makeDifference(location,
                                        "" + System.currentTimeMillis(), 0));
                    }
                });
    }

    private void startService() {
        startTime = System.currentTimeMillis();
        makeDataBase();
        startJob();
        start_stop.setImageResource(R.mipmap.stop_icon);
        pause_resume.setVisibility(View.VISIBLE);
        Constants.isWorking = true;
        Constants.isPause = false;
    }

    private void stopService() {
        Constants.isWorking = false;
        Constants.isPause = false;
        locationRepository repository = locationRepository.get(null);
        if (repository != null)
            repository.makeJsonAndSend();
        ((MainActivity) Constants.context).backToMain();
        SmartLocation.with(context).location().stop();
    }

    private void resumeService() {
        pause_resume.setImageResource(R.mipmap.pause_icon);
        Constants.isPause = false;
        Constants.isWorking = true;

        long now = System.currentTimeMillis();
        sumOfPause += now - lastPause;
        long past = now - startTime - sumOfPause;
        Log.i("FRAGMENT", "resumeService: past time is" + past);
    }

    private void pauseService() {
        pause_resume.setImageResource(R.mipmap.play_icon);
        Constants.isWorking = true;
        Constants.isPause = true;
        pause_resume.setEnabled(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                pause_resume.setEnabled(true);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);

        lastPause = System.currentTimeMillis();

    }

    private void makeDataBase() {
        TravelDataBase dataBase = Room.databaseBuilder(context.getApplicationContext(),
                TravelDataBase.class, "database-name").build();
        locationRepository repository = locationRepository.get(dataBase);
        if (repository != null)
            repository.nukeTable();
        else {

        }
    }

    private void updateProfile(ProfileData data) {
        name.setText(data.getUser().getFullName());
        coin.setText(data.getCoinsText());
        hourText.setText(data.getHoursText());
        progressBar.setMaxProgress(data.getLevel().getLevelMaxPoint());
        progressBar.setProgress(data.getLevel().getPoint());
        point.setText(" " + data.getLevel().getPoint() + " امتیاز ");
        level.setText(" سطح " + data.getLevel().getLevel() + " ");
        if (data.getLevel().getPoint() < 10) {
            point.setTextColor(getResources().getColor(R.color.login_submit_gradient_left));
        }
    }

}
