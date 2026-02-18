package com.openpositioning.PositionMe.data.local.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for Major entities.
 * Provides methods to interact with the majors table.
 *
 * @author PositionMe Team
 */
@Dao
public interface MajorDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Major major);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Major> majors);

    @Update
    void update(Major major);

    @Delete
    void delete(Major major);

    @Query("SELECT * FROM majors")
    List<Major> getAllMajors();

    @Query("SELECT * FROM majors WHERE id = :majorId")
    Major getMajorById(long majorId);

    @Query("SELECT * FROM majors WHERE universityId = :universityId")
    List<Major> getMajorsByUniversity(long universityId);

    @Query("SELECT * FROM majors WHERE majorCategory = :category")
    List<Major> getMajorsByCategory(String category);

    @Query("SELECT * FROM majors WHERE majorName LIKE :name")
    List<Major> searchMajorsByName(String name);

    @Query("DELETE FROM majors")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM majors")
    int getCount();
}
