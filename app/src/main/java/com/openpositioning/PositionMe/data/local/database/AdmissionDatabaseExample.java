package com.openpositioning.PositionMe.data.local.database;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Example usage of the Admission Database system.
 * This class demonstrates how to use the database for managing
 * university admission score data for Hebei Province physics group.
 * 
 * @author PositionMe Team
 */
public class AdmissionDatabaseExample {
    private static final String TAG = "AdmissionDBExample";
    private final DatabaseHelper helper;

    public AdmissionDatabaseExample(Context context) {
        this.helper = new DatabaseHelper(context);
    }

    /**
     * Example 1: Initialize database with sample data
     */
    public void exampleInitializeDatabase() {
        Log.d(TAG, "Initializing database with sample data...");
        
        helper.insertSampleData(new DatabaseHelper.DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "✓ Database initialized successfully!");
                // After initialization, show statistics
                exampleShowDatabaseStats();
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "✗ Failed to initialize database", e);
            }
        });
    }

    /**
     * Example 2: Show database statistics
     */
    public void exampleShowDatabaseStats() {
        Log.d(TAG, "Fetching database statistics...");
        
        helper.getDatabaseStats(new DatabaseHelper.DatabaseCallback<String>() {
            @Override
            public void onSuccess(String stats) {
                Log.d(TAG, "=== 数据库统计信息 ===");
                Log.d(TAG, stats);
                Log.d(TAG, "====================");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error fetching stats", e);
            }
        });
    }

    /**
     * Example 3: Query admission details for a specific year
     */
    public void exampleQueryByYear(int year) {
        Log.d(TAG, "Querying admission data for year: " + year);
        
        helper.getAdmissionDetailsByYear(year, "河北", "物理组",
            new DatabaseHelper.DatabaseCallback<List<AdmissionDetail>>() {
                @Override
                public void onSuccess(List<AdmissionDetail> details) {
                    Log.d(TAG, String.format("Found %d admission records for %d", 
                            details.size(), year));
                    
                    // Display first 5 records as examples
                    int count = Math.min(5, details.size());
                    Log.d(TAG, "=== Sample Records ===");
                    for (int i = 0; i < count; i++) {
                        Log.d(TAG, "\nRecord " + (i + 1) + ":");
                        Log.d(TAG, details.get(i).toString());
                    }
                    Log.d(TAG, "=====================");
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "Error querying by year", e);
                }
            });
    }

    /**
     * Example 4: Search for majors by keyword
     */
    public void exampleSearchMajors(String keyword) {
        Log.d(TAG, "Searching for majors with keyword: " + keyword);
        
        helper.searchMajors(keyword, new DatabaseHelper.DatabaseCallback<List<Major>>() {
            @Override
            public void onSuccess(List<Major> majors) {
                Log.d(TAG, "Found " + majors.size() + " majors matching '" + keyword + "'");
                
                for (Major major : majors) {
                    Log.d(TAG, String.format("- %s (%s) - %s", 
                            major.getMajorName(), 
                            major.getMajorCode(), 
                            major.getMajorCategory()));
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error searching majors", e);
            }
        });
    }

    /**
     * Example 5: Query using direct database access
     * This example shows how to use the database DAOs directly
     */
    public void exampleDirectDatabaseAccess(Context context) {
        Log.d(TAG, "Using direct database access...");
        
        // Get database instance
        AdmissionDatabase database = AdmissionDatabase.getInstance(context);
        
        // Perform query on background thread
        new Thread(() -> {
            try {
                // Get all universities in Hebei
                List<University> universities = database.universityDao()
                        .getUniversitiesByProvince("河北");
                
                Log.d(TAG, "=== Universities in Hebei ===");
                for (University uni : universities) {
                    Log.d(TAG, String.format("%s (%s) - %s - %s",
                            uni.getUniversityName(),
                            uni.getUniversityCode(),
                            uni.getCity(),
                            uni.getUniversityType()));
                    
                    // Get majors for this university
                    List<Major> majors = database.majorDao()
                            .getMajorsByUniversity(uni.getId());
                    Log.d(TAG, "  Majors: " + majors.size());
                    
                    // Get admission scores for 2024
                    List<AdmissionScore> scores = database.admissionScoreDao()
                            .getScoresByUniversityAndYear(uni.getId(), 2024);
                    Log.d(TAG, "  2024 Scores: " + scores.size());
                }
                Log.d(TAG, "============================");
                
            } catch (Exception e) {
                Log.e(TAG, "Error in direct database access", e);
            }
        }).start();
    }

    /**
     * Example 6: Add new data to the database
     */
    public void exampleAddNewData(Context context) {
        Log.d(TAG, "Adding new data to database...");
        
        AdmissionDatabase database = AdmissionDatabase.getInstance(context);
        
        new Thread(() -> {
            try {
                // Add a new university
                University newUniversity = new University(
                        "河北新大学",
                        "10999",
                        "河北",
                        "石家庄",
                        "Regular",
                        "本科一批"
                );
                
                long universityId = database.universityDao().insert(newUniversity);
                Log.d(TAG, "Inserted new university with ID: " + universityId);
                
                // Add a major for this university
                Major newMajor = new Major(
                        universityId,
                        "人工智能",
                        "080717",
                        "工学",
                        "本科",
                        4
                );
                
                long majorId = database.majorDao().insert(newMajor);
                Log.d(TAG, "Inserted new major with ID: " + majorId);
                
                // Add admission score for 2024
                AdmissionScore newScore = new AdmissionScore(
                        universityId,
                        majorId,
                        2024,
                        "河北",
                        "物理组",
                        550,
                        600,
                        575,
                        8000,
                        12000,
                        50,
                        48
                );
                
                long scoreId = database.admissionScoreDao().insert(newScore);
                Log.d(TAG, "Inserted new admission score with ID: " + scoreId);
                
            } catch (Exception e) {
                Log.e(TAG, "Error adding new data", e);
            }
        }).start();
    }

    /**
     * Example 7: Query scores by score range
     */
    public void exampleQueryByScoreRange(Context context, int minScore, int maxScore) {
        Log.d(TAG, String.format("Querying majors with admission scores %d-%d", 
                minScore, maxScore));
        
        AdmissionDatabase database = AdmissionDatabase.getInstance(context);
        
        new Thread(() -> {
            try {
                List<AdmissionScore> scores = database.admissionScoreDao()
                        .getScoresByScoreRange(minScore, maxScore);
                
                Log.d(TAG, String.format("Found %d matching admission scores", scores.size()));
                
                for (AdmissionScore score : scores) {
                    University uni = database.universityDao()
                            .getUniversityById(score.getUniversityId());
                    Major major = database.majorDao()
                            .getMajorById(score.getMajorId());
                    
                    Log.d(TAG, String.format("%s - %s (%d): 最低分 %d, 平均分 %d",
                            uni.getUniversityName(),
                            major.getMajorName(),
                            score.getYear(),
                            score.getMinimumScore(),
                            score.getAverageScore()));
                }
                
            } catch (Exception e) {
                Log.e(TAG, "Error querying by score range", e);
            }
        }).start();
    }

    /**
     * Example 8: Compare admission scores across years
     */
    public void exampleCompareYears(Context context, long majorId) {
        Log.d(TAG, "Comparing admission scores across years for major ID: " + majorId);
        
        AdmissionDatabase database = AdmissionDatabase.getInstance(context);
        
        new Thread(() -> {
            try {
                Major major = database.majorDao().getMajorById(majorId);
                University uni = database.universityDao()
                        .getUniversityById(major.getUniversityId());
                
                Log.d(TAG, String.format("=== %s - %s ===", 
                        uni.getUniversityName(), major.getMajorName()));
                
                List<Integer> years = database.admissionScoreDao().getAllYears();
                for (int year : years) {
                    List<AdmissionScore> scores = database.admissionScoreDao()
                            .getScoresByMajorAndYear(majorId, year);
                    
                    if (!scores.isEmpty()) {
                        AdmissionScore score = scores.get(0);
                        Log.d(TAG, String.format("%d年: 最低分=%d, 平均分=%d, 最高分=%d, 位次=%d-%d",
                                year,
                                score.getMinimumScore(),
                                score.getAverageScore(),
                                score.getMaximumScore(),
                                score.getRankMinimum(),
                                score.getRankMaximum()));
                    }
                }
                Log.d(TAG, "==========================");
                
            } catch (Exception e) {
                Log.e(TAG, "Error comparing years", e);
            }
        }).start();
    }

    /**
     * Example 9: Clear all database data
     */
    public void exampleClearDatabase() {
        Log.d(TAG, "Clearing all database data...");
        
        helper.clearAllData(new DatabaseHelper.DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "✓ All data cleared successfully!");
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "✗ Failed to clear data", e);
            }
        });
    }

    /**
     * Run all examples in sequence
     */
    public void runAllExamples(Context context) {
        Log.d(TAG, "========================================");
        Log.d(TAG, "Running all database examples...");
        Log.d(TAG, "========================================");
        
        // Example 1: Initialize with sample data
        exampleInitializeDatabase();
        
        // Wait a bit for data to be inserted
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Wait 2 seconds
                
                // Example 2: Show stats (already called in Example 1)
                
                // Example 3: Query by year
                exampleQueryByYear(2024);
                
                Thread.sleep(1000);
                
                // Example 4: Search majors
                exampleSearchMajors("计算机");
                
                Thread.sleep(1000);
                
                // Example 5: Direct database access
                exampleDirectDatabaseAccess(context);
                
                Thread.sleep(1000);
                
                // Example 7: Query by score range
                exampleQueryByScoreRange(context, 550, 600);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Cleanup method
     */
    public void cleanup() {
        helper.shutdown();
    }
}
