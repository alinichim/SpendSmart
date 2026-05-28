package com.example.spendsmart.data.repository

import com.example.spendsmart.data.remote.ExchangeRateApi
import com.example.spendsmart.data.remote.ExchangeRateResponseDto
import com.example.spendsmart.domain.model.Currency
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ExchangeRateRepositoryImplTest {

    private val api = mockk<ExchangeRateApi>()
    private val repo = ExchangeRateRepositoryImpl(api)

    @Test
    fun `success path returns mapped rates`() = runTest {
        coEvery { api.latest("USD") } returns ExchangeRateResponseDto(
            result = "success",
            baseCode = "USD",
            timeLastUpdateUnix = 1_700_000_000L,
            conversionRates = mapOf("USD" to 1.0, "EUR" to 0.92, "WAT" to 9.9),
            errorType = null
        )
        val result = repo.fetchLatest(Currency.USD)
        assertTrue(result.isSuccess)
        val rates = result.getOrThrow()
        assertEquals(Currency.USD, rates.base)
        assertEquals(0.92, rates.rates[Currency.EUR]!!, 0.0001)
        // Unknown code "WAT" is filtered out
        assertEquals(2, rates.rates.size)
    }

    @Test
    fun `api error string returns failure result`() = runTest {
        coEvery { api.latest("USD") } returns ExchangeRateResponseDto(
            result = "error",
            baseCode = null,
            timeLastUpdateUnix = null,
            conversionRates = null,
            errorType = "invalid-key"
        )
        val result = repo.fetchLatest(Currency.USD)
        assertTrue(result.isFailure)
    }

    @Test
    fun `network exception bubbles up as failure`() = runTest {
        coEvery { api.latest("EUR") } throws IOException("boom")
        val result = repo.fetchLatest(Currency.EUR)
        assertTrue(result.isFailure)
    }
}
