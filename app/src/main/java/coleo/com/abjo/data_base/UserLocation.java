package coleo.com.abjo.data_base;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class UserLocation {

    public UserLocation() {
    }

    private static int count = 0;

    public UserLocation(double latitude, double longitude) {
        this.latitude = "" + latitude;
        this.longitude = "" + longitude;
        this.number = count;
        count++;
    }

    @PrimaryKey
    public int number;

    @ColumnInfo(name = "user_lat")
    public String latitude;

    @ColumnInfo(name = "user_lng")
    public String longitude;

}
