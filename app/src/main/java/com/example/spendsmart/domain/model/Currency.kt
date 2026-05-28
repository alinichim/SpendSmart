package com.example.spendsmart.domain.model

enum class Currency(
    val code: String,
    val displayName: String,
    val flag: String,
    val symbol: String
) {
    USD("USD", "US Dollar", "🇺🇸", "$"),
    EUR("EUR", "Euro", "🇪🇺", "€"),
    GBP("GBP", "British Pound", "🇬🇧", "£"),
    JPY("JPY", "Japanese Yen", "🇯🇵", "¥"),
    CAD("CAD", "Canadian Dollar", "🇨🇦", "C$"),
    AUD("AUD", "Australian Dollar", "🇦🇺", "A$"),
    CHF("CHF", "Swiss Franc", "🇨🇭", "CHF"),
    CNY("CNY", "Chinese Yuan", "🇨🇳", "¥"),
    INR("INR", "Indian Rupee", "🇮🇳", "₹"),
    BRL("BRL", "Brazilian Real", "🇧🇷", "R$");

    companion object {
        fun fromCode(code: String): Currency? =
            entries.firstOrNull { it.code.equals(code, ignoreCase = true) }
    }
}
