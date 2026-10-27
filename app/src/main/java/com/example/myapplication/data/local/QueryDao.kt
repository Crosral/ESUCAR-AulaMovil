package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {

    // Retornar Long obliga a KSP a generar la firma JVM idónea
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: QueryEntity): Long

    // Retornar Int (filas afectadas) evita el error 'unexpected jvm signature V' en KSP
    @Update
    suspend fun updateQuery(query: QueryEntity): Int

    @Query("SELECT * FROM student_queries ORDER BY timestamp DESC")
    fun getAllQueries(): Flow<List<QueryEntity>>

    @Query("SELECT * FROM student_queries WHERE status = :statusFilter ORDER BY timestamp DESC")
    fun getQueriesByStatus(statusFilter: String): Flow<List<QueryEntity>>
}