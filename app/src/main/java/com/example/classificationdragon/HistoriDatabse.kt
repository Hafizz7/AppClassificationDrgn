package com.example.classificationdragon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.classificationdragon.models.HistoryEntity
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoriDatabase :RoomDatabase() {
    abstract fun historyDao(): HistoriDao

    companion object {
        @Volatile
        private var INSTANCE: HistoriDatabase? = null

        fun getDatabase(context: Context): HistoriDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoriDatabase::class.java,
                    "history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
