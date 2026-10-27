package com.example.myapplication.data.repository

import com.example.myapplication.data.local.QueryDao
import com.example.myapplication.data.local.QueryEntity
import kotlinx.coroutines.flow.Flow

class QueryRepository(private val queryDao: QueryDao) {
    val allQueries: Flow<List<QueryEntity>> = queryDao.getAllQueries()

    suspend fun insertQuery(query: QueryEntity) {
        queryDao.insertQuery(query)
    }

    fun getQueriesByStatus(status: String): Flow<List<QueryEntity>> {
        return queryDao.getQueriesByStatus(status)
    }
}