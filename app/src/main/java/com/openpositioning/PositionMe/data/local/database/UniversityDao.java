package com.openpositioning.PositionMe.data.local.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for University entities.
 * Provides methods to interact with the universities table.
 *
 * @author PositionMe Team
 */
@Dao
public interface UniversityDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(University university);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<University> universities);

    @Update
    void update(University university);

    @Delete
    void delete(University university);

    @Query("SELECT * FROM universities")
    List<University> getAllUniversities();

    @Query("SELECT * FROM universities WHERE id = :universityId")
    University getUniversityById(long universityId);

    @Query("SELECT * FROM universities WHERE universityName = :name")
    University getUniversityByName(String name);

    @Query("SELECT * FROM universities WHERE province = :province")
    List<University> getUniversitiesByProvince(String province);

    @Query("SELECT * FROM universities WHERE universityType = :type")
    List<University> getUniversitiesByType(String type);

    @Query("DELETE FROM universities")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM universities")
    int getCount();
}
