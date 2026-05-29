package com.example.spendsmart.ui.screens.addexpense

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.spendsmart.R
import com.example.spendsmart.domain.model.Currency
import com.example.spendsmart.ui.components.CategoryGrid
import com.example.spendsmart.ui.components.GradientButton
import com.example.spendsmart.ui.components.GradientHeader
import com.example.spendsmart.ui.util.formatDate
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    var showCurrencyMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        GradientHeader(
            title = stringResource(if (state.isEdit) R.string.edit_expense_title else R.string.add_expense_title),
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.amount,
                onValueChange = { input ->
                    val filtered = input.filter { ch -> ch.isDigit() || ch == '.' }
                    viewModel.onAmountChange(filtered)
                },
                label = { Text(stringResource(R.string.field_amount)) },
                prefix = {
                    Box {
                        Row(
                            modifier = Modifier
                                .clickable { showCurrencyMenu = true }
                                .padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.currency.flag,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = " ${state.currency.code}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = stringResource(R.string.cd_pick_currency),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        DropdownMenu(
                            expanded = showCurrencyMenu,
                            onDismissRequest = { showCurrencyMenu = false }
                        ) {
                            Currency.entries.forEach { cur ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.currency_label, cur.flag, cur.code, cur.displayName)) },
                                    onClick = {
                                        showCurrencyMenu = false
                                        viewModel.onCurrencyChange(cur)
                                    }
                                )
                            }
                        }
                    }
                },
                singleLine = true,
                isError = state.amountError != null,
                supportingText = state.amountError?.let { resId ->
                    { Text(stringResource(resId)) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            )

            Column {
                Text(
                    text = stringResource(R.string.field_category),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                CategoryGrid(
                    selected = state.category,
                    onSelect = viewModel::onCategoryChange
                )
                state.categoryError?.let { resId ->
                    Text(
                        text = stringResource(resId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            OutlinedTextField(
                value = formatDate(state.date),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.field_date)) },
                trailingIcon = {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = stringResource(R.string.cd_pick_date))
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            TextButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.field_change_date)) }

            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text(stringResource(R.string.field_notes)) },
                supportingText = { Text(stringResource(R.string.notes_char_counter, state.notesCharCount, 200)) },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(8.dp))

            GradientButton(
                text = stringResource(
                    when {
                        state.justSaved -> R.string.saved
                        state.isEdit -> R.string.save_changes
                        else -> R.string.save_expense
                    }
                ),
                enabled = !state.isSaving,
                onClick = { viewModel.onSubmit(onSaved) },
                leadingContent = if (state.justSaved) {
                    @Composable {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                } else null
            )
        }
    }

    if (showDatePicker) {
        val initialMillis = state.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        val picked = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.onDateChange(picked)
                    }
                    showDatePicker = false
                }) { Text(stringResource(R.string.action_ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.action_cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
