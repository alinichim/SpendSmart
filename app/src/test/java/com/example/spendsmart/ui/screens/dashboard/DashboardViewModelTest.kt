package com.example.spendsmart.ui.screens.dashboard

import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.ExchangeRates
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory
import com.example.spendsmart.domain.repository.PreferencesRepository
import com.example.spendsmart.domain.usecase.DeleteExpenseUseCase
import com.example.spendsmart.domain.usecase.GetExchangeRatesUseCase
import com.example.spendsmart.domain.usecase.ObserveExpensesUseCase
import com.example.spendsmart.domain.usecase.SeedSampleExpensesUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val observe = mockk<ObserveExpensesUseCase>()
    private val delete = mockk<DeleteExpenseUseCase>(relaxed = true)
    private val seed = mockk<SeedSampleExpensesUseCase>(relaxed = true)
    private val getRates = mockk<GetExchangeRatesUseCase>()
    private val prefs = mockk<PreferencesRepository>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { seed.invoke(any()) } just Runs
        every { prefs.themeMode } returns flowOf()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun expense(amount: Double, currency: Currency, cat: ExpenseCategory) = Expense(
        id = java.util.UUID.randomUUID().toString(),
        amount = amount,
        currency = currency,
        category = cat,
        date = LocalDate.now(),
        notes = "",
        createdAt = 0L
    )

    @Test
    fun `converts multi currency totals into display currency`() = runTest {
        // Default = USD, rates with base=USD: 1 USD = 0.5 EUR (so 10 EUR -> 20 USD).
        val rates = ExchangeRates(
            base = Currency.USD,
            rates = mapOf(Currency.USD to 1.0, Currency.EUR to 0.5),
            lastUpdatedUtc = 0L
        )
        val expenses = listOf(
            expense(100.0, Currency.USD, ExpenseCategory.FOOD),       // -> 100 USD
            expense(10.0, Currency.EUR, ExpenseCategory.TRANSPORT)    // -> 20 USD
        )
        every { observe() } returns flowOf(expenses)
        every { prefs.displayCurrency } returns flowOf(Currency.USD)
        coEvery { getRates.invoke(Currency.USD) } returns Result.success(rates)

        val vm = DashboardViewModel(observe, delete, seed, getRates, prefs)
        val state = vm.state.value

        assertTrue(state.ratesAvailable)
        assertNotNull(state.monthTotalConverted)
        assertEquals(120.0, state.monthTotalConverted!!, 0.0001)
        val byCategory = state.categoryBreakdown.associate { it.category to it.amount }
        assertEquals(100.0, byCategory[ExpenseCategory.FOOD]!!, 0.0001)
        assertEquals(20.0, byCategory[ExpenseCategory.TRANSPORT]!!, 0.0001)
    }

    @Test
    fun `returns null total when rates lookup fails`() = runTest {
        every { observe() } returns flowOf(listOf(expense(50.0, Currency.EUR, ExpenseCategory.FOOD)))
        every { prefs.displayCurrency } returns flowOf(Currency.USD)
        coEvery { getRates.invoke(Currency.USD) } returns Result.failure(RuntimeException("nope"))

        val vm = DashboardViewModel(observe, delete, seed, getRates, prefs)
        val state = vm.state.value

        assertFalse(state.ratesAvailable)
        assertNull(state.monthTotalConverted)
        assertTrue(state.categoryBreakdown.isEmpty())
    }

    @Test
    fun `returns null total when any expense lacks a rate`() = runTest {
        val rates = ExchangeRates(
            base = Currency.USD,
            rates = mapOf(Currency.USD to 1.0), // no EUR rate
            lastUpdatedUtc = 0L
        )
        val expenses = listOf(
            expense(100.0, Currency.USD, ExpenseCategory.FOOD),
            expense(10.0, Currency.EUR, ExpenseCategory.TRANSPORT)
        )
        every { observe() } returns flowOf(expenses)
        every { prefs.displayCurrency } returns flowOf(Currency.USD)
        coEvery { getRates.invoke(Currency.USD) } returns Result.success(rates)

        val vm = DashboardViewModel(observe, delete, seed, getRates, prefs)
        val state = vm.state.value

        assertFalse(state.ratesAvailable)
        assertNull(state.monthTotalConverted)
    }
}
