package com.example.spendsmart.ui.screens.addexpense

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory
import com.example.spendsmart.domain.repository.PreferencesRepository
import com.example.spendsmart.domain.usecase.GetExpenseByIdUseCase
import com.example.spendsmart.domain.usecase.UpsertExpenseUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddExpenseViewModelTest {

    private val getById = mockk<GetExpenseByIdUseCase>()
    private val upsert = mockk<UpsertExpenseUseCase>()
    private val prefs = mockk<PreferencesRepository>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { prefs.displayCurrency } returns flowOf(Currency.USD)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun newVm(editId: String? = null): AddExpenseViewModel {
        val handle = SavedStateHandle(if (editId != null) mapOf("editId" to editId) else emptyMap())
        return AddExpenseViewModel(handle, getById, upsert, prefs)
    }

    @Test
    fun `invalid amount surfaces amount error and skips upsert`() = runTest {
        val vm = newVm()
        vm.state.test {
            skipItems(1)
            vm.onAmountChange("abc")
            assertEquals("abc", awaitItem().amount)
            vm.onCategoryChange(ExpenseCategory.FOOD)
            assertEquals(ExpenseCategory.FOOD, awaitItem().category)
            vm.onSubmit { /* should not be called */ }
            val errored = awaitItem()
            assertNotNull(errored.amountError)
            assertNull(errored.categoryError)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 0) { upsert(any()) }
    }

    @Test
    fun `valid submit persists selected currency`() = runTest {
        coEvery { upsert(any<Expense>()) } just Runs
        val vm = newVm()
        var called = false

        vm.onAmountChange("12.50")
        vm.onCategoryChange(ExpenseCategory.FOOD)
        vm.onCurrencyChange(Currency.EUR)
        vm.onNotesChange("  Lunch  ")
        vm.onSubmit { called = true }

        coVerify {
            upsert(match { exp ->
                exp.amount == 12.50 &&
                    exp.category == ExpenseCategory.FOOD &&
                    exp.currency == Currency.EUR &&
                    exp.notes == "Lunch"
            })
        }
        assertTrue(called)
        assertTrue(vm.state.value.justSaved)
    }

    @Test
    fun `new expense defaults currency to user display currency`() = runTest {
        coEvery { prefs.displayCurrency } returns flowOf(Currency.GBP)
        val vm = newVm()
        assertEquals(Currency.GBP, vm.state.value.currency)
    }

    @Test
    fun `edit mode prefills currency and other fields`() = runTest {
        val existing = Expense(
            id = "id-1",
            amount = 8.0,
            currency = Currency.JPY,
            category = ExpenseCategory.SHOPPING,
            date = java.time.LocalDate.of(2025, 3, 1),
            notes = "tee shirt",
            createdAt = 1L
        )
        coEvery { getById("id-1") } returns existing
        val vm = newVm(editId = "id-1")
        val s = vm.state.value
        assertEquals("id-1", s.editingId)
        assertEquals(Currency.JPY, s.currency)
        assertEquals(ExpenseCategory.SHOPPING, s.category)
        assertEquals("tee shirt", s.notes)
    }
}
