package com.vyro.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vyro.app.ui.components.VyroTab
import com.vyro.app.ui.screens.*
import com.vyro.app.ui.theme.VyroBackground
import com.vyro.app.ui.theme.VyroPrimary
import com.vyro.app.ui.theme.VyroTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        setContent { VyroTheme { VyroApp() } }
    }
}

@Composable
fun VyroApp() {
    var showSplash by remember { mutableStateOf(true) }
    var backStack by remember { mutableStateOf<List<VyroRoute>>(listOf(VyroRoute.Home)) }

    if (showSplash) {
        VyroSplash(onTimeout = { showSplash = false })
        return
    }

    val currentRoute = backStack.last()
    val currentTab = when (currentRoute) {
        is VyroRoute.Home -> VyroTab.Home
        is VyroRoute.Library -> VyroTab.Library
        is VyroRoute.Settings -> VyroTab.Settings
        else -> null
    }

    fun navigate(route: VyroRoute) { backStack = backStack + route }
    fun goBack() { if (backStack.size > 1) backStack = backStack.dropLast(1) }
    fun selectTab(tab: VyroTab) {
        val route = when (tab) {
            VyroTab.Home -> VyroRoute.Home
            VyroTab.Library -> VyroRoute.Library
            VyroTab.Settings -> VyroRoute.Settings
        }
        backStack = listOf(route)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = VyroBackground) {
        when (val route = currentRoute) {
            is VyroRoute.Home -> HomeScreen(onNavigate = ::navigate, onTabSelect = ::selectTab, currentTab = VyroTab.Home)
            is VyroRoute.Library -> LibraryScreen(onNavigate = ::navigate, onTabSelect = ::selectTab, currentTab = VyroTab.Library)
            is VyroRoute.Settings -> SettingsScreen(onBack = ::goBack, onTabSelect = ::selectTab, currentTab = VyroTab.Settings)
            is VyroRoute.Analyzing -> AnalyzingScreen(onComplete = { navigate(VyroRoute.Results) }, onBack = ::goBack)
            is VyroRoute.Results -> ResultsScreen(onBack = ::goBack, onEdit = { navigate(VyroRoute.Editor) })
            is VyroRoute.Editor -> EditorScreen(clipIndex = 0, onBack = ::goBack, onExport = { navigate(VyroRoute.Export) })
            is VyroRoute.Live -> LiveClipperScreen(onBack = ::goBack, onEdit = { navigate(VyroRoute.Editor) })
            is VyroRoute.Export -> ExportScreen(onBack = ::goBack, onPublish = { navigate(VyroRoute.Publish) })
            is VyroRoute.Publish -> PublishScreen(onBack = ::goBack)
        }
    }
}

@Composable
fun VyroSplash(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) { delay(1500); onTimeout() }
    Box(modifier = Modifier.fillMaxSize().background(VyroBackground), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("VYRO", color = VyroPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp)
            Text("Every Moment, Made Powerful.", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
        }
    }
}