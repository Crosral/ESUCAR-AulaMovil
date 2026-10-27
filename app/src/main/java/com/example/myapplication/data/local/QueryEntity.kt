package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_queries")
data class QueryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectName: String,
    val unitName: String = "General",
    val resourceTitle: String = "Consulta General",
    val subjectText: String,
    val detailText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pendiente"
)