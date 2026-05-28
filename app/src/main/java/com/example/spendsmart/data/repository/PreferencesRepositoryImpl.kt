package com.example.spendsmart.data.repository

import com.example.spendsmart.data.prefs.SecurePreferences
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ThemeMode
import com.example.spendsmart.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val prefs: SecurePreferences
) : PreferencesRepository {

    override val themeMode: Flow<ThemeMode> = prefs.observe().map { prefs.getThemeMode() }
    override val displayCurrency: Flow<Currency> = prefs.observe().map { prefs.getDisplayCurrency() }

    override suspend fun setThemeMode(mode: ThemeMode) = prefs.setThemeMode(mode)
    override suspend fun setDisplayCurrency(currency: Currency) = prefs.setDisplayCurrency(currency)
}
