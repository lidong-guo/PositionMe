package com.openpositioning.PositionMe.data.local.database;

/**
 * Data model class representing detailed admission information.
 * This combines data from University, Major, and AdmissionScore tables.
 *
 * @author PositionMe Team
 */
public class AdmissionDetail {
    private String universityName;
    private String universityType;
    private String majorName;
    private String majorCategory;
    private int year;
    private int minimumScore;
    private int maximumScore;
    private int averageScore;
    private int rankMinimum;
    private int rankMaximum;
    private int plannedEnrollment;
    private int actualEnrollment;

    public AdmissionDetail(String universityName, String universityType, String majorName,
                          String majorCategory, int year, int minimumScore, int maximumScore,
                          int averageScore, int rankMinimum, int rankMaximum,
                          int plannedEnrollment, int actualEnrollment) {
        this.universityName = universityName;
        this.universityType = universityType;
        this.majorName = majorName;
        this.majorCategory = majorCategory;
        this.year = year;
        this.minimumScore = minimumScore;
        this.maximumScore = maximumScore;
        this.averageScore = averageScore;
        this.rankMinimum = rankMinimum;
        this.rankMaximum = rankMaximum;
        this.plannedEnrollment = plannedEnrollment;
        this.actualEnrollment = actualEnrollment;
    }

    // Getters
    public String getUniversityName() {
        return universityName;
    }

    public String getUniversityType() {
        return universityType;
    }

    public String getMajorName() {
        return majorName;
    }

    public String getMajorCategory() {
        return majorCategory;
    }

    public int getYear() {
        return year;
    }

    public int getMinimumScore() {
        return minimumScore;
    }

    public int getMaximumScore() {
        return maximumScore;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public int getRankMinimum() {
        return rankMinimum;
    }

    public int getRankMaximum() {
        return rankMaximum;
    }

    public int getPlannedEnrollment() {
        return plannedEnrollment;
    }

    public int getActualEnrollment() {
        return actualEnrollment;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d年)\n" +
                        "最低分: %d, 平均分: %d, 最高分: %d\n" +
                        "位次范围: %d - %d\n" +
                        "计划招生: %d人, 实际录取: %d人",
                universityName, majorName, year,
                minimumScore, averageScore, maximumScore,
                rankMinimum, rankMaximum,
                plannedEnrollment, actualEnrollment);
    }
}
