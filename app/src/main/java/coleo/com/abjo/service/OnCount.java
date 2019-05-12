package coleo.com.abjo.service;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.mrq.android.ibrary.FinalCountDownTimer;

import java.util.ArrayDeque;
import java.util.Queue;

import coleo.com.abjo.MyLocation;
import coleo.com.abjo.activity.MainActivity;
import coleo.com.abjo.constants.Constants;
import coleo.com.abjo.data_base.UserLocation;
import mumayank.com.airlocationlibrary.AirLocation;

import static coleo.com.abjo.constants.Constants.context;
import static coleo.com.abjo.constants.Constants.saveLastAction;

class OnCount implements FinalCountDownTimer.OnTimeDownCallBack {

    private int second;
    private int minute;
    private int hour;
    private static SaveLocationService service;
    private static OnCount lastShape;
    private MyLocation myLocation = new MyLocation();
    private Queue<Long> timeQueue = new ArrayDeque<>();

    public OnCount(int second, int minute, int hour, SaveLocationService service) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
        OnCount.service = service;
        Constants.second.setText("" + second);
        Constants.minute.setText("" + minute);
        Constants.hour.setText("" + hour);
        lastShape = this;

    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (service == null) {
            saveLastAction(context, Constants.ACTION.STOP_FOREGROUND_ACTION);
            ((MainActivity)context).backToMain();
        }

        second++;
        if (second >= 60) {
            second = 0;
            minute++;
            if (minute >= 60) {
                minute = 0;
                hour++;
            }
        }
        Constants.second.setText("" + second);
        Constants.minute.setText("" + minute);
        Constants.hour.setText("" + hour);

        if (second % 2 == 0) {
            if (!service.isStep()) {
                timeQueue.add(System.currentTimeMillis());
                AirLocation airLocation = new AirLocation((Activity) context, true, true, new AirLocation.Callbacks() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.i("ONCount", "onSuccess: ");
                        if (location != null) {
                            service.getRepository().insert(UserLocation.makeDifference(location, "" + timeQueue.poll(), 0));
                        }
                    }

                    @Override
                    public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
                        // do something
                    }
                });
            }
        }
        if (second % 30 == 0) {
            System.gc();
        }
    }

    @Override
    public void onFinish() {
        service.countDownTimer = FinalCountDownTimer.createDefault(Constants.ONE_HOUR, new OnCount(second, minute, hour, service));
    }

    public int getSecond() {
        return second;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }

    public static OnCount getNew() {
        if (lastShape == null) {
//            lastShape = new OnCount(lastShape.second, lastShape.minute, lastShape.hour, service);
//        } else {
            lastShape = new OnCount(0, 0, 0, service);
        }
        Constants.second.setText("" + lastShape.second);
        Constants.minute.setText("" + lastShape.minute);
        Constants.hour.setText("" + lastShape.hour);
        return lastShape;
    }

}