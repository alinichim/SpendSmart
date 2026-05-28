package com.example.spendsmart.domain.model

data class ExchangeRates(
    val base: Currency,
    val rates: Map<Currency, Double>,
    val lastUpdatedUtc: Long
) {
    fun convert(amount: Double, target: Currency): Double? {
        if (target == base) return amount
        val rate = rates[target] ?: return null
        return amount * rate
    }
}
