package com.example.spendsmart.data.mapper

import com.example.spendsmart.data.remote.ExchangeRateResponseDto
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExchangeRates

fun ExchangeRateResponseDto.toDomain(fallbackBase: Currency): ExchangeRates {
    val base = baseCode?.let { Currency.fromCode(it) } ?: fallbackBase
    val rates = conversionRates.orEmpty()
        .mapNotNull { (code, rate) ->
            val cur = Currency.fromCode(code) ?: return@mapNotNull null
            cur to rate
        }
        .toMap()
    return ExchangeRates(
        base = base,
        rates = rates,
        lastUpdatedUtc = (timeLastUpdateUnix ?: 0L) * 1000L
    )
}
