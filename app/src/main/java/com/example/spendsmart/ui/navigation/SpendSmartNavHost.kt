package com.example.spendsmart.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spendsmart.ui.components.BottomNavBar
import com.example.spendsmart.ui.screens.addexpense.AddExpenseScreen
import com.example.spendsmart.ui.screens.dashboard.DashboardScreen
import com.example.spendsmart.ui.screens.details.ExpenseDetailsScreen
import com.example.spendsmart.ui.screens.settings.SettingsScreen

@Composable
fun SpendSmartNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute?.let { route ->
        route == Route.Dashboard.baseRoute || route == Route.Settings.baseRoute
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onSelect = { selected ->
                        val target = selected.build()
                        navController.navigate(target) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Route.Dashboard.baseRoute) {
                                saveState = true
                                inclusive = false
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Route.Dashboard.baseRoute,
            modifier = Modifier.padding(PaddingValues(bottom = padding.calculateBottomPadding()))
        ) {
            spendSmartGraph(
                onExpenseClick = { expense ->
                    navController.navigate(Route.ExpenseDetails(expense.id).build())
                },
                onOpenAdd = {
                    navController.navigate(Route.AddExpense().build())
                },
                onBack = { navController.popBackStack() },
                onEditExpense = { id ->
                    navController.navigate(Route.AddExpense(editId = id).build()) {
                        popUpTo(Route.Dashboard.baseRoute)
                    }
                },
                onAfterSave = { navController.popBackStack() },
                onAfterDelete = {
                    navController.popBackStack(Route.Dashboard.baseRoute, inclusive = false)
                }
            )
        }
    }
}

private fun NavGraphBuilder.spendSmartGraph(
    onExpenseClick: (com.example.spendsmart.domain.model.Expense) -> Unit,
    onOpenAdd: () -> Unit,
    onBack: () -> Unit,
    onEditExpense: (String) -> Unit,
    onAfterSave: () -> Unit,
    onAfterDelete: () -> Unit
) {
    composable(Route.Dashboard.baseRoute) {
        DashboardScreen(onExpenseClick = onExpenseClick)
    }

    composable(
        route = Route.AddExpense.ROUTE_PATTERN,
        arguments = listOf(
            navArgument(Route.AddExpense.ARG_EDIT_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        AddExpenseScreen(onBack = onBack, onSaved = onAfterSave)
    }

    composable(
        route = Route.ExpenseDetails.ROUTE_PATTERN,
        arguments = listOf(
            navArgument(Route.ExpenseDetails.ARG_ID) { type = NavType.StringType }
        )
    ) {
        ExpenseDetailsScreen(
            onBack = onBack,
            onEdit = onEditExpense,
            onDeleted = onAfterDelete
        )
    }

    composable(Route.Settings.baseRoute) {
        SettingsScreen()
    }
}
