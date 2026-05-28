package com.example.spendsmart.domain.repository

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val themeMode: Flow<ThemeMode>
    val displayCurrency: Flow<Currency>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setDisplayCurrency(currency: Currency)
}
