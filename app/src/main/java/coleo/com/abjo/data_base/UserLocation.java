package coleo.com.abjo.data_base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserLocation {

    public UserLocation() {
    }

    public UserLocation(double latitude, double longitude, String time, double accuracy, int method) {
        this.accuracy = String.format("%.2f", accuracy);
        this.latitude = String.format("%.6f", latitude);
        this.longitude = String.format("%.6f", longitude);
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

}
