package com.openpositioning.PositionMe.data.local.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for AdmissionScore entities.
 * Provides methods to interact with the admission_scores table.
 *
 * @author PositionMe Team
 */
@Dao
public interface AdmissionScoreDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AdmissionScore score);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AdmissionScore> scores);

    @Update
    void update(AdmissionScore score);

    @Delete
    void delete(AdmissionScore score);

    @Query("SELECT * FROM admission_scores")
    List<AdmissionScore> getAllScores();

    @Query("SELECT * FROM admission_scores WHERE id = :scoreId")
    AdmissionScore getScoreById(long scoreId);

    @Query("SELECT * FROM admission_scores WHERE universityId = :universityId")
    List<AdmissionScore> getScoresByUniversity(long universityId);

    @Query("SELECT * FROM admission_scores WHERE majorId = :majorId")
    List<AdmissionScore> getScoresByMajor(long majorId);

    @Query("SELECT * FROM admission_scores WHERE year = :year")
    List<AdmissionScore> getScoresByYear(int year);

    @Query("SELECT * FROM admission_scores WHERE province = :province AND subjectGroup = :subjectGroup")
    List<AdmissionScore> getScoresByProvinceAndSubject(String province, String subjectGroup);

    @Query("SELECT * FROM admission_scores WHERE universityId = :universityId AND year = :year")
    List<AdmissionScore> getScoresByUniversityAndYear(long universityId, int year);

    @Query("SELECT * FROM admission_scores WHERE majorId = :majorId AND year = :year")
    List<AdmissionScore> getScoresByMajorAndYear(long majorId, int year);

    @Query("SELECT * FROM admission_scores WHERE province = :province AND subjectGroup = :subjectGroup AND year = :year")
    List<AdmissionScore> getScoresByProvinceSubjectAndYear(String province, String subjectGroup, int year);

    @Query("SELECT * FROM admission_scores WHERE minimumScore >= :minScore AND minimumScore <= :maxScore")
    List<AdmissionScore> getScoresByScoreRange(int minScore, int maxScore);

    @Query("SELECT DISTINCT year FROM admission_scores ORDER BY year DESC")
    List<Integer> getAllYears();

    @Query("DELETE FROM admission_scores")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM admission_scores")
    int getCount();
}
