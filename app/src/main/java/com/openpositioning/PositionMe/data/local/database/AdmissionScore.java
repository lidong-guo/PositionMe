package com.openpositioning.PositionMe.data.local.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity class representing university admission scores.
 * Stores admission score data for physics group (物理组) in Hebei Province
 * for the past three years.
 *
 * @author PositionMe Team
 */
@Entity(tableName = "admission_scores",
        foreignKeys = {
                @ForeignKey(entity = University.class,
                        parentColumns = "id",
                        childColumns = "universityId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Major.class,
                        parentColumns = "id",
                        childColumns = "majorId",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("universityId"), @Index("majorId"), @Index("year")})
public class AdmissionScore {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long universityId;
    private long majorId;
    private int year; // Admission year (e.g., 2024, 2023, 2022)
    private String province; // Province where the student is from (e.g., "河北")
    private String subjectGroup; // Subject group (e.g., "物理组")
    
    // Score information
    private int minimumScore; // Minimum admission score
    private int maximumScore; // Maximum admission score
    private int averageScore; // Average admission score
    private int rankMinimum; // Minimum provincial rank
    private int rankMaximum; // Maximum provincial rank
    
    // Enrollment information
    private int plannedEnrollment; // Planned enrollment number
    private int actualEnrollment; // Actual enrollment number

    public AdmissionScore(long universityId, long majorId, int year, String province,
                         String subjectGroup, int minimumScore, int maximumScore,
                         int averageScore, int rankMinimum, int rankMaximum,
                         int plannedEnrollment, int actualEnrollment) {
        this.universityId = universityId;
        this.majorId = majorId;
        this.year = year;
        this.province = province;
        this.subjectGroup = subjectGroup;
        this.minimumScore = minimumScore;
        this.maximumScore = maximumScore;
        this.averageScore = averageScore;
        this.rankMinimum = rankMinimum;
        this.rankMaximum = rankMaximum;
        this.plannedEnrollment = plannedEnrollment;
        this.actualEnrollment = actualEnrollment;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(long universityId) {
        this.universityId = universityId;
    }

    public long getMajorId() {
        return majorId;
    }

    public void setMajorId(long majorId) {
        this.majorId = majorId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(String subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    public int getMinimumScore() {
        return minimumScore;
    }

    public void setMinimumScore(int minimumScore) {
        this.minimumScore = minimumScore;
    }

    public int getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(int maximumScore) {
        this.maximumScore = maximumScore;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getRankMinimum() {
        return rankMinimum;
    }

    public void setRankMinimum(int rankMinimum) {
        this.rankMinimum = rankMinimum;
    }

    public int getRankMaximum() {
        return rankMaximum;
    }

    public void setRankMaximum(int rankMaximum) {
        this.rankMaximum = rankMaximum;
    }

    public int getPlannedEnrollment() {
        return plannedEnrollment;
    }

    public void setPlannedEnrollment(int plannedEnrollment) {
        this.plannedEnrollment = plannedEnrollment;
    }

    public int getActualEnrollment() {
        return actualEnrollment;
    }

    public void setActualEnrollment(int actualEnrollment) {
        this.actualEnrollment = actualEnrollment;
    }
}
