package com.vyro.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vyro.app.data.MockData
import com.vyro.app.data.ScoredClip
import com.vyro.app.data.LiveClip
import com.vyro.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

sealed class VyroScreen {
    object Home : VyroScreen()
    object Analyzing : VyroScreen()
    object Results : VyroScreen()
    data class Editor(val clipIndex: Int) : VyroScreen()
    object Live : VyroScreen()
    object Export : VyroScreen()
    object Settings : VyroScreen()
}

@Composable
fun VyroPrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled, modifier = modifier.height(46.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.buttonColors(containerColor = VyroPrimary, contentColor = VyroPhoneFrame,
            disabledContainerColor = VyroSurfaceAlt, disabledContentColor = VyroTextMuted),
        contentPadding = PaddingValues(horizontal = 16.dp)) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun VyroCard(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    val m = modifier.clip(RoundedCornerShape(17.dp)).background(VyroSurface)
        .border(1.dp, Color.White.copy(alpha = 0.075f), RoundedCornerShape(17.dp))
    Column(modifier = if (onClick != null) m.clickable(onClick = onClick) else m, content = content)
}

@Composable
fun VyroChip(text: String, selected: Boolean = false, onClick: () -> Unit = {}) {
    Box(modifier = Modifier.height(36.dp).clip(RoundedCornerShape(20.dp))
        .background(if (selected) VyroPrimary else VyroSurfaceAlt).clickable(onClick = onClick)
        .padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
        Text(text, color = if (selected) VyroPhoneFrame else VyroTextPrimary,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, fontSize = 12.sp)
    }
}

@Composable
fun VyroScoreRing(score: Int) {
    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(progress = score / 100f, color = if (score >= 80) VyroPrimary else VyroSecondary,
            strokeWidth = 4.dp, modifier = Modifier.fillMaxSize(), trackColor = VyroSurfaceAlt)
        Text("$score", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun VyroLiveBadge() {
    val t = rememberInfiniteTransition(label = "lb")
    val a by t.animateFloat(1f, 0.5f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "p")
    Surface(shape = RoundedCornerShape(50), color = VyroDanger.copy(alpha = a)) {
        Text("LIVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
    }
}

@Composable
fun HomeScreen(nav: (VyroScreen) -> Unit) {
    var url by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(VyroBackground)
        .verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text("VYRO", color = VyroPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("100% on-device", color = VyroTextMuted, fontSize = 12.sp)
        }
        Text("Turn long videos into clips that land.", color = VyroTextPrimary,
            fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp, letterSpacing = (-1).sp)
        Text("Paste a YouTube link or monitor a live stream locally.", color = VyroTextMuted, fontSize = 13.sp)
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp), Alignment.CenterVertically) {
            OutlinedTextField(value = url, onValueChange = { url = it },
                placeholder = { Text("Paste YouTube link", color = VyroTextMuted, fontSize = 14.sp) },
                modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VyroPhoneFrame, unfocusedContainerColor = VyroPhoneFrame,
                    focusedBorderColor = VyroPrimary, unfocusedBorderColor = VyroSurfaceAlt,
                    focusedTextColor = VyroTextPrimary, unfocusedTextColor = VyroTextPrimary),
                singleLine = true)
            VyroPrimaryButton("Sample", onClick = { url = MockData.sampleVideo.url })
        }
        VyroPrimaryButton("Find the best clips", onClick = { nav(VyroScreen.Analyzing) },
            enabled = url.contains("youtu"), modifier = Modifier.fillMaxWidth())
        VyroCard(Modifier.fillMaxWidth(), onClick = { nav(VyroScreen.Live) }) {
            Row(Modifier.fillMaxWidth().padding(13.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("YouTube Live Clipper", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("EWMA chat spike detector + Clip now", color = VyroTextMuted, fontSize = 12.sp)
                }
                VyroLiveBadge()
            }
        }
        VyroCard(Modifier.fillMaxWidth(), onClick = {}) {
            Column(Modifier.padding(13.dp)) {
                Text("Import a local video", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Pick MP4/MKV from your phone", color = VyroTextMuted, fontSize = 12.sp)
            }
        }
        Text("Recent", color = VyroTextMuted, fontSize = 12.sp)
        VyroCard(Modifier.fillMaxWidth(), onClick = { nav(VyroScreen.Results) }) {
            Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(Modifier.size(58.dp, 42.dp).clip(RoundedCornerShape(10.dp))
                    .background(Brush.linearGradient(listOf(Color(0xFF573322), Color(0xFF24282E)))))
                Column(Modifier.weight(1f)) {
                    Text(MockData.sampleVideo.title, color = VyroTextPrimary, fontSize = 13.sp,
                        fontWeight = FontWeight.Bold, maxLines = 1)
                    Text("3 clips - Preview & Edit", color = VyroTextMuted, fontSize = 12.sp)
                }
            }
        }
        TextButton(onClick = { nav(VyroScreen.Settings) }) { Text("Settings", color = VyroTextMuted, fontSize = 13.sp) }
    }
}

@Composable
fun AnalyzingScreen(onComplete: () -> Unit, onCancel: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val steps = MockData.analyzeSteps
    LaunchedEffect(Unit) { while (step < steps.size) { delay(700); step++ }; delay(300); onComplete() }
    Column(Modifier.fillMaxSize().background(VyroBackground).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Analyzing on-device", color = VyroTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(MockData.sampleVideo.title, color = VyroTextMuted, fontSize = 12.sp)
        steps.forEachIndexed { i, label ->
            val status = when { i < step -> "Done"; i == step -> "Running"; else -> "Waiting" }
            val c = when (status) { "Done" -> VyroSuccess; "Running" -> VyroPrimary; else -> VyroTextMuted }
            VyroCard(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(13.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text(label, color = VyroTextPrimary, fontSize = 14.sp)
                    Text(status, color = c, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
        LinearProgressIndicator(progress = step.toFloat() / steps.size, color = VyroPrimary,
            trackColor = VyroSurfaceAlt, modifier = Modifier.fillMaxWidth().height(6.dp))
        VyroPrimaryButton("Cancel", onClick = onCancel)
    }
}

@Composable
fun ResultsScreen(onBack: () -> Unit, onEdit: (Int) -> Unit) {
    var sortBy by remember { mutableStateOf("top") }
    val clips = if (sortBy == "top") MockData.scoredClips.sortedByDescending { it.score }
    else MockData.scoredClips.sortedBy { it.durationSec }
    Column(Modifier.fillMaxSize().background(VyroBackground).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            VyroChip("Back", onClick = onBack)
            Text("Podcast - 2 speakers", color = VyroTextMuted, fontSize = 12.sp)
        }
        Text(MockData.sampleVideo.title, color = VyroTextPrimary, fontSize = 20.sp,
            fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
        Text("1:12:40 - Tap any clip to edit", color = VyroTextMuted, fontSize = 12.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            VyroChip("Top scored", sortBy == "top") { sortBy = "top" }
            VyroChip("Shortest", sortBy == "short") { sortBy = "short" }
        }
        clips.forEach { ClipCard(it) { onEdit(it.index) } }
    }
}

@Composable
private fun ClipCard(clip: ScoredClip, onEdit: () -> Unit) {
    VyroCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(13.dp)) {
            Box(Modifier.fillMaxWidth().height(118.dp).clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF29231F), Color(0xFF15191F), Color(0xFF211A17))))
                .clickable(onClick = onEdit), contentAlignment = Alignment.Center) {
                Text("\"${clip.captions.firstOrNull() ?: ""}\"", color = VyroTextPrimary,
                    fontSize = 11.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(horizontal = 16.dp))
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp), Alignment.CenterVertically) {
                VyroScoreRing(clip.score)
                Column(Modifier.weight(1f)) {
                    Text(clip.title, color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${clip.durationSec / 60}:${String.format("%02d", clip.durationSec % 60)} - Audio ${clip.audioScore} - Scene ${clip.sceneScore}",
                        color = VyroTextMuted, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(20.dp), color = VyroSurfaceAlt) {
                    Text(clip.reason, color = VyroTextMuted, fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
                }
                VyroPrimaryButton("Edit", onClick = onEdit)
            }
        }
    }
}

@Composable
fun EditorScreen(clipIndex: Int, onBack: () -> Unit, onExport: () -> Unit) {
    val clip = MockData.scoredClips.getOrElse(clipIndex) { MockData.scoredClips[0] }
    var ratioIdx by remember { mutableIntStateOf(0) }
    var capOn by remember { mutableStateOf(true) }
    var musicOn by remember { mutableStateOf(false) }
    var trimEnd by remember { mutableFloatStateOf(clip.durationSec.toFloat()) }
    Column(Modifier.fillMaxSize().background(VyroBackground).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            VyroChip("Back", onClick = onBack)
            VyroPrimaryButton("Export", onClick = onExport)
        }
        Box(Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(14.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF2A211B), VyroSurface))), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(50.dp).clip(CircleShape).background(VyroPrimary), contentAlignment = Alignment.Center) {
                    Text("S1", color = VyroPhoneFrame, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Text("Speaker 1", color = VyroTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("9:16", "1:1", "16:9", "4:5").forEachIndexed { i, r -> VyroChip(r, ratioIdx == i) { ratioIdx = i } }
        }
        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color(0xFF141210)).padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("Timeline", color = VyroTextMuted, fontSize = 11.sp)
            Box(Modifier.fillMaxWidth().height(38.dp).clip(RoundedCornerShape(8.dp))
                .background(Brush.horizontalGradient(listOf(Color(0xFF3A261B), Color(0xFF231D18))))
                .border(2.dp, VyroSecondary, RoundedCornerShape(8.dp)))
            Box(Modifier.fillMaxWidth().height(24.dp).clip(RoundedCornerShape(6.dp))
                .background(if (musicOn) VyroPrimary.copy(alpha = 0.14f) else VyroSurfaceAlt)
                .clickable { musicOn = !musicOn }.padding(horizontal = 8.dp), contentAlignment = Alignment.CenterStart) {
                Text(if (musicOn) "Lo-Fi Bed (-14 dB)" else "Tap to add music",
                    color = if (musicOn) VyroTextPrimary else VyroTextMuted, fontSize = 11.sp)
            }
            Box(Modifier.fillMaxWidth().height(24.dp).clip(RoundedCornerShape(6.dp))
                .background(if (capOn) VyroPrimary.copy(alpha = 0.14f) else VyroSurfaceAlt)
                .clickable { capOn = !capOn }.padding(horizontal = 8.dp), contentAlignment = Alignment.CenterStart) {
                Text(if (capOn) "Hook: ${clip.title.take(20)}..." else "Tap to add text",
                    color = if (capOn) VyroTextPrimary else VyroTextMuted, fontSize = 11.sp)
            }
        }
        VyroCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Trim Window", color = VyroTextMuted, fontSize = 12.sp)
                Text("0:00 - ${trimEnd.toInt() / 60}:${String.format("%02d", trimEnd.toInt() % 60)}",
                    color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                Slider(value = trimEnd, onValueChange = { trimEnd = it },
                    valueRange = 0f..clip.durationSec.toFloat(),
                    colors = SliderDefaults.colors(thumbColor = VyroPrimary, activeTrackColor = VyroPrimary, inactiveTrackColor = VyroSurfaceAlt))
            }
        }
        Text("Tools", color = VyroTextMuted, fontSize = 11.sp)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Music", "Frame", "Memes", "Zoom", "Text", "Trim").forEach { tool ->
                VyroCard(Modifier.weight(1f)) { Text(tool, color = VyroTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(8.dp)) }
            }
        }
    }
}

@Composable
fun LiveClipperScreen(onBack: () -> Unit, onEdit: () -> Unit) {
    var watchSec by remember { mutableIntStateOf(13) }
    var chatRate by remember { mutableIntStateOf(24) }
    var clipsSaved by remember { mutableIntStateOf(26) }
    var isRunning by remember { mutableStateOf(true) }
    var spikeGraph by remember { mutableStateOf(MockData.initialSpikeGraph) }
    val liveClips = remember { mutableStateListOf<LiveClip>().apply { addAll(MockData.initialLiveClips) } }
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000); watchSec++
            chatRate = MockData.nextSpikeValue(chatRate)
            spikeGraph = (spikeGraph + chatRate).takeLast(16)
        }
    }
    Column(Modifier.fillMaxSize().background(VyroBackground).verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VyroChip("<", onClick = onBack)
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.size(9.dp).clip(CircleShape).background(VyroDanger))
                        Text("LIVE! - PIRATE TOKEN...", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                    }
                    Text("YouTube Live - EWMA Spike", color = VyroTextMuted, fontSize = 11.sp)
                }
            }
            Surface(shape = RoundedCornerShape(50), color = VyroSurfaceAlt) {
                Text("${liveClips.size}/3", color = VyroTextPrimary, fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp))
            }
        }
        Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).clip(RoundedCornerShape(14.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF1E1B4B))))) {
            VyroLiveBadge()
            Box(Modifier.padding(10.dp).align(Alignment.TopEnd).size(84.dp, 92.dp)
                .clip(RoundedCornerShape(10.dp)).background(Brush.linearGradient(listOf(Color(0xFF3F2E26), Color(0xFF1F1714))))
                .border(1.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(30.dp).clip(CircleShape).background(Color(0xFFEF4444)), contentAlignment = Alignment.Center) {
                        Text("TG", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                    Text("Facecam", color = VyroTextPrimary, fontSize = 10.sp)
                    Text("ML Kit", color = VyroSuccess, fontSize = 9.sp)
                }
            }
            Surface(Modifier.padding(10.dp).align(Alignment.BottomStart).fillMaxWidth(0.65f),
                shape = RoundedCornerShape(8.dp), color = VyroPhoneFrame.copy(alpha = 0.8f)) {
                Column(Modifier.padding(6.dp)) {
                    Text("LIVE! - NEW UPDATE and PIRATE TOKEN", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("Typical Gamer - Buffer 60s", color = VyroTextMuted, fontSize = 10.sp)
                }
            }
        }
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
            Button(onClick = {
                clipsSaved++; liveClips.add(0, LiveClip(clipsSaved, 65, 50, "now",
                    Random.nextInt(3, 7), Random.nextInt(1, 5), Random.nextInt(12, 30), "Manual Clip now", "Live"))
            }, modifier = Modifier.weight(1f).height(46.dp), shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VyroLivePink)) {
                Text("Clip now", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            VyroPrimaryButton(if (isRunning) "Stop" else "Resume", onClick = { isRunning = !isRunning })
        }
        VyroCard(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth().padding(12.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("Baseline locked - 2.5x EWMA", color = VyroTextMuted, fontSize = 12.sp)
                VyroChip("Simulate") {
                    chatRate = 68; spikeGraph = (spikeGraph + 68).takeLast(16); clipsSaved++
                    liveClips.add(0, LiveClip(clipsSaved, Random.nextInt(68, 82), 50, "now",
                        Random.nextInt(8, 14), Random.nextInt(1, 5), Random.nextInt(12, 30), "Chat explodes", "Live"))
                }
            }
        }
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp)) {
            StatBox("Watching", "${watchSec}s", modifier = Modifier.weight(1f))
            StatBox("Chat/min", "$chatRate", modifier = Modifier.weight(1f))
        }
        Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp)) {
            StatBox("Clips saved", "$clipsSaved", true, Modifier.weight(1f))
        }
        VyroCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp)) {
                Text("Chat activity", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth().height(60.dp), Arrangement.spacedBy(2.dp), Alignment.Bottom) {
                    spikeGraph.forEach { v ->
                        Box(Modifier.weight(1f).height((v * 60f / 50f).dp).clip(RoundedCornerShape(2.dp))
                            .background(if (v > 50) VyroLivePink else VyroAccentPurple.copy(alpha = 0.6f)))
                    }
                }
            }
        }
        Button(onClick = onEdit, modifier = Modifier.fillMaxWidth().height(46.dp), shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VyroAccentPurpleDeep)) {
            Text("Edit all $clipsSaved clips", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
        liveClips.forEach { lc -> LiveClipCard(lc) }
    }
}

@Composable
private fun StatBox(label: String, value: String, highlight: Boolean = false, modifier: Modifier = Modifier) {
    Box(modifier.clip(RoundedCornerShape(14.dp))
        .background(if (highlight) Brush.linearGradient(listOf(VyroAccentPurpleDeep.copy(alpha = 0.3f), VyroSurface)) else Brush.linearGradient(listOf(VyroSurface, VyroSurface)))
        .border(1.dp, if (highlight) VyroAccentPurple.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
        .padding(horizontal = 13.dp, vertical = 11.dp)) {
        Column { Text(label, color = VyroTextMuted, fontSize = 11.sp); Text(value, color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 22.sp) }
    }
}

@Composable
private fun LiveClipCard(clip: LiveClip) {
    VyroCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Box(Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF27221D), Color(0xFF171A21), Color(0xFF1E293B))))) {
                Surface(Modifier.padding(8.dp).align(Alignment.TopStart), shape = RoundedCornerShape(6.dp),
                    color = VyroPhoneFrame.copy(alpha = 0.8f)) {
                    Text("Clip ${clip.id}", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp))
                }
                Surface(Modifier.padding(8.dp).align(Alignment.BottomStart), shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF78350F).copy(alpha = 0.9f)) {
                    Text("Score ${clip.score}", color = Color(0xFFFDE68A), fontWeight = FontWeight.Bold, fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp))
                }
                Surface(Modifier.padding(8.dp).align(Alignment.BottomEnd), shape = RoundedCornerShape(6.dp),
                    color = VyroPhoneFrame.copy(alpha = 0.8f)) {
                    Text("${clip.durationSec}s", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Chat exploded - ${clip.timeLabel}", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("${clip.messageCount} msgs in 5s - moment at ${clip.peakSec}s", color = VyroTextMuted, fontSize = 12.sp)
            Text(clip.reason, color = VyroSecondary, fontSize = 12.sp, modifier = Modifier.padding(top = 3.dp))
        }
    }
}

@Composable
fun ExportScreen(onDone: () -> Unit) {
    var progress by remember { mutableIntStateOf(0) }
    var done by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { while (progress < 100) { delay(130); progress = (progress + 5).coerceAtMost(100) }; done = true }
    Column(Modifier.fillMaxSize().background(VyroBackground).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (!done) {
            Spacer(Modifier.height(40.dp))
            Text("$progress%", color = VyroPrimary, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Text(if (progress < 35) "Trimming" else if (progress < 70) "Burning captions" else "Encoding 9:16",
                color = VyroTextMuted, fontSize = 13.sp)
            LinearProgressIndicator(progress = progress / 100f, color = VyroPrimary, trackColor = VyroSurfaceAlt,
                modifier = Modifier.fillMaxWidth().height(6.dp))
            VyroPrimaryButton("Cancel", onClick = onDone)
        } else {
            Spacer(Modifier.height(20.dp))
            Text("Clip ready", color = VyroTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("vyro_1_9x16.mp4", color = VyroTextMuted, fontSize = 12.sp)
            VyroCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(13.dp)) {
                    Text("Auto Hook (YAKE)", color = VyroTextMuted, fontSize = 12.sp)
                    Text(MockData.scoredClips[0].title, color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Description", color = VyroTextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    Text("${MockData.scoredClips[0].captions[0]} #Shorts", color = VyroTextPrimary, fontSize = 13.sp)
                }
            }
            VyroPrimaryButton("Save to gallery", onClick = onDone, modifier = Modifier.fillMaxWidth())
            VyroPrimaryButton("Back to clips", onClick = onDone, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var lang by remember { mutableStateOf("EN") }
    var ratio by remember { mutableStateOf("9:16") }
    Column(Modifier.fillMaxSize().background(VyroBackground).verticalScroll(rememberScrollState()).padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            VyroChip("Back", onClick = onBack)
            Text("Settings", color = VyroTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        Text("General", color = VyroTextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        VyroCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Language", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VyroChip("English", lang == "EN") { lang = "EN" }
                    VyroChip("Bahasa ID", lang == "ID") { lang = "ID" }
                }
            }
        }
        VyroCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Default ratio", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("9:16", "1:1", "16:9", "4:5").forEach { r -> VyroChip(r, ratio == r) { ratio = r } }
                }
            }
        }
        Text("About", color = VyroTextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        VyroCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(13.dp)) {
                Text("VYRO v2.0", color = VyroTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Every Moment, Made Powerful.", color = VyroTextMuted, fontSize = 12.sp)
            }
        }
        VyroCard(Modifier.fillMaxWidth(), onClick = {}) { Text("Open-Source Licenses", color = VyroTextPrimary, fontSize = 14.sp, modifier = Modifier.padding(13.dp)) }
        VyroCard(Modifier.fillMaxWidth(), onClick = {}) { Text("Privacy Policy", color = VyroTextPrimary, fontSize = 14.sp, modifier = Modifier.padding(13.dp)) }
    }
}