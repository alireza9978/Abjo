package coleo.com.abjo.service;

import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mrq.android.ibrary.FinalCountDownTimer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import coleo.com.abjo.MyLocation;
import coleo.com.abjo.constants.Constants;

class OnCount implements FinalCountDownTimer.OnTimeDownCallBack {

    private int second;
    private int minute;
    private int hour;
    private static SaveLocationService service;
    private static OnCount lastShape;
    private MyLocation myLocation = new MyLocation();


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

        if (second % 2 == 0) {
            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    String temp = "{ 'lat':" + location.getLatitude() +
                            " 'lng':" + location.getLongitude() +
                            " 'time':" + System.currentTimeMillis() + " }";
                    writeToFile(temp, Constants.context);
                }
            };

            myLocation.getLocation(Constants.context, locationResult);
        }

    }

    private void writeToFile(String data, Context context) {
        try {
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            File myFile = new File(path, "secondMode.txt");
            FileOutputStream fOut = new FileOutputStream(myFile,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.close();
        }
        catch (java.io.IOException e) {
            e.printStackTrace();
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