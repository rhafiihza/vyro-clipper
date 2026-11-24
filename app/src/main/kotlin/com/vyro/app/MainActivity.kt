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
        setContent {
            VyroTheme {
                VyroApp()
            }
        }
    }
}

@Composable
fun VyroApp() {
    var showSplash by remember { mutableStateOf(true) }
    var screen by remember { mutableStateOf<VyroScreen>(VyroScreen.Home) }

    if (showSplash) {
        VyroSplash(onTimeout = { showSplash = false })
    } else {
        Surface(modifier = Modifier.fillMaxSize(), color = VyroBackground) {
            when (val s = screen) {
                is VyroScreen.Home -> HomeScreen(nav = { screen = it })
                is VyroScreen.Analyzing -> AnalyzingScreen(
                    onComplete = { screen = VyroScreen.Results },
                    onCancel = { screen = VyroScreen.Home },
                )
                is VyroScreen.Results -> ResultsScreen(
                    onBack = { screen = VyroScreen.Home },
                    onEdit = { idx -> screen = VyroScreen.Editor(idx) },
                )
                is VyroScreen.Editor -> EditorScreen(
                    clipIndex = s.clipIndex,
                    onBack = { screen = VyroScreen.Results },
                    onExport = { screen = VyroScreen.Export },
                )
                is VyroScreen.Live -> LiveClipperScreen(
                    onBack = { screen = VyroScreen.Home },
                    onEdit = { screen = VyroScreen.Editor(0) },
                )
                is VyroScreen.Export -> ExportScreen(onDone = { screen = VyroScreen.Results })
                is VyroScreen.Settings -> SettingsScreen(onBack = { screen = VyroScreen.Home })
            }
        }
    }
}

@Composable
fun VyroSplash(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onTimeout()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(VyroBackground),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("VYRO", color = VyroPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp)
            Text("Every Moment, Made Powerful.", color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
        }
    }
}