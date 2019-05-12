package coleo.com.abjo.data_base;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserLocation {

    public UserLocation() {
    }

    public UserLocation(String latitude, String longitude, String time, String accuracy, int method) {
        this.accuracy = accuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.method = method;
        this.time = time;
    }


    @PrimaryKey(autoGenerate = true)
    public int number;

    @ColumnInfo(name = "data_method")
    public int method;

    @ColumnInfo(name = "user_lat")
    public String latitude;

    @ColumnInfo(name = "accuracy")
    public String accuracy;

    @ColumnInfo(name = "user_lng")
    public String longitude;

    @ColumnInfo(name = "time")
    public String time;

    public static UserLocation[] makeDifference(Location location, String time, int method) {
        UserLocation[] locations = new UserLocation[3];
        for (int i = 0; i < 3; i++) {
            String modifier = "%." + (6 + (i * 2)) + "f";
            String lat = String.format(modifier, location.getLatitude());
            String lng = String.format(modifier, location.getLongitude());
            String acc = String.format(modifier, location.getAccuracy());
            locations[i] = new UserLocation(lat, lng, time, acc, method + i);
        }
        return locations;
    }

    public static UserLocation[] makeDifference(double latitude, double longitude, String time, int method) {
        UserLocation[] locations = new UserLocation[3];
        for (int i = 0; i < 3; i++) {
            String modifier = "%." + (6 + (i * 2)) + "f";
            String lat = String.format(modifier, latitude);
            String lng = String.format(modifier, longitude);
//            String acc = String.format(modifier, accuracy);
            locations[i] = new UserLocation(lat, lng, time, "0.0", method + i);
        }
        return locations;
    }

}
