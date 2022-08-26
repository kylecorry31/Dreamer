package com.kylecorry.oneironaut.app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kylecorry.oneironaut.infrastructure.persistence.DreamDao
import com.kylecorry.oneironaut.infrastructure.persistence.DreamEntity

/**
 * The Room database for this app
 */
@Database(
    entities = [DreamEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dreamDao(): DreamDao

    companion object {
        private const val DB_NAME = "oneironaut_db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
//                .addMigrations()
                .build()
        }
    }
}