package com.example.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.NeonCyan

@Composable
fun AppNavigationBar(
    currentScreen: String,
    isBengali: Boolean,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Triple("dashboard", if (isBengali) "হোম" else "Home", Icons.Default.Dashboard),
        Triple("new_report", if (isBengali) "নতুন" else "New", Icons.Default.AddCircle),
        Triple("cases", if (isBengali) "কেস" else "Cases", Icons.Default.Folder),
        Triple("evidence_vault", if (isBengali) "ভল্ট" else "Vault", Icons.Default.PhotoLibrary),
        Triple("official_reporting", if (isBengali) "পোর্টাল" else "Portals", Icons.Default.Link)
    )

    NavigationBar(
        containerColor = CyberNavyDark,
        tonalElevation = 6.dp
    ) {
        items.forEach { (route, label, icon) ->
            val isSelected = currentScreen == route || (route == "cases" && (currentScreen == "case_detail" || currentScreen == "report_view"))

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B),
                    indicatorColor = Color(0xFF0F2642)
                ),
                modifier = Modifier.testTag("nav_item_$route")
            )
        }
    }
}
