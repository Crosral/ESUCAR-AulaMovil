package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.QueryEntity
import com.example.myapplication.data.repository.QueryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class QueryViewModel(private val repository: QueryRepository) : ViewModel() {

    val allQueries: Flow<List<QueryEntity>> = repository.allQueries

    fun sendQuery(
        subject: String,
        unit: String,
        resource: String,
        title: String,
        detail: String
    ) {
        viewModelScope.launch {
            val newQuery = QueryEntity(
                subjectName = subject,
                unitName = unit,
                resourceTitle = resource,
                subjectText = title,
                detailText = detail
            )
            repository.insertQuery(newQuery)
        }
    }
}