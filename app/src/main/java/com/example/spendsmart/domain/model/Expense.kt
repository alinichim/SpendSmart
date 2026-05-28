package com.example.spendsmart.domain.model

import java.time.LocalDate

data class Expense(
    val id: String,
    val amount: Double,
    val currency: Currency,
    val category: ExpenseCategory,
    val date: LocalDate,
    val notes: String,
    val createdAt: Long
)
