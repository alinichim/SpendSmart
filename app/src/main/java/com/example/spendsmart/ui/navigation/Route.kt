package com.example.spendsmart.ui.navigation

sealed class Route(val baseRoute: String) {
    open fun build(): String = baseRoute

    data object Dashboard : Route("dashboard")

    data class AddExpense(val editId: String? = null) : Route("addExpense") {
        override fun build(): String = if (editId == null) baseRoute else "$baseRoute?editId=$editId"
        companion object {
            const val ARG_EDIT_ID = "editId"
            const val ROUTE_PATTERN = "addExpense?editId={editId}"
        }
    }

    data class ExpenseDetails(val id: String) : Route("expenseDetails") {
        override fun build(): String = "$baseRoute/$id"
        companion object {
            const val ARG_ID = "id"
            const val ROUTE_PATTERN = "expenseDetails/{id}"
        }
    }

    data object Settings : Route("settings")
}
