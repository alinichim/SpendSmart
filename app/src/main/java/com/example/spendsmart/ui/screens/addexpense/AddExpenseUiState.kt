package com.example.spendsmart.ui.screens.addexpense

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExpenseCategory
import java.time.LocalDate

data class AddExpenseUiState(
    val editingId: String? = null,
    val amount: String = "",
    val category: ExpenseCategory? = null,
    val date: LocalDate = LocalDate.now(),
    val notes: String = "",
    val currency: Currency = Currency.USD,
    val currencyTouched: Boolean = false,
    val displayCurrency: Currency = Currency.USD,
    val amountError: String? = null,
    val categoryError: String? = null,
    val isSaving: Boolean = false,
    val justSaved: Boolean = false
) {
    val isEdit: Boolean get() = editingId != null
    val notesCharCount: Int get() = notes.length
    val isFormValid: Boolean
        get() = amountError == null && categoryError == null && category != null && amount.isNotBlank()
}
