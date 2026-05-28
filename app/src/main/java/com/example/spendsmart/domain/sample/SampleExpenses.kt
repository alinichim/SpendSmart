package com.example.spendsmart.domain.sample

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory
import java.time.LocalDate
import java.util.UUID

object SampleExpenses {
    fun build(today: LocalDate = LocalDate.now()): List<Expense> {
        val now = System.currentTimeMillis()
        return listOf(
            sample(12.50, ExpenseCategory.FOOD, today.minusDays(0), "Lunch at cafe", now),
            sample(8.50, ExpenseCategory.FOOD, today.minusDays(1), "Morning coffee", now),
            sample(32.00, ExpenseCategory.FOOD, today.minusDays(2), "Groceries", now),
            sample(45.00, ExpenseCategory.TRANSPORT, today.minusDays(3), "Monthly bus pass", now),
            sample(18.00, ExpenseCategory.TRANSPORT, today.minusDays(4), "Uber ride", now),
            sample(89.99, ExpenseCategory.SHOPPING, today.minusDays(5), "New sneakers", now),
            sample(15.00, ExpenseCategory.ENTERTAINMENT, today.minusDays(6), "Movie ticket", now),
            sample(60.00, ExpenseCategory.ENTERTAINMENT, today.minusDays(7), "Concert", now),
            sample(25.00, ExpenseCategory.HEALTH, today.minusDays(8), "Vitamins & supplements", now),
            sample(800.00, ExpenseCategory.HOUSING, today.minusDays(9), "Monthly rent", now)
        )
    }

    private fun sample(
        amount: Double,
        category: ExpenseCategory,
        date: LocalDate,
        notes: String,
        createdAt: Long
    ) = Expense(
        id = UUID.randomUUID().toString(),
        amount = amount,
        currency = Currency.USD,
        category = category,
        date = date,
        notes = notes,
        createdAt = createdAt
    )
}
