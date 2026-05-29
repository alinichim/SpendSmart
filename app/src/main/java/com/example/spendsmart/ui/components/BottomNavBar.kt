package com.example.spendsmart.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spendsmart.R
import com.example.spendsmart.ui.navigation.Route

private data class BottomItem(
    val route: Route,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector
)

private val items = listOf(
    BottomItem(Route.Dashboard, R.string.nav_home, Icons.Filled.Dashboard),
    BottomItem(Route.AddExpense(), R.string.nav_add, Icons.Filled.AddCircle),
    BottomItem(Route.Settings, R.string.nav_settings, Icons.Filled.Settings)
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onSelect: (Route) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute?.startsWith(item.route.baseRoute) == true
            val label = stringResource(item.labelRes)
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(item.route) },
                icon = { Icon(item.icon, contentDescription = label) },
                label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
