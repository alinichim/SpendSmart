package com.example.spendsmart.ui.screens.dashboard

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory

data class DashboardUiState(
    val isLoading: Boolean = true,
    val expenses: List<Expense> = emptyList(),
    val displayCurrency: Currency = Currency.USD,
    val monthLabel: String = "",
    val monthTotalConverted: Double? = null,
    val monthTransactionCount: Int = 0,
    val topCategory: ExpenseCategory? = null,
    val categoryBreakdown: List<CategoryAmount> = emptyList(),
    val ratesAvailable: Boolean = false
)

data class CategoryAmount(
    val category: ExpenseCategory,
    val amount: Double
)
