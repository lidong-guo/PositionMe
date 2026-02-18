package com.openpositioning.PositionMe.data.local.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a university major/program.
 * Stores information about specific majors offered by universities.
 *
 * @author PositionMe Team
 */
@Entity(tableName = "majors",
        foreignKeys = @ForeignKey(entity = University.class,
                parentColumns = "id",
                childColumns = "universityId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("universityId")})
public class Major {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long universityId;
    private String majorName;
    private String majorCode;
    private String majorCategory; // e.g., "工学", "理学", "医学"
    private String degreeType; // e.g., "本科", "专科"
    private int duration; // Study duration in years

    public Major(long universityId, String majorName, String majorCode, 
                String majorCategory, String degreeType, int duration) {
        this.universityId = universityId;
        this.majorName = majorName;
        this.majorCode = majorCode;
        this.majorCategory = majorCategory;
        this.degreeType = degreeType;
        this.duration = duration;
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

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getMajorCategory() {
        return majorCategory;
    }

    public void setMajorCategory(String majorCategory) {
        this.majorCategory = majorCategory;
    }

    public String getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(String degreeType) {
        this.degreeType = degreeType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
