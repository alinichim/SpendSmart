package com.example.spendsmart.domain.usecase

import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveExpensesUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    operator fun invoke(): Flow<List<Expense>> = repo.observeAll()
}

class GetExpenseByIdUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    suspend operator fun invoke(id: String): Expense? = repo.getById(id)
}

class UpsertExpenseUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense) = repo.upsert(expense)
}

class DeleteExpenseUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    suspend operator fun invoke(id: String) = repo.delete(id)
}

class SeedSampleExpensesUseCase @Inject constructor(
    private val repo: ExpenseRepository
) {
    suspend operator fun invoke(samples: List<Expense>) = repo.seedIfEmpty(samples)
}
