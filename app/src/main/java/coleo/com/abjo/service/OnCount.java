package coleo.com.abjo.service;

import com.mrq.android.ibrary.FinalCountDownTimer;

import coleo.com.abjo.constants.Constants;

class OnCount implements FinalCountDownTimer.OnTimeDownCallBack {

    private int second;
    private int minute;
    private int hour;
    private static SaveLocationService service;
    private static OnCount lastShape;

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