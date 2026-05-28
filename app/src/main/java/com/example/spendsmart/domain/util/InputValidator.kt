package com.example.spendsmart.domain.util

import com.example.spendsmart.domain.model.ExpenseCategory

object InputValidator {

    private val amountRegex = Regex("^\\d{1,9}(\\.\\d{0,2})?$")
    private const val MAX_NOTES_LENGTH = 200

    fun sanitizeAmount(raw: String): Double? {
        val trimmed = raw.trim()
        if (trimmed.isEmpty()) return null
        if (!amountRegex.matches(trimmed)) return null
        val value = trimmed.toDoubleOrNull() ?: return null
        if (value <= 0.0) return null
        return value
    }

    fun sanitizeNotes(raw: String): String {
        val tabsToSpaces = raw.replace('\t', ' ')
        val noControl = tabsToSpaces.filter { it.code >= 0x20 || it == '\n' }
        val collapsed = noControl.replace(Regex(" +"), " ").trim()
        return if (collapsed.length <= MAX_NOTES_LENGTH) collapsed
        else collapsed.substring(0, MAX_NOTES_LENGTH)
    }

    fun validateCategory(raw: String): Boolean =
        ExpenseCategory.fromName(raw) != null
}
