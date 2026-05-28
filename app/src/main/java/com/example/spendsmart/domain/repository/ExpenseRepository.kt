package com.example.spendsmart.domain.repository

import com.example.spendsmart.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun observeAll(): Flow<List<Expense>>
    suspend fun getById(id: String): Expense?
    suspend fun upsert(expense: Expense)
    suspend fun delete(id: String)
    suspend fun seedIfEmpty(samples: List<Expense>)
}
