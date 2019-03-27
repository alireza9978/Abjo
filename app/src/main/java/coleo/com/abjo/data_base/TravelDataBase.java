package coleo.com.abjo.data_base;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {UserLocation.class}, version = 3)
public abstract class TravelDataBase extends RoomDatabase {
    public abstract UserLocationDao userDao();
}
