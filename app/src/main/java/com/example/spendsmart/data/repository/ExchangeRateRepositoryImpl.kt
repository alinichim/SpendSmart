package com.example.spendsmart.data.repository

import com.example.spendsmart.data.mapper.toDomain
import com.example.spendsmart.data.remote.ExchangeRateApi
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExchangeRates
import com.example.spendsmart.domain.repository.ExchangeRateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRateRepository {

    override suspend fun fetchLatest(base: Currency): Result<ExchangeRates> = runCatching {
        val dto = api.latest(base.code)
        if (dto.result != null && dto.result != "success") {
            throw IllegalStateException("Exchange API error: ${dto.errorType ?: "unknown"}")
        }
        dto.toDomain(fallbackBase = base)
    }
}
