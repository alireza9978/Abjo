package coleo.com.abjo.data_base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Action {

    public Action(String time, String action) {
        this.time = time;
        this.action = action;
    }

    @PrimaryKey(autoGenerate = true)
    public int number;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "action")
    public String action;

}
