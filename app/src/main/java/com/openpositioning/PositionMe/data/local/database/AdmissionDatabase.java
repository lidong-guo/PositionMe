package com.openpositioning.PositionMe.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room Database class for university admission scores.
 * This database stores information about universities, majors, and their admission scores
 * for Hebei Province physics group (物理组) over the past three years.
 *
 * @author PositionMe Team
 */
@Database(entities = {University.class, Major.class, AdmissionScore.class}, version = 1, exportSchema = false)
public abstract class AdmissionDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "hebei_admission_scores.db";
    private static volatile AdmissionDatabase INSTANCE;

    public abstract UniversityDao universityDao();
    public abstract MajorDao majorDao();
    public abstract AdmissionScoreDao admissionScoreDao();

    /**
     * Get the singleton instance of the database.
     * 
     * @param context Application context
     * @return Database instance
     */
    public static AdmissionDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AdmissionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AdmissionDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Close the database instance.
     */
    public static void closeDatabase() {
        if (INSTANCE != null && INSTANCE.isOpen()) {
            INSTANCE.close();
            INSTANCE = null;
        }
    }
}
