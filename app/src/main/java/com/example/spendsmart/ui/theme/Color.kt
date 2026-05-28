package com.example.spendsmart.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand
val Violet600 = Color(0xFF7C3AED)
val Purple700 = Color(0xFF9333EA)
val Violet50 = Color(0xFFF5F3FF)
val Violet100 = Color(0xFFEDE9FE)
val Violet500 = Color(0xFF8B5CF6)

// Neutrals (light)
val NeutralBackground = Color(0xFFF9F9FB)
val NeutralSurface = Color(0xFFFFFFFF)
val NeutralSurfaceVariant = Color(0xFFF3F3F5)
val NeutralBorder = Color(0xFFE5E5EA)
val NeutralOnSurface = Color(0xFF111111)
val NeutralOnSurfaceMuted = Color(0xFF717182)

// Neutrals (dark)
val DarkBackground = Color(0xFF0F0F14)
val DarkSurface = Color(0xFF1A1A24)
val DarkSurfaceVariant = Color(0xFF22222E)
val DarkBorder = Color(0xFF2E2E3A)
val DarkOnSurface = Color(0xFFF5F5F7)
val DarkOnSurfaceMuted = Color(0xFFA1A1AA)

// Status
val Danger = Color(0xFFD4183D)

// Category accents (from Figma)
val CategoryFood = Color(0xFFFF6B6B)
val CategoryTransport = Color(0xFF4ECDC4)
val CategoryShopping = Color(0xFF45B7D1)
val CategoryEntertainment = Color(0xFFA78BFA)
val CategoryHealth = Color(0xFF34D399)
val CategoryHousing = Color(0xFFFBBF24)
val CategoryOther = Color(0xFF9CA3AF)

val SpendSmartGradient: Brush = Brush.linearGradient(
    colors = listOf(Violet600, Purple700)
)
