package com.example.spendsmart.domain.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class InputValidatorTest {

    @Test
    fun `sanitizeAmount parses well formed positive values`() {
        assertEquals(12.5, InputValidator.sanitizeAmount("12.50"))
        assertEquals(0.01, InputValidator.sanitizeAmount("0.01"))
        assertEquals(999999999.99, InputValidator.sanitizeAmount("999999999.99"))
    }

    @Test
    fun `sanitizeAmount allows trailing decimal point`() {
        val v = InputValidator.sanitizeAmount("12.")
        assertNotNull(v)
        assertEquals(12.0, v!!, 0.0001)
    }

    @Test
    fun `sanitizeAmount trims surrounding whitespace`() {
        assertEquals(7.0, InputValidator.sanitizeAmount("  7  "))
    }

    @Test
    fun `sanitizeAmount rejects negative and zero`() {
        assertNull(InputValidator.sanitizeAmount("-1.00"))
        assertNull(InputValidator.sanitizeAmount("0"))
        assertNull(InputValidator.sanitizeAmount("0.00"))
    }

    @Test
    fun `sanitizeAmount rejects garbage and SQL like input`() {
        assertNull(InputValidator.sanitizeAmount(""))
        assertNull(InputValidator.sanitizeAmount("abc"))
        assertNull(InputValidator.sanitizeAmount("1; DROP TABLE expenses;"))
        assertNull(InputValidator.sanitizeAmount("1,000.00"))
        assertNull(InputValidator.sanitizeAmount("1.234"))
        assertNull(InputValidator.sanitizeAmount("9999999999"))
    }

    @Test
    fun `sanitizeNotes strips control chars`() {
        val nul = Char(0x00)
        val bel = Char(0x07)
        val us = Char(0x1F)
        val raw = "Hello${nul}${bel}World${us}Bye!"
        val cleaned = InputValidator.sanitizeNotes(raw)
        assertFalse("Should not contain NUL", cleaned.contains(nul))
        assertFalse("Should not contain BEL", cleaned.contains(bel))
        assertFalse("Should not contain US", cleaned.contains(us))
        assertTrue(cleaned.contains("Hello"))
        assertTrue(cleaned.contains("World"))
    }

    @Test
    fun `sanitizeNotes caps length at 200`() {
        val long = "a".repeat(500)
        assertEquals(200, InputValidator.sanitizeNotes(long).length)
    }

    @Test
    fun `sanitizeNotes collapses whitespace`() {
        assertEquals("a b c", InputValidator.sanitizeNotes("a    b\tc"))
    }

    @Test
    fun `validateCategory matches known names case insensitively`() {
        assertTrue(InputValidator.validateCategory("FOOD"))
        assertTrue(InputValidator.validateCategory("food"))
        assertTrue(InputValidator.validateCategory("Housing"))
        assertFalse(InputValidator.validateCategory("Groceries"))
        assertFalse(InputValidator.validateCategory(""))
    }
}
