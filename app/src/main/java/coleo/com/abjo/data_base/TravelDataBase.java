package coleo.com.abjo.data_base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserLocation.class}, version = 5)
public abstract class TravelDataBase extends RoomDatabase {
    public abstract UserLocationDao userDao();
}
