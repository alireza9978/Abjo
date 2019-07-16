package coleo.com.abjo.data_base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserLocation {

    public UserLocation(long id, String latitude, String accuracy, String longitude) {
        this.id = id;
        this.latitude = latitude;
        this.accuracy = accuracy;
        this.longitude = longitude;
        time = "" + System.currentTimeMillis();
        synced = false;
    }

    @PrimaryKey(autoGenerate = true)
    public long number;

    @ColumnInfo(name = "session_id")
    public long id;

    @ColumnInfo(name = "user_lat")
    public String latitude;

    @ColumnInfo(name = "accuracy")
    public String accuracy;

    @ColumnInfo(name = "user_lng")
    public String longitude;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "synced")
    public boolean synced;


}
