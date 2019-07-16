package coleo.com.abjo.data_base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserLocationDao {
    @Query("SELECT * FROM UserLocation")
    List<UserLocation> getAll();

    @Query("SELECT * FROM UserLocation WHERE synced='FALSE'")
    List<UserLocation> getNotSynced();

    @Query("DELETE FROM UserLocation WHERE synced='TRUE'")
    void nukeTable();

    @Insert
    void insertAll(UserLocation... users);

    @Delete
    void delete(UserLocation user);

}
