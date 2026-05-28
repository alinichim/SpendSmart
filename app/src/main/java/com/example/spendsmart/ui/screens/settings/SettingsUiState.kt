package com.example.spendsmart.ui.screens.settings

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val displayCurrency: Currency = Currency.USD,
    val totalTransactions: Int = 0,
    val totalSpent: Double = 0.0,
    val versionName: String = "1.0"
)
