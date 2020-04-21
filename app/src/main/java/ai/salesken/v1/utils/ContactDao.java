package ai.salesken.v1.utils;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ai.salesken.v1.pojo.ContactPojo;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contactpojo")
    List<ContactPojo> getAll();

    @Query("SELECT * FROM contactpojo WHERE uid IN (:userIds)")
    List<ContactPojo> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM contactpojo WHERE name LIKE :first LIMIT 1")
    ContactPojo findByName(String first);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContactPojo> values);

    @Delete
    void delete(ContactPojo user);

    @Query("DELETE FROM contactpojo")
    void deleteAll();
}
