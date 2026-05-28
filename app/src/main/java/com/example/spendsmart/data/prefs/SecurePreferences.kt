package com.example.spendsmart.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.spendsmart.BuildConfig
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurePreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    init {
        // Keep the encrypted store in sync with the build-time key from local.properties.
        // Rebuilding with a new key in local.properties replaces the old one transparently.
        val buildKey = BuildConfig.EXCHANGE_RATE_API_KEY
        if (buildKey.isNotBlank() && prefs.getString(KEY_API_KEY, "") != buildKey) {
            prefs.edit().putString(KEY_API_KEY, buildKey).apply()
        }
    }

    fun getApiKey(): String = prefs.getString(KEY_API_KEY, "") ?: ""

    fun getThemeMode(): ThemeMode {
        val raw = prefs.getString(KEY_THEME, ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        return runCatching { ThemeMode.valueOf(raw) }.getOrDefault(ThemeMode.SYSTEM)
    }

    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME, mode.name).apply()
    }

    fun getDisplayCurrency(): Currency {
        val raw = prefs.getString(KEY_CURRENCY, Currency.USD.code) ?: Currency.USD.code
        return Currency.fromCode(raw) ?: Currency.USD
    }

    fun setDisplayCurrency(currency: Currency) {
        prefs.edit().putString(KEY_CURRENCY, currency.code).apply()
    }

    fun observe(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key != null) trySend(key)
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend("__init__")
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    companion object {
        private const val FILE_NAME = "spendsmart_secure_prefs"
        private const val KEY_API_KEY = "exchange_rate_api_key"
        private const val KEY_THEME = "theme_mode"
        private const val KEY_CURRENCY = "display_currency"
    }
}
