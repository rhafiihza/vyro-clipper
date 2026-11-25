package com.vyro.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vyro.app.ui.theme.*

enum class VyroTab(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Default.Home),
    Library("library", "Library", Icons.Default.PlayArrow),
    Settings("settings", "Settings", Icons.Default.Settings),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VyroScaffold(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    currentTab: VyroTab? = null,
    onTabSelect: (VyroTab) -> Unit = {},
    actions: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = VyroBackground,
        topBar = {
            TopAppBar(
                title = { Text(title, color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    if (showBack) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = VyroPrimary)
                        }
                    } else {
                        Text("VYRO", color = VyroPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp,
                            modifier = Modifier.padding(start = 16.dp))
                    }
                },
                actions = { actions() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VyroPhoneFrame,
                    titleContentColor = VyroTextPrimary,
                    navigationIconContentColor = VyroPrimary,
                    actionIconContentColor = VyroPrimary,
                ),
            )
        },
        bottomBar = {
            if (currentTab != null) {
                NavigationBar(containerColor = VyroPhoneFrame, contentColor = VyroTextMuted, tonalElevation = 0.dp) {
                    VyroTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = currentTab == tab,
                            onClick = { onTabSelect(tab) },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VyroPrimary,
                                selectedTextColor = VyroPrimary,
                                unselectedIconColor = VyroTextMuted,
                                unselectedTextColor = VyroTextMuted,
                                indicatorColor = VyroSurfaceAlt,
                            ),
                        )
                    }
                }
            }
        },
        content = content,
    )
}