package com.openpositioning.PositionMe.data.local.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class for database operations.
 * Provides convenient methods to perform database operations on background threads
 * with callbacks on the main thread.
 *
 * @author PositionMe Team
 */
public class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    private final AdmissionDatabase database;
    private final ExecutorService executorService;
    private final Handler mainHandler;
    private final Random random;

    public DatabaseHelper(Context context) {
        this.database = AdmissionDatabase.getInstance(context);
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.random = new Random(12345); // Seeded random for reproducible sample data
    }

    /**
     * Listener interface for database operations.
     */
    public interface DatabaseCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    /**
     * Insert sample data for Hebei Province physics group.
     * This includes universities, majors, and admission scores for recent years.
     */
    public void insertSampleData(DatabaseCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                // Insert sample universities in Hebei
                List<University> universities = new ArrayList<>();
                universities.add(new University("河北大学", "10075", "河北", "保定", "Regular", "本科一批"));
                universities.add(new University("燕山大学", "10216", "河北", "秦皇岛", "Regular", "本科一批"));
                universities.add(new University("河北工业大学", "10080", "河北", "天津", "211", "本科一批"));
                universities.add(new University("华北电力大学(保定)", "10079", "河北", "保定", "211", "本科一批"));
                universities.add(new University("河北师范大学", "10094", "河北", "石家庄", "Regular", "本科一批"));
                universities.add(new University("河北医科大学", "10089", "河北", "石家庄", "Regular", "本科一批"));
                universities.add(new University("石家庄铁道大学", "10107", "河北", "石家庄", "Regular", "本科一批"));
                universities.add(new University("河北科技大学", "10082", "河北", "石家庄", "Regular", "本科一批"));
                universities.add(new University("河北农业大学", "10086", "河北", "保定", "Regular", "本科一批"));
                universities.add(new University("河北经贸大学", "10077", "河北", "石家庄", "Regular", "本科二批"));

                database.universityDao().insertAll(universities);
                Log.d(TAG, "Inserted " + universities.size() + " universities");

                // Get inserted universities for foreign key references
                List<University> insertedUniversities = database.universityDao().getAllUniversities();

                // Insert sample majors
                List<Major> majors = new ArrayList<>();
                for (University uni : insertedUniversities) {
                    // Add some common physics-related majors
                    majors.add(new Major(uni.getId(), "计算机科学与技术", "080901", "工学", "本科", 4));
                    majors.add(new Major(uni.getId(), "软件工程", "080902", "工学", "本科", 4));
                    majors.add(new Major(uni.getId(), "电子信息工程", "080701", "工学", "本科", 4));
                    majors.add(new Major(uni.getId(), "自动化", "080801", "工学", "本科", 4));
                    majors.add(new Major(uni.getId(), "机械设计制造及其自动化", "080202", "工学", "本科", 4));
                    
                    // Limit to first 3 universities to keep data manageable
                    if (majors.size() >= 15) break;
                }

                database.majorDao().insertAll(majors);
                Log.d(TAG, "Inserted " + majors.size() + " majors");

                // Get inserted majors for foreign key references
                List<Major> insertedMajors = database.majorDao().getAllMajors();

                // Insert sample admission scores for recent 3 years (2022, 2023, 2024)
                List<AdmissionScore> scores = new ArrayList<>();
                int[] years = {2022, 2023, 2024};
                
                for (Major major : insertedMajors) {
                    for (int year : years) {
                        // Generate realistic sample scores based on university ranking
                        University uni = database.universityDao().getUniversityById(major.getUniversityId());
                        int baseScore = getBaseScoreForUniversity(uni.getUniversityType(), year);
                        
                        int minScore = baseScore + random.nextInt(20);
                        int maxScore = minScore + random.nextInt(30) + 20;
                        int avgScore = (minScore + maxScore) / 2;
                        int rankMin = 5000 + random.nextInt(10000);
                        int rankMax = rankMin + random.nextInt(5000) + 2000;
                        int planned = 30 + random.nextInt(50);
                        
                        // Calculate actual enrollment: typically within 2 of planned enrollment
                        // Ensure it's between (planned-2) and (planned+2), and never negative
                        int enrollmentVariation = random.nextInt(5) - 2; // Range: -2 to 2
                        int actual = Math.max(0, Math.max(planned - 2, Math.min(planned + 2, planned + enrollmentVariation)));

                        scores.add(new AdmissionScore(
                                major.getUniversityId(),
                                major.getId(),
                                year,
                                "河北",
                                "物理组",
                                minScore,
                                maxScore,
                                avgScore,
                                rankMin,
                                rankMax,
                                planned,
                                actual
                        ));
                    }
                }

                database.admissionScoreDao().insertAll(scores);
                Log.d(TAG, "Inserted " + scores.size() + " admission scores");

                mainHandler.post(() -> callback.onSuccess(null));
            } catch (Exception e) {
                Log.e(TAG, "Error inserting sample data", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    /**
     * Get base score based on university type and year.
     */
    private int getBaseScoreForUniversity(String type, int year) {
        int baseScore = 500; // Default for regular universities
        
        if ("985".equals(type)) {
            baseScore = 620;
        } else if ("211".equals(type)) {
            baseScore = 570;
        } else if ("Double First-Class".equals(type)) {
            baseScore = 590;
        }
        
        // Add some variation based on year
        baseScore += (year - 2022) * 5;
        
        return baseScore;
    }

    /**
     * Get all admission details for a specific year and province.
     */
    public void getAdmissionDetailsByYear(int year, String province, String subjectGroup,
                                          DatabaseCallback<List<AdmissionDetail>> callback) {
        executorService.execute(() -> {
            try {
                List<AdmissionScore> scores = database.admissionScoreDao()
                        .getScoresByProvinceSubjectAndYear(province, subjectGroup, year);
                
                List<AdmissionDetail> details = new ArrayList<>();
                for (AdmissionScore score : scores) {
                    University uni = database.universityDao().getUniversityById(score.getUniversityId());
                    Major major = database.majorDao().getMajorById(score.getMajorId());
                    
                    details.add(new AdmissionDetail(
                            uni.getUniversityName(),
                            uni.getUniversityType(),
                            major.getMajorName(),
                            major.getMajorCategory(),
                            score.getYear(),
                            score.getMinimumScore(),
                            score.getMaximumScore(),
                            score.getAverageScore(),
                            score.getRankMinimum(),
                            score.getRankMaximum(),
                            score.getPlannedEnrollment(),
                            score.getActualEnrollment()
                    ));
                }
                
                mainHandler.post(() -> callback.onSuccess(details));
            } catch (Exception e) {
                Log.e(TAG, "Error getting admission details", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    /**
     * Search majors by name pattern.
     */
    public void searchMajors(String keyword, DatabaseCallback<List<Major>> callback) {
        executorService.execute(() -> {
            try {
                List<Major> majors = database.majorDao().searchMajorsByName("%" + keyword + "%");
                mainHandler.post(() -> callback.onSuccess(majors));
            } catch (Exception e) {
                Log.e(TAG, "Error searching majors", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    /**
     * Get database statistics.
     */
    public void getDatabaseStats(DatabaseCallback<String> callback) {
        executorService.execute(() -> {
            try {
                int uniCount = database.universityDao().getCount();
                int majorCount = database.majorDao().getCount();
                int scoreCount = database.admissionScoreDao().getCount();
                List<Integer> years = database.admissionScoreDao().getAllYears();
                
                String stats = String.format(
                        "数据库统计:\n" +
                        "大学数量: %d\n" +
                        "专业数量: %d\n" +
                        "录取分数记录: %d\n" +
                        "年份: %s",
                        uniCount, majorCount, scoreCount,
                        years.toString()
                );
                
                mainHandler.post(() -> callback.onSuccess(stats));
            } catch (Exception e) {
                Log.e(TAG, "Error getting database stats", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    /**
     * Clear all data from the database.
     */
    public void clearAllData(DatabaseCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                database.admissionScoreDao().deleteAll();
                database.majorDao().deleteAll();
                database.universityDao().deleteAll();
                mainHandler.post(() -> callback.onSuccess(null));
            } catch (Exception e) {
                Log.e(TAG, "Error clearing data", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    /**
     * Shutdown the executor service.
     */
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
