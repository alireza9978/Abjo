package coleo.com.abjo.data_base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Action {

    public Action(long id, String action, long step) {
        this.id = id;
        this.time = "" + System.currentTimeMillis();
        this.action = action;
        this.step = step;
        synced = false;
    }

    @PrimaryKey(autoGenerate = true)
    public int number;

    @ColumnInfo(name = "session_id")
    public long id;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "action")
    public String action;

    @ColumnInfo(name = "stepCount")
    public long step;

    @ColumnInfo(name = "synced")
    public boolean synced;

}
