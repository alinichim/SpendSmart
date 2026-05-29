package com.example.spendsmart.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.spendsmart.R
import com.example.spendsmart.domain.model.Expense
import com.example.spendsmart.ui.components.DonutChart
import com.example.spendsmart.ui.components.DonutSlice
import com.example.spendsmart.ui.components.ExpenseListItem
import com.example.spendsmart.ui.components.GradientHeader
import com.example.spendsmart.ui.components.SectionCard
import com.example.spendsmart.ui.util.formatMoney

@Composable
fun DashboardScreen(
    onExpenseClick: (Expense) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                GradientHeader(
                    title = stringResource(R.string.dashboard_greeting),
                    subtitle = state.monthLabel,
                    extraBottomPadding = true
                )
            }
            item {
                SectionCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-48).dp)
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_this_month),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    val total = state.monthTotalConverted
                    if (total != null) {
                        Text(
                            text = formatMoney(total, state.displayCurrency),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        val top = state.topCategory
                        val topLabel = if (top != null) {
                            stringResource(R.string.dashboard_top_category, top.emoji, top.displayName)
                        } else {
                            stringResource(R.string.dashboard_no_expenses_yet)
                        }
                        Text(
                            text = stringResource(
                                R.string.dashboard_transactions_summary,
                                state.monthTransactionCount,
                                topLabel
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "—",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.dashboard_no_rates_hint),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            if (state.ratesAvailable && state.categoryBreakdown.isNotEmpty()) {
                item {
                    SectionCard(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .offset(y = (-32).dp)
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_spending_breakdown),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        DonutChart(
                            slices = state.categoryBreakdown.map { DonutSlice(it.category, it.amount) },
                            totalLabel = formatMoney(state.monthTotalConverted ?: 0.0, state.displayCurrency)
                        )
                    }
                }
            }
            item {
                Text(
                    text = stringResource(R.string.dashboard_recent_transactions),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = (-16).dp)
                )
            }
            items(state.expenses.take(20), key = { it.id }) { expense ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .offset(y = (-12).dp)
                ) {
                    ExpenseListItem(
                        expense = expense,
                        onClick = { onExpenseClick(expense) },
                        onDelete = { pendingDeleteId = expense.id }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                }
            }
            if (state.expenses.isEmpty() && !state.isLoading) {
                item {
                    Text(
                        text = stringResource(R.string.dashboard_no_expenses),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }
        }
    }

    val deleteId = pendingDeleteId
    if (deleteId != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteId = null },
            title = { Text(stringResource(R.string.delete_expense_title)) },
            text = { Text(stringResource(R.string.delete_expense_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onDelete(deleteId)
                    pendingDeleteId = null
                }) { Text(stringResource(R.string.action_delete)) }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteId = null }) { Text(stringResource(R.string.action_cancel)) }
            }
        )
    }
}
