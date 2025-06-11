package com.example.classificationdragon.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.classificationdragon.data.models.HistoryEntity

@Dao
interface HistoriDao {
    @Insert
    suspend fun insertHistory(history: HistoryEntity)

    @Delete
    suspend fun  delete(history: HistoryEntity)

    @Query("SELECT * FROM hitoriKlasifikasi ORDER BY id DESC")
    suspend fun getAllHistory(): List<HistoryEntity>
}
