package coleo.com.abjo.data_base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserLocation.class, Action.class}, version = 8,exportSchema = false)
public abstract class TravelDataBase extends RoomDatabase {
    public abstract UserLocationDao userDao();
    public abstract PauseDao actionDao();
}
