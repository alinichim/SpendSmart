package com.example.spendsmart.ui.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.repository.PreferencesRepository
import com.example.spendsmart.domain.usecase.DeleteExpenseUseCase
import com.example.spendsmart.domain.usecase.GetExchangeRatesUseCase
import com.example.spendsmart.domain.usecase.GetExpenseByIdUseCase
import com.example.spendsmart.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getExpenseById: GetExpenseByIdUseCase,
    private val deleteExpense: DeleteExpenseUseCase,
    private val getExchangeRates: GetExchangeRatesUseCase,
    private val prefs: PreferencesRepository
) : ViewModel() {

    private val expenseId: String =
        checkNotNull(savedStateHandle.get<String>(Route.ExpenseDetails.ARG_ID)) {
            "ExpenseDetails requires an id argument"
        }

    private val _state = MutableStateFlow(ExpenseDetailsUiState())
    val state: StateFlow<ExpenseDetailsUiState> = _state.asStateFlow()

    init {
        load()
        viewModelScope.launch {
            prefs.displayCurrency.collect { currency ->
                _state.update { it.copy(displayCurrency = currency) }
                recomputeConverted()
            }
        }
    }

    fun reload() = load()

    private fun load() {
        viewModelScope.launch {
            val found = getExpenseById(expenseId)
            _state.update {
                it.copy(isLoading = false, expense = found, notFound = found == null)
            }
            recomputeConverted()
        }
    }

    private fun recomputeConverted() {
        val s = _state.value
        val expense = s.expense ?: return
        if (expense.currency == s.displayCurrency) {
            _state.update { it.copy(convertedAmount = null) }
            return
        }
        viewModelScope.launch {
            val ratesResult = getExchangeRates(s.displayCurrency)
            val rates = ratesResult.getOrNull()
            val converted = rates?.rates?.get(expense.currency)?.takeIf { it != 0.0 }
                ?.let { expense.amount / it }
            _state.update { it.copy(convertedAmount = converted) }
        }
    }

    fun onDelete(onDone: () -> Unit) {
        viewModelScope.launch {
            deleteExpense(expenseId)
            onDone()
        }
    }
}
