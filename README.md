# VYRO

> **Every Moment, Made Powerful.**

On-device video clipping, scoring, transcription, and live-chat-triggered capture for Android.

## What is VYRO?

VYRO turns long-form videos and live streams into ready-to-share short clips, scored and ranked by an on-device pipeline that combines classical signal processing, statistical NLP, and standard computer vision. No cloud inference, no generative AI, no server API.

## Brand

- **Primary:** warm orange `#FF713D`
- **Secondary:** charcoal `#15181D` and deep black `#0D0F12`
- **Accent:** electric purple `#A855F7` (reserved for intelligence features)
- **Typography:** Space Grotesk (bold, geometric, clean sans-serif)
- **Logo:** abstract play symbol combined with a cut/motion element

## Product Suite

| Product | Description | Status |
|---------|-------------|--------|
| VYRO Clips | Smart clip discovery from long-form VOD | MVP (this repo) |
| VYRO Live | Livestream moment capture with chat-spike detection | MVP (Phase 9) |
| VYRO Studio | Full timeline video editor | Future |
| VYRO AI | On-device content assist | Future exploration |

> VYRO, VYRO Clips, VYRO Live, VYRO Studio, and VYRO AI are creative naming proposals, not trademark-cleared names.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Min SDK:** 26 (Android 8.0) · **Target SDK:** 34 (Android 14)
- **Package:** `com.vyro.app`
- **Build:** Gradle 8.7 + AGP 8.5.2 + Kotlin 1.9.24

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK with platform 34 and build-tools 34.0.0

### Build

```bash
# Generate Gradle wrapper (if not present)
gradle wrapper --gradle-version 8.7

# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

Or just open the project in Android Studio and let it sync.

## Project Structure

```
app/src/main/kotlin/com/vyro/app/
├── VyroApplication.kt          # Application class
├── MainActivity.kt             # Entry point + splash + navigation
├── data/
│   └── Models.kt               # ScoredClip, LiveClip, MockData
├── ui/
│   ├── theme/
│   │   ├── Color.kt            # VYRO color tokens
│   │   ├── Type.kt             # Typography
│   │   └── Theme.kt            # Compose theme
│   └── screens/
│       └── Screens.kt          # All screens + shared components
└── (domain/ — to be added per PLAN.md phases)
```

## Screens

| Screen | Phase | Description |
|--------|-------|-------------|
| Splash | 10 | VYRO wordmark + tagline |
| Home | 8 | URL input, Live entry, Recent |
| Analyzing | 8 | 7-step pipeline progress |
| Results | 8 | Scored clip list with score rings |
| Editor | 8 | Timeline, trim, tool dock, ratio |
| Live Clipper | 9 | EWMA spike detector, telemetry, clip cards |
| Export | 7 | Staged progress + auto hook/description |
| Settings | 10 | Language, ratio, about, OSS licenses |

## Development Plan

See [PLAN.md](PLAN.md) for the complete master development plan — 26 sections, 13 phases, task-by-task instructions for the coding agent.

## Coding Standard

Follow the **Ponytail rules** (YAGNI → reuse → stdlib → native → existing dep → one-liner → minimum code). Every task must be under 30 lines of new code. See PLAN.md Section 2.

## Constraint

No generative AI / LLM / cloud ML. Only classical signal processing, statistical NLP, and standard computer vision.

## License

Proprietary. All rights reserved.

---

*VYRO v2.0 — Reconciled with production UI prototypes*