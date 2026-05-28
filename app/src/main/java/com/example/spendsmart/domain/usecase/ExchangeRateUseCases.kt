package com.example.spendsmart.domain.usecase

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExchangeRates
import com.example.spendsmart.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repo: ExchangeRateRepository
) {
    suspend operator fun invoke(base: Currency): Result<ExchangeRates> = repo.fetchLatest(base)
}

class ConvertCurrencyUseCase @Inject constructor() {
    operator fun invoke(rates: ExchangeRates, amount: Double, target: Currency): Double? =
        rates.convert(amount, target)
}
