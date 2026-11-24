package com.vyro.app.data

import kotlin.random.Random

data class ScoredClip(
    val index: Int,
    val score: Int,
    val title: String,
    val durationSec: Int,
    val audioScore: Int,
    val sceneScore: Int,
    val reason: String,
    val captions: List<String>,
)

data class LiveClip(
    val id: Int,
    val score: Int,
    val durationSec: Int,
    val timeLabel: String,
    val messageCount: Int,
    val nearbyCount: Int,
    val peakSec: Int,
    val reason: String,
    val scene: String,
)

data class VideoMeta(
    val title: String,
    val url: String,
    val durationLabel: String,
    val speakerCount: Int,
)

object MockData {
    val sampleVideo = VideoMeta(
        title = "The Build Log Podcast #42: Building a Cabin in 90 Days",
        url = "https://youtube.com/watch?v=SAMPLE42",
        durationLabel = "1:12:40",
        speakerCount = 2,
    )

    val scoredClips = listOf(
        ScoredClip(0, 92, "We poured the foundation in the rain", 42, 90, 85,
            "Laughter and audio peak",
            listOf("The foundation was the scariest week.", "We poured concrete in a downpour.",
                "Everyone said it would crack.", "It held. Not one crack.",
                "Here is what we would do differently.", "Check the forecast. Twice.")),
        ScoredClip(1, 81, "The budget mistake nobody warns you about", 35, 80, 60,
            "Fast speaker exchange",
            listOf("We were 30% over by week three.", "Nobody warns you about permits.",
                "Keep a buffer. Always.", "It saved the whole build.")),
        ScoredClip(2, 67, "Why we skipped the contractor", 58, 55, 50,
            "Steady, clear speech",
            listOf("We almost hired a contractor.", "Then we watched a few builds.",
                "Ninety days later, we are done.")),
    )

    val analyzeSteps = listOf(
        "Downloading video", "Reading audio energy", "Finding silent gaps",
        "Detecting scene changes", "Transcribing speech", "Tracking speakers", "Scoring clips",
    )

    val initialLiveClips = listOf(
        LiveClip(26, 68, 50, "03:38 PM", 9, 1, 21, "Hype words + Chat explodes", "Night street, Neon car"),
        LiveClip(25, 70, 60, "03:34 PM", 8, 4, 20, "Hype words + Chat explodes", "Tactical map overview"),
    )

    val initialSpikeGraph = listOf(14, 18, 15, 22, 19, 28, 24, 21, 30, 26, 22, 35, 29, 24, 27, 31)

    fun nextSpikeValue(current: Int): Int {
        val delta = Random.nextInt(-5, 6)
        return maxOf(8, minOf(44, current + delta))
    }
}