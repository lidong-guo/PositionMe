package com.openpositioning.PositionMe.data.local.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

/**
 * Entity class representing a university in the database.
 * Stores basic information about universities in Hebei Province.
 *
 * @author PositionMe Team
 */
@Entity(tableName = "universities", indices = {@Index(value = "universityName", unique = true)})
public class University {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String universityName;
    private String universityCode;
    private String province;
    private String city;
    private String universityType; // e.g., "985", "211", "Double First-Class", "Regular"
    private String universityLevel; // e.g., "本科一批", "本科二批"

    public University(String universityName, String universityCode, String province, 
                     String city, String universityType, String universityLevel) {
        this.universityName = universityName;
        this.universityCode = universityCode;
        this.province = province;
        this.city = city;
        this.universityType = universityType;
        this.universityLevel = universityLevel;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getUniversityCode() {
        return universityCode;
    }

    public void setUniversityCode(String universityCode) {
        this.universityCode = universityCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniversityType() {
        return universityType;
    }

    public void setUniversityType(String universityType) {
        this.universityType = universityType;
    }

    public String getUniversityLevel() {
        return universityLevel;
    }

    public void setUniversityLevel(String universityLevel) {
        this.universityLevel = universityLevel;
    }
}
