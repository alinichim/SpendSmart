package com.example.spendsmart.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExchangeRates
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.repository.PreferencesRepository
import com.example.spendsmart.domain.sample.SampleExpenses
import com.example.spendsmart.domain.usecase.DeleteExpenseUseCase
import com.example.spendsmart.domain.usecase.GetExchangeRatesUseCase
import com.example.spendsmart.domain.usecase.ObserveExpensesUseCase
import com.example.spendsmart.domain.usecase.SeedSampleExpensesUseCase
import com.example.spendsmart.ui.util.formatMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val observeExpenses: ObserveExpensesUseCase,
    private val deleteExpense: DeleteExpenseUseCase,
    private val seedSamples: SeedSampleExpensesUseCase,
    private val getExchangeRates: GetExchangeRatesUseCase,
    private val prefs: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    // null = not loaded yet, Result holds the most recent attempt
    private val rates = MutableStateFlow<Result<ExchangeRates>?>(null)

    init {
        viewModelScope.launch { seedSamples(SampleExpenses.build()) }

        // Re-fetch rates whenever the default display currency changes.
        viewModelScope.launch {
            prefs.displayCurrency.collect { currency ->
                rates.value = getExchangeRates(currency)
            }
        }

        viewModelScope.launch {
            combine(observeExpenses(), prefs.displayCurrency, rates) { expenses, currency, rateResult ->
                buildState(expenses, currency, rateResult)
            }.collect { _state.value = it }
        }
    }

    fun onDelete(id: String) {
        viewModelScope.launch { deleteExpense(id) }
    }

    private fun buildState(
        expenses: List<Expense>,
        currency: Currency,
        rateResult: Result<ExchangeRates>?
    ): DashboardUiState {
        val today = LocalDate.now()
        val sameMonth = expenses.filter {
            it.date.year == today.year && it.date.month == today.month
        }

        val rates = rateResult?.getOrNull()
        val ratesAvailable = rates != null

        // Try to convert every expense in the current month to the default currency.
        // If any rate is missing, treat the whole conversion as unavailable.
        val convertedAll = if (rates != null) {
            sameMonth.map { it to convert(it.amount, it.currency, rates, currency) }
        } else emptyList()
        val allConverted = ratesAvailable && convertedAll.all { it.second != null }

        val converted = if (allConverted) convertedAll.map { (e, v) -> e to v!! } else emptyList()
        val monthTotal = if (allConverted) converted.sumOf { it.second } else null
        val breakdown = if (allConverted) {
            converted.groupBy { (e, _) -> e.category }
                .map { (cat, items) -> CategoryAmount(cat, items.sumOf { it.second }) }
                .sortedByDescending { it.amount }
        } else emptyList()
        val top = breakdown.firstOrNull()?.category

        return DashboardUiState(
            isLoading = false,
            expenses = expenses,
            displayCurrency = currency,
            monthLabel = formatMonth(today),
            monthTotalConverted = monthTotal,
            monthTransactionCount = sameMonth.size,
            topCategory = top,
            categoryBreakdown = breakdown,
            ratesAvailable = allConverted
        )
    }

    private fun convert(
        amount: Double,
        from: Currency,
        rates: ExchangeRates,
        default: Currency
    ): Double? {
        if (from == default) return amount
        // Rates are fetched with base = `default`, so 1 default = rates[from] of `from`.
        // To go from `from` -> `default`: divide by the rate of `from`.
        val rate = rates.rates[from] ?: return null
        if (rate == 0.0) return null
        return amount / rate
    }
}
