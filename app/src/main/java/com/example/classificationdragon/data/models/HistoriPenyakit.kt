package com.example.classificationdragon.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hitoriKlasifikasi")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val diseaseName: String,
    val date: String,
    val imagePath: String
)

