package com.example.spendsmart.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spendsmart.BuildConfig
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ThemeMode
import com.example.spendsmart.domain.repository.PreferencesRepository
import com.example.spendsmart.domain.usecase.ObserveExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: PreferencesRepository,
    observeExpenses: ObserveExpensesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState(versionName = BuildConfig.VERSION_NAME))
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                prefs.themeMode,
                prefs.displayCurrency,
                observeExpenses()
            ) { theme, currency, expenses ->
                SettingsUiState(
                    themeMode = theme,
                    displayCurrency = currency,
                    totalTransactions = expenses.size,
                    totalSpent = expenses.sumOf { it.amount },
                    versionName = BuildConfig.VERSION_NAME
                )
            }.collect { _state.value = it }
        }
    }

    fun onThemeChange(mode: ThemeMode) {
        viewModelScope.launch { prefs.setThemeMode(mode) }
    }

    fun onCurrencyChange(currency: Currency) {
        viewModelScope.launch { prefs.setDisplayCurrency(currency) }
    }
}
