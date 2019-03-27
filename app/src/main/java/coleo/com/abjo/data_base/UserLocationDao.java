package coleo.com.abjo.data_base;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserLocationDao {
    @Query("SELECT * FROM UserLocation")
    List<UserLocation> getAll();

    @Query("SELECT * FROM UserLocation WHERE number IN (:time)")
    List<UserLocation> loadAllByIds(int[] time);

    @Query("SELECT * FROM UserLocation WHERE user_lat LIKE :temp_lat AND " +
            "user_lng LIKE :temp_lng LIMIT 1")
    UserLocation findByName(String temp_lat, String temp_lng);

    @Query("DELETE FROM UserLocation")
    void nukeTable();

    @Insert
    void insertAll(UserLocation... users);

    @Delete
    void delete(UserLocation user);

}
