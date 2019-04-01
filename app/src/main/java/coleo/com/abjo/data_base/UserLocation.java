package coleo.com.abjo.data_base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserLocation {

    public UserLocation() {
    }

    public UserLocation(double latitude, double longitude, String time) {
        this.latitude = "" + latitude;
        this.longitude = "" + longitude;
        this.time = time;
    }


    @PrimaryKey(autoGenerate = true)
    public int number;

    @ColumnInfo(name = "user_lat")
    public String latitude;

    @ColumnInfo(name = "user_lng")
    public String longitude;

    @ColumnInfo(name = "time")
    public String time;

}
