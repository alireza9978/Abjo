package coleo.com.abjo.activity.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.mrq.android.ibrary.FinalCountDownTimer;
import com.robinhood.ticker.TickerUtils;

import java.util.Objects;

import coleo.com.abjo.R;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.Action;
import coleo.com.abjo.data_base.TravelDataBase;
import coleo.com.abjo.data_base.locationRepository;
import coleo.com.abjo.data_class.ProfileData;
import coleo.com.abjo.service.OnCount;
import coleo.com.abjo.service.SaverService;

import static android.content.Context.MODE_PRIVATE;
import static coleo.com.abjo.constants.Constants.ONE_HOUR;
import static coleo.com.abjo.constants.Constants.context;
import static coleo.com.abjo.constants.Constants.hour;
import static coleo.com.abjo.constants.Constants.isPause;
import static coleo.com.abjo.constants.Constants.manageButton;
import static coleo.com.abjo.constants.Constants.minute;
import static coleo.com.abjo.constants.Constants.pause_resume;
import static coleo.com.abjo.constants.Constants.second;
import static coleo.com.abjo.constants.Constants.start_stop;


public class AfterStartFragment extends Fragment {


    private String startActionKind = "";
    private String pauseActionKind = "";
    private String resumeActionKind = "";
    private String lastAction = "";

    private long startTime = 0;
    private long sumOfPause = 0;
    private long lastPause = 0;
    private FinalCountDownTimer timer;

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

        manageButton(lastAction);

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




    @Override
    public void onResume() {
        super.onResume();
        getLastTime();
        lastAction = Constants.getLastAction();
        manageButton(lastAction);
        if (!isPause) {
            continueTimer();
        } else {
            updateTextViews();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveLastStartTime();
        if (timer != null) {
            timer.cancel();
        }
    }

    public void startServiceFromNotification(ProfileData data) {
        lastAction = Constants.getLastAction();
        boolean isStep = Constants.isActionKindStep(lastAction);
        setActionKind(isStep);
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        startTime = System.currentTimeMillis();
        lastPause = 0;
        sumOfPause = 0;
        timer = FinalCountDownTimer.createDefault(ONE_HOUR, new OnCount(0, 0, 0));
        timer.start();
    }

    public void pauseFromNotification() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public void resumeFromNotification() {
        if (timer != null) {
            timer.start();
        }
    }

    private void continueTimer() {
        if (timer != null) {
            timer.cancel();
        }
        getLastTime();
        long now = System.currentTimeMillis();
        long past = now - startTime - sumOfPause;
        past /= 1000;
        int min = 0;
        int hour = 0;
        if (past > 3_600) {
            hour = (int) (past / 3600);
            past -= (hour * 3600);
        }
        if (past > 60) {
            min = (int) (past / 60);
            past -= (min * 60);
        }
        timer = FinalCountDownTimer.createDefault(ONE_HOUR, new OnCount((int) past, min, hour));
        timer.start();
    }

    private void updateTextViews() {
        if (timer != null) {
            timer.cancel();
        }
        getLastTime();
        long past = lastPause - startTime - sumOfPause;
        past /= 1000;
        int min = 0;
        int hour = 0;
        if (past > 3_600) {
            hour = (int) (past / 3600);
            past -= (hour * 3600);
        }
        if (past > 60) {
            min = (int) (past / 60);
            past -= (min * 60);
        }
        Constants.second.setText("" + past + 1);
        Constants.minute.setText("" + min);
        Constants.hour.setText("" + hour);

    }

    public void startServiceFromOut(boolean isStep, ProfileData data) {
        setActionKind(isStep);
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
        Intent intent = new Intent(context, SaverService.class);
        intent.setAction(startActionKind);
        Log.i("Receiver", "onReceive: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private void stopJob() {
        Intent intent = new Intent(context, SaverService.class);
        intent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        Log.i("Receiver", "onReceive: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private void resumeJob() {
        Intent intent = new Intent(context, SaverService.class);
        intent.setAction(resumeActionKind);
        Log.i("Receiver", "onReceive: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private void pauseJob() {
        Intent intent = new Intent(context, SaverService.class);
        intent.setAction(pauseActionKind);
        Log.i("Receiver", "onReceive: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private void saveLastStartTime() {
        SharedPreferences.Editor editor = context.getSharedPreferences("LAST_TIME", MODE_PRIVATE).edit();
        editor.putString("START", "" + startTime);
        editor.putString("SUM", "" + sumOfPause);
        editor.putString("PAUSE", "" + lastPause);
        editor.apply();
    }

    private void getLastTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LAST_TIME", MODE_PRIVATE);
        startTime = Long.valueOf(Objects.requireNonNull(sharedPreferences.getString("START", "0")));
        sumOfPause = Long.valueOf(Objects.requireNonNull(sharedPreferences.getString("SUM", "0")));
        lastPause = Long.valueOf(Objects.requireNonNull(sharedPreferences.getString("PAUSE", "0")));
    }

    private void startService() {
        startTime = System.currentTimeMillis();
        makeDataBase();
        startJob();
        startTimer();
        start_stop.setImageResource(R.mipmap.stop_icon);
        pause_resume.setVisibility(View.VISIBLE);
        Constants.isWorking = true;
        Constants.isPause = false;
        locationRepository repository = locationRepository.get(null);
        if (repository != null) {
            repository.insert(new Action("" + System.currentTimeMillis(), "start"));
        }
    }

    private void stopService() {
        Constants.isWorking = false;
        Constants.isPause = false;
        stopJob();
        locationRepository repository = locationRepository.get(null);
        if (repository != null) {
            repository.insert(new Action("" + System.currentTimeMillis(), "stop"));
            repository.makeJsonAndSend();
        }
        startTime = 0;
        sumOfPause = 0;
        lastPause = 0;
        ((MainActivity) context).backToMain();
    }

    private void resumeService() {
        pause_resume.setImageResource(R.mipmap.pause_icon);
        Constants.isPause = false;
        Constants.isWorking = true;
        long now = System.currentTimeMillis();
        sumOfPause += now - lastPause;
        if (timer != null) {
            timer.start();
        }
        resumeJob();
        pause_resume.setEnabled(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                pause_resume.setEnabled(true);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 1000);
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
        if (timer != null) {
            timer.cancel();
        }
        pauseJob();
    }

    private void makeDataBase() {
        TravelDataBase dataBase = Room.databaseBuilder(context.getApplicationContext(),
                TravelDataBase.class, "database-name").build();
        locationRepository repository = locationRepository.get(dataBase);
        assert repository != null;
        repository.setUserLocationDao(dataBase.userDao());
        repository.setActionDao(dataBase.actionDao());
        repository.nukeTable();
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
