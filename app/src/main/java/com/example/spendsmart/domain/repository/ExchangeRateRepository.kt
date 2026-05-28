package com.example.spendsmart.domain.repository

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExchangeRates

interface ExchangeRateRepository {
    suspend fun fetchLatest(base: Currency): Result<ExchangeRates>
}
