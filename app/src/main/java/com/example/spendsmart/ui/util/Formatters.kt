package com.example.spendsmart.ui.util

import com.example.spendsmart.domain.model.Currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val moneyFormat = DecimalFormat(
    "#,##0.00",
    DecimalFormatSymbols(Locale.US)
)

fun formatMoney(amount: Double, currency: Currency): String =
    "${currency.symbol}${moneyFormat.format(amount)}"

fun formatMoneyPlain(amount: Double): String = moneyFormat.format(amount)

private val dateFormat = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US)
private val monthFormat = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)

fun formatDate(date: LocalDate): String = date.format(dateFormat)
fun formatMonth(date: LocalDate): String = date.format(monthFormat)
