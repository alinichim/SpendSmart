package com.example.spendsmart.ui.screens.details

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense

data class ExpenseDetailsUiState(
    val isLoading: Boolean = true,
    val expense: Expense? = null,
    val displayCurrency: Currency = Currency.USD,
    val convertedAmount: Double? = null,
    val notFound: Boolean = false
)
