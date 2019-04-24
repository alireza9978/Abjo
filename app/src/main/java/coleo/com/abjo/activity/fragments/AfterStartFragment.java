package coleo.com.abjo.activity.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.ColorOfProgress;
import com.robinhood.ticker.TickerUtils;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import coleo.com.abjo.R;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_class.ProfileData;
import coleo.com.abjo.service.SaveLocationService;

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

//        if (!lastAction.equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
//            getView().findViewById(R.id.start_service_button_id).setVisibility(View.INVISIBLE);
//            getView().findViewById(R.id.pause_service_button_id).setVisibility(View.INVISIBLE);
////            getView().findViewById(R.id.continue_last_action_id).setVisibility(View.VISIBLE);
//        } else {
//            getView().findViewById(R.id.start_service_button_id).setVisibility(View.VISIBLE);
//            getView().findViewById(R.id.pause_service_button_id).setVisibility(View.VISIBLE);
////            getView().findViewById(R.id.continue_last_action_id).setVisibility(View.INVISIBLE);
//        }
    }

    public void startServiceFromNotification(ProfileData data) {
        lastAction = Constants.getLastAction();
        boolean isStep = Constants.isActionKindStep(lastAction);
        setActionKind(isStep);
        updateProfile(data);
        Intent startIntent = new Intent(getActivity(), SaveLocationService.class);
        startIntent.setAction(Constants.ACTION.UPDATE_FOREGROUND_ACTION);
        Objects.requireNonNull(getActivity()).startService(startIntent);
//        updateProfile(ServerClass.getProfile(getContext()));
    }

    public void startServiceFromOut(boolean isStep, ProfileData data) {
        setActionKind(isStep);
        updateProfile(data);
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

    private void startService() {
        Intent startIntent = new Intent(getActivity(), SaveLocationService.class);
        startIntent.setAction(startActionKind);
        Objects.requireNonNull(getActivity()).startService(startIntent);
    }

    private void stopService() {
        Intent stopIntent = new Intent(getActivity(), SaveLocationService.class);
        stopIntent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        Objects.requireNonNull(getActivity()).startService(stopIntent);
    }

    private void pauseService() {
        Intent pauseIntent = new Intent(getActivity(), SaveLocationService.class);
        pauseIntent.setAction(pauseActionKind);
        Objects.requireNonNull(getActivity()).startService(pauseIntent);
    }

    private void resumeService() {
        Intent resumeIntent = new Intent(getActivity(), SaveLocationService.class);
        resumeIntent.setAction(resumeActionKind);
        Objects.requireNonNull(getActivity()).startService(resumeIntent);
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
