package com.example.spendsmart.domain.model

import androidx.compose.ui.graphics.Color
import com.example.spendsmart.ui.theme.CategoryEntertainment
import com.example.spendsmart.ui.theme.CategoryFood
import com.example.spendsmart.ui.theme.CategoryHealth
import com.example.spendsmart.ui.theme.CategoryHousing
import com.example.spendsmart.ui.theme.CategoryOther
import com.example.spendsmart.ui.theme.CategoryShopping
import com.example.spendsmart.ui.theme.CategoryTransport

enum class ExpenseCategory(
    val displayName: String,
    val emoji: String,
    val color: Color
) {
    FOOD("Food", "🍔", CategoryFood),
    TRANSPORT("Transport", "🚌", CategoryTransport),
    SHOPPING("Shopping", "🛍️", CategoryShopping),
    ENTERTAINMENT("Entertainment", "🎮", CategoryEntertainment),
    HEALTH("Health", "💊", CategoryHealth),
    HOUSING("Housing", "🏠", CategoryHousing),
    OTHER("Other", "📦", CategoryOther);

    companion object {
        fun fromName(name: String): ExpenseCategory? =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
