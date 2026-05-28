package com.example.spendsmart.data.repository

import com.example.spendsmart.data.local.ExpenseDao
import com.example.spendsmart.data.mapper.toDomain
import com.example.spendsmart.data.mapper.toEntity
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun observeAll(): Flow<List<Expense>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): Expense? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(expense: Expense) {
        dao.upsert(expense.toEntity())
    }

    override suspend fun delete(id: String) {
        dao.delete(id)
    }

    override suspend fun seedIfEmpty(samples: List<Expense>) {
        if (dao.count() == 0) {
            dao.upsertAll(samples.map { it.toEntity() })
        }
    }
}
