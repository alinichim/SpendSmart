package com.example.spendsmart.data.repository

import app.cash.turbine.test
import com.example.spendsmart.data.local.ExpenseDao
import com.example.spendsmart.data.local.ExpenseEntity
import com.example.spendsmart.data.mapper.toEntity
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.domain.model.ExpenseCategory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ExpenseRepositoryImplTest {

    private val dao = mockk<ExpenseDao>(relaxed = true)
    private val repo = ExpenseRepositoryImpl(dao)

    private val sampleEntity = ExpenseEntity(
        id = "abc",
        amount = 12.50,
        currency = "EUR",
        category = "FOOD",
        dateEpochDay = LocalDate.of(2025, 1, 5).toEpochDay(),
        notes = "Lunch",
        createdAt = 1_700_000_000L
    )

    @Test
    fun `observeAll maps entities to domain models`() = runTest {
        every { dao.observeAll() } returns flowOf(listOf(sampleEntity))
        repo.observeAll().test {
            val list = awaitItem()
            assertEquals(1, list.size)
            assertEquals(ExpenseCategory.FOOD, list[0].category)
            assertEquals(Currency.EUR, list[0].currency)
            assertEquals(12.50, list[0].amount, 0.0001)
            assertEquals(LocalDate.of(2025, 1, 5), list[0].date)
            awaitComplete()
        }
    }

    @Test
    fun `getById returns null when missing`() = runTest {
        coEvery { dao.getById("missing") } returns null
        assertNull(repo.getById("missing"))
    }

    @Test
    fun `upsert forwards mapped entity to dao`() = runTest {
        val expense = Expense(
            id = "x",
            amount = 5.0,
            currency = Currency.GBP,
            category = ExpenseCategory.TRANSPORT,
            date = LocalDate.of(2024, 12, 1),
            notes = "Bus",
            createdAt = 1L
        )
        repo.upsert(expense)
        coVerify { dao.upsert(expense.toEntity()) }
    }

    @Test
    fun `seedIfEmpty only inserts when count is zero`() = runTest {
        coEvery { dao.count() } returns 0
        val seeds = listOf(
            Expense("a", 1.0, Currency.USD, ExpenseCategory.OTHER, LocalDate.now(), "x", 0L)
        )
        repo.seedIfEmpty(seeds)
        coVerify { dao.upsertAll(seeds.map { it.toEntity() }) }
    }

    @Test
    fun `seedIfEmpty no ops when count is non zero`() = runTest {
        coEvery { dao.count() } returns 3
        repo.seedIfEmpty(emptyList())
        coVerify(exactly = 0) { dao.upsertAll(any()) }
    }
}
