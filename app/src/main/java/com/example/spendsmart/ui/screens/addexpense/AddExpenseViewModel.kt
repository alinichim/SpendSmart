package com.example.spendsmart.ui.screens.addexpense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory
import com.example.spendsmart.domain.repository.PreferencesRepository
import com.example.spendsmart.domain.usecase.GetExpenseByIdUseCase
import com.example.spendsmart.domain.usecase.UpsertExpenseUseCase
import com.example.spendsmart.R
import com.example.spendsmart.domain.util.InputValidator
import com.example.spendsmart.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExpenseById: GetExpenseByIdUseCase,
    private val upsert: UpsertExpenseUseCase,
    private val prefs: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddExpenseUiState())
    val state: StateFlow<AddExpenseUiState> = _state.asStateFlow()

    private val editId: String? = savedStateHandle.get<String>(Route.AddExpense.ARG_EDIT_ID)

    init {
        viewModelScope.launch {
            prefs.displayCurrency.collect { currency ->
                _state.update { current ->
                    // For a brand-new expense (no editing, no prior user pick), default the
                    // selected currency to whatever the user has set as their display currency.
                    val pickedCurrency =
                        if (current.editingId == null && !current.currencyTouched) currency
                        else current.currency
                    current.copy(displayCurrency = currency, currency = pickedCurrency)
                }
            }
        }
        if (editId != null) {
            viewModelScope.launch {
                val existing = getExpenseById(editId)
                if (existing != null) {
                    _state.update {
                        it.copy(
                            editingId = existing.id,
                            amount = existing.amount.toString().trimEnd('0').trimEnd('.'),
                            category = existing.category,
                            currency = existing.currency,
                            currencyTouched = true,
                            date = existing.date,
                            notes = existing.notes
                        )
                    }
                }
            }
        }
    }

    fun onAmountChange(value: String) {
        _state.update { it.copy(amount = value, amountError = null) }
    }

    fun onCategoryChange(category: ExpenseCategory) {
        _state.update { it.copy(category = category, categoryError = null) }
    }

    fun onCurrencyChange(currency: Currency) {
        _state.update { it.copy(currency = currency, currencyTouched = true) }
    }

    fun onDateChange(date: LocalDate) {
        _state.update { it.copy(date = date) }
    }

    fun onNotesChange(value: String) {
        val truncated = if (value.length > 200) value.substring(0, 200) else value
        _state.update { it.copy(notes = truncated) }
    }

    fun onSubmit(onSaved: () -> Unit) {
        val current = _state.value
        val amount = InputValidator.sanitizeAmount(current.amount)
        val category = current.category
        val amountError = if (amount == null) R.string.error_invalid_amount else null
        val categoryError = if (category == null) R.string.error_pick_category else null
        if (amountError != null || categoryError != null || amount == null || category == null) {
            _state.update {
                it.copy(amountError = amountError, categoryError = categoryError)
            }
            return
        }
        val cleanedNotes = InputValidator.sanitizeNotes(current.notes)
        val toPersist = Expense(
            id = current.editingId ?: UUID.randomUUID().toString(),
            amount = amount,
            currency = current.currency,
            category = category,
            date = current.date,
            notes = cleanedNotes,
            createdAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            upsert(toPersist)
            _state.update { it.copy(isSaving = false, justSaved = true) }
            onSaved()
        }
    }
}
