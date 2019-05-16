package coleo.com.abjo.service;

import com.mrq.android.ibrary.FinalCountDownTimer;

import coleo.com.abjo.constants.Constants;

public class OnCount implements FinalCountDownTimer.OnTimeDownCallBack {

    private int second;
    private int minute;
    private int hour;

    private static OnCount lastShape;

    public OnCount(int second, int minute, int hour) {
        this.second = second;
        this.minute = minute;
        this.hour = hour;
    }

    @Override
    public void onTick(long millisUntilFinished) {
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

    }

    @Override
    public void onFinish() {

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
            lastShape = new OnCount(0, 0, 0);
        }
        Constants.second.setText("" + lastShape.second);
        Constants.minute.setText("" + lastShape.minute);
        Constants.hour.setText("" + lastShape.hour);
        return lastShape;
    }

}