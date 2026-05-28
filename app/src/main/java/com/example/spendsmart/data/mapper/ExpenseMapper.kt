package com.example.spendsmart.data.mapper

import com.example.spendsmart.data.local.ExpenseEntity
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory
import java.time.LocalDate

fun ExpenseEntity.toDomain(): Expense = Expense(
    id = id,
    amount = amount,
    currency = Currency.fromCode(currency) ?: Currency.USD,
    category = ExpenseCategory.fromName(category) ?: ExpenseCategory.OTHER,
    date = LocalDate.ofEpochDay(dateEpochDay),
    notes = notes,
    createdAt = createdAt
)

fun Expense.toEntity(): ExpenseEntity = ExpenseEntity(
    id = id,
    amount = amount,
    currency = currency.code,
    category = category.name,
    dateEpochDay = date.toEpochDay(),
    notes = notes,
    createdAt = createdAt
)
