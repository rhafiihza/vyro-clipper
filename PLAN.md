# VYRO — Master Development Plan

**Project:** VYRO — On-device video clipping, scoring, transcription, and live-chat-triggered capture for Android.
**Coding Standard:** Ponytail ruleset + official Kotlin/Android style guide.
**Current Scope:** Local-only MVP including Live Clipper. Auto-publish, iOS, and cloud backend are documented future enhancements.
**Constraint:** No generative AI / LLM / cloud ML. Only classical signal processing, statistical NLP, and standard computer vision (face detection is permitted).
**Package:** `com.vyro.app` (rebranded from `com.clipforge.app`)
**Version:** 2.0 — Reconciled with production UI prototypes

> Hand this file to your coding agent as PLAN.md and instruct it to complete tasks sequentially. Deferred phases are explicitly marked; do not implement them without approval.

---

## Table of Contents

1. Executive Summary
2. Core Principles & Ponytail Rules
3. Brand & Product Direction
4. Feature Summary
5. Library Stack
6. UI Screen Inventory
7. Design System
8. Phase 0 — Project Skeleton
9. Phase 1 — Authentication & Device Check
10. Phase 2 — YouTube Ingestion (Local)
11. Phase 3 — Non-AI Video Analysis
12. Phase 4 — Transcription & Speaker Tracking
13. Phase 5 — Streamer Cam Detection & Split
14. Phase 6 — Title & Description (Non-AI)
15. Phase 7 — Aspect Ratio & Export
16. Phase 8 — UI & Manual Editor
17. Phase 9 — Live Clipper (MVP)
18. Phase 10 — Onboarding, Settings & Library
19. Phase 11 — Cloudflare Security Layer (Future)
20. Phase 12 — Codebase Security Audit
21. Phase 13 — VYRO Suite Roadmap
22. Legal & Compliance Requirements
23. Future Enhancements
24. File Map
25. AI Agent Prompting Template
26. Reconciliation Note — Plan versus Prototype

---

## 1. Executive Summary

VYRO is an on-device video clipping and live-capture product for Android. It turns long-form videos and live streams into ready-to-share short clips, scored and ranked by an on-device pipeline that combines classical signal processing, statistical natural language processing, and standard computer vision. This document consolidates the original ClipForge specification with the latest production UI prototypes, rebrands the product to VYRO, promotes the Live Clipper to MVP scope, and lays out the full VYRO product suite as a roadmap.

All processing runs on the user's phone. There is no cloud inference, no server API for media processing, and no generative AI. The Live Clipper, which monitors YouTube Live chat for spike events and captures moments from a rolling screen buffer, is now part of the MVP rather than a deferred future note, because it is already fully prototyped in the production UI and represents the primary differentiator against competing clipping tools.

This plan is organised into thirteen phases. Phases 0 through 10 constitute the MVP. Phases 11 and 12 are deferred infrastructure hardening. Phase 13 is the VYRO suite roadmap covering VYRO Live as a standalone module, VYRO Studio as a full timeline editor, and VYRO AI as a future exploration track that would revisit the classical-only principle. Every task is scoped to under thirty lines of new code; anything larger must be split before implementation.

---

## 2. Core Principles & Ponytail Rules

### 2.1 Core Principles

| Principle | Meaning |
|-----------|---------|
| 100% On-Device | All processing runs on the user's phone. No cloud inference, no server API for media. |
| Classical Intelligence Only | Statistical NLP (YAKE), on-device speech recognition (sherpa-onnx), and standard computer vision (ML Kit face detection, JavaCV histogram diff). No LLMs, no Gemini, no Whisper-as-a-service. |
| Local-First MVP | Downloads and exports stay on the device. Auto-publish and cloud sync are future additions. |
| Small, Verifiable Tasks | Every task must be under 30 lines of new code. If larger, split again. Each non-trivial task ships one runnable check. |
| Deletion over Addition | Prefer removing code to adding it. Prefer reusing libraries to writing new code. |

### 2.2 The Ponytail Rules (Mandatory for All Code)

Before writing any code, the agent must walk this seven-rung ladder and stop at the first rung that holds:

1. **YAGNI** — Does this need to exist at all?
2. **Reuse** — Does it already exist in this codebase? Reuse it.
3. **Stdlib** — Does the Kotlin or Java standard library already do this?
4. **Native** — Does an Android platform feature cover it?
5. **Existing dependency** — Does an already-installed library solve it?
6. **One-liner** — Can this be one line?
7. **Minimum code** — Only then, write the smallest working code.

#### Hard Constraints

- No unrequested abstractions.
- No new dependency if avoidable.
- Deletion over addition.
- Shortest working diff wins.
- Mark deliberate simplifications with `ponytail: <ceiling>, <upgrade path>`.
- Never lazy about: input validation, error handling, security, accessibility, real hardware calibration.
- Non-trivial logic needs one runnable check (assert-based demo or one small test file).

#### Kotlin/Android Baseline

- Follow Google's Kotlin style guide (UTF-8, 100-char limit, braces on the same line).
- Follow official Kotlin conventions.
- Enforce via ktlint and detekt.

---

## 3. Brand & Product Direction

### 3.1 Brand Identity

VYRO is the working brand for the product formerly known as ClipForge. The tagline is **Every Moment, Made Powerful.** The positioning is on-device video intelligence for creators who want every moment captured, scored, and shipped without cloud dependence or subscription friction.

Brand positioning avoids the phrase "AI-powered." The product uses classical on-device intelligence (statistical keyword extraction, on-device speech recognition, standard face detection) rather than generative AI. The electric purple accent colour is reserved for features that feel advanced or intelligence-driven (score peaks, spike detection, live telemetry).

### 3.2 Visual Identity

| Token | Value | Usage |
|-------|-------|-------|
| Primary | Warm orange `#FF713D` (gradient `#FF8954` -> `#FF6A2B`) | Primary buttons, active states, brand accents, score rings above 80. |
| Secondary | Charcoal `#15181D` and deep black `#0D0F12` | App background, phone frame, surface cards. |
| Accent (advanced) | Electric purple `#A855F7` / `#8B5CF6` | Score peaks, spike graph, live telemetry highlights. |
| Live trigger | Magenta-pink `#EC4899` to purple `#8B5CF6` gradient | Live mode is visually distinct from VOD mode. |
| Amber | `#FFB84D` | Clip-open state borders, equaliser bars. |
| Success | `#7FD99A` | Done states, gallery-saved confirmation. |
| Danger | `#FF6B6B` | Live badge, delete actions. |

### 3.3 Typography

Space Grotesk is the brand typeface — bold, geometric, clean sans-serif. Weights: 400 body, 500 chips, 700 headings. Bundle as asset font.

### 3.4 Logo Specification

Play triangle whose right edge is replaced by a vertical cut line, suggesting "play + edit." Wordmark VYRO in Space Grotesk Bold 700, all caps. App icon: glyph centred on charcoal `#0D0F12` with orange gradient.

### 3.5 Product Suite Roadmap

| Product | Description | Status |
|---------|-------------|--------|
| VYRO Clips | Smart clip discovery from long-form VOD | MVP (this plan) |
| VYRO Live | Livestream moment capture with chat-spike detection | MVP Phase 9 |
| VYRO Studio | Full timeline video editor | Future module |
| VYRO AI | On-device content assist | Future exploration |

---

## 4. Feature Summary

| # | Feature | Mode |
|---|---------|------|
| 1 | Splash and onboarding | MVP |
| 2 | Google Sign-In (placeholder) | MVP (stub) |
| 3 | Device capability check | MVP |
| 4 | YouTube video download (local) | MVP |
| 5 | Non-AI clip analysis (audio/scene/motion) | MVP |
| 6 | 1-100 clip rating with breakdown | MVP |
| 7 | Local transcription (EN + ID) | MVP |
| 8 | Speaker diarization | MVP |
| 9 | Streamer cam detection + split-screen | MVP |
| 10 | Aspect ratio selector (9:16, 1:1, 16:9, 4:5) | MVP |
| 11 | Title and description (YAKE + templates) | MVP |
| 12 | Manual editor with full tool dock | MVP |
| 13 | Clip Open vs Clip Closed editor modes | MVP |
| 14 | Clip quick preview sheet | MVP |
| 15 | Auto style presets (Clean Minimal, Dynamic) | MVP |
| 16 | Music and meme overlay tracks | MVP |
| 17 | Score peak markers on timeline | MVP |
| 18 | Preview resolution toggle | MVP |
| 19 | Export pipeline with staged progress | MVP |
| 20 | YouTube Live Clipper with EWMA detector | MVP (Phase 9) |
| 21 | Manual Clip now trigger | MVP (Phase 9) |
| 22 | Live telemetry dashboard | MVP (Phase 9) |
| 23 | Spike sensitivity settings | MVP (Phase 9) |
| 24 | Live clip cards | MVP (Phase 9) |
| 25 | Settings screen | MVP |
| 26 | Unified library | MVP |
| 27 | Auto-publish to YouTube | Future |
| 28 | Cloudflare Turnstile gate | Future |
| 29 | iOS support | Future |
| 30 | VYRO Studio full editor | Future |
| 31 | VYRO AI content assist | Future exploration |

---

## 5. Library Stack

| Feature | Library | License |
|---------|---------|---------|
| YouTube download | youtubedl-android | GPL-3.0 |
| Video processing | ffmpeg-kit-full | LGPL/GPL |
| Visual analysis | bytedeco/javacv | Apache-2.0 |
| Audio energy | lincollincol/Amplituda | Apache-2.0 |
| Keyword extraction | xberg-io/xberg (YAKE) | Check repo |
| Transcription + diarization | sherpa-onnx | Apache-2.0 |
| Face detection | com.google.mlkit:face-detection | Proprietary SDK |
| Live chat | kusaanko/YouTubeLiveChat | Check repo |
| Live recording | MediaProjection (Android native) | Platform |

---

## 6. UI Screen Inventory

| ID | Screen | Phase |
|----|--------|-------|
| S1 | Splash | 10 |
| S2 | Onboarding | 10 |
| S3 | Google Sign-In | 1/10 |
| S4 | Device Capability Check | 1/10 |
| S5 | Home | 8 |
| S6 | Analyzing | 8 |
| S7 | Clip Results | 8 |
| S8 | Clip Quick Preview (modal) | 8 |
| S9 | Manual Editor | 8 |
| S10 | Auto Style Sheet (modal) | 8 |
| S11 | Live Clipper | 9 |
| S12 | Live Settings (modal) | 9 |
| S13 | Live Clip Preview (modal) | 9 |
| S14 | Export | 7/8 |
| S15 | Library | 10 |
| S16 | Settings | 10 |

---

## 7. Design System

### 7.1 Color Tokens

| Token | Hex | Compose constant |
|-------|-----|------------------|
| background | `#090A0C` | `VyroColors.background` |
| phoneFrame | `#0D0F12` | `VyroColors.phoneFrame` |
| surface | `#15181D` | `VyroColors.surface` |
| surfaceAlt | `#20242A` | `VyroColors.surfaceAlt` |
| textPrimary | `#F4F5F7` | `VyroColors.textPrimary` |
| textMuted | `#9BA3AE` | `VyroColors.textMuted` |
| primary | `#FF713D` | `VyroColors.primary` |
| secondary | `#FFB84D` | `VyroColors.secondary` |
| accentPurple | `#A855F7` | `VyroColors.accentPurple` |
| livePink | `#EC4899` | `VyroColors.livePink` |
| success | `#7FD99A` | `VyroColors.success` |
| danger | `#FF6B6B` | `VyroColors.danger` |

### 7.2 Component Contract

| Component | Compose equivalent |
|-----------|--------------------|
| Button (primary) | `VyroPrimaryButton` |
| Card | `VyroCard` |
| Chip | `VyroChip` |
| Score ring | `VyroScoreRing` |
| Live badge | `VyroLiveBadge` |
| Spike graph | `VyroSpikeGraph` |

---

## 8. Phase 0 — Project Skeleton

### Task 0.1 — Create Android Project
- Package: `com.vyro.app`, Kotlin, min SDK 26, target SDK 34, single `:app` module.

### Task 0.2 — Add Code Quality Gates
- ktlint + detekt, `kotlin.code.style=official`.

### Task 0.3 — Add Minimal Dependencies
- core-ktx, lifecycle, activity-compose, Compose BOM.

---

## 9. Phase 1 — Authentication & Device Check

### Task 1.1 — OAuth Interface Placeholder
- `AuthRepository` interface with `suspend fun signIn(): Result<Token>`.

### Task 1.2 — Device Capability Check
- `DeviceChecker` — RAM >= 4096MB, storage >= 2048MB.

### Task 1.3 — Sign-In and Device Check Screens
- `VyroSplashScreen`, `VyroOnboardingScreen`, `VyroSignInScreen`, `VyroDeviceCheckScreen`.

---

## 10. Phase 2 — YouTube Ingestion (Local)

### Task 2.1 — Add Downloader Dependency
- `youtubedl-android:library` + `:ffmpeg`.

### Task 2.2 — Downloader Wrapper
- `YoutubeDownloader.download(url, outputDir): File`.

### Task 2.3 — Metadata Scraper
- `MetadataScraper.scrape(url): VideoMetadata`.

---

## 11. Phase 3 — Non-AI Video Analysis

### Task 3.1 — Add Analysis Dependencies
- Amplituda, javacv, ffmpeg-kit-full.

### Task 3.2 — Audio Energy Extractor
### Task 3.3 — Silence Detection (FFmpeg silencedetect)
### Task 3.4 — Scene Change Detector (JavaCV histogram diff)
### Task 3.5 — Motion Intensity (optional)
### Task 3.6 — Clip Scorer

Formula: `score = 0.40*audio + 0.20*silence + 0.25*scene + 0.15*motion`

---

## 12. Phase 4 — Transcription & Speaker Tracking

### Task 4.1 — Add sherpa-onnx
### Task 4.2 — Transcribe Function
### Task 4.3 — Speaker Diarization
### Task 4.4 — Content Type Heuristic (PODCAST vs STREAMER)

---

## 13. Phase 5 — Streamer Cam Detection & Split

### Task 5.1 — ML Kit Face Detection
### Task 5.2 — Facecam Region Detector
### Task 5.3 — Split-Screen Command Builder (FFmpeg crop/scale/overlay)

---

## 14. Phase 6 — Title & Description (Non-AI)

### Task 6.1 — Add Xberg (YAKE)
### Task 6.2 — Template Engine (JSON templates per language)
### Task 6.3 — Description Hook (#Shorts if 9:16 and <= 60s)

---

## 15. Phase 7 — Aspect Ratio & Export

### Task 7.1 — Ratio Enum
### Task 7.2 — Export Function (FFmpeg)
### Task 7.3 — Export Screen with staged progress

---

## 16. Phase 8 — UI & Manual Editor

### Task 8.1 — Home Screen
### Task 8.2 — Analyzing Screen (7-step pipeline)
### Task 8.3 — Clip Results Screen
### Task 8.4 — Clip Quick Preview Sheet
### Task 8.5 — Manual Editor (Clip Open/Closed, tool dock, timeline, trim, undo/redo)
### Task 8.6 — Auto Style Sheet (Clean Minimal vs Dynamic)
### Task 8.7 — Projects and Uploads Screens

---

## 17. Phase 9 — Live Clipper (MVP)

### Task 9.1 — Add Live Dependencies (kusaanko/YouTubeLiveChat)
### Task 9.2 — YouTube Live Chat Ingestion
### Task 9.3 — EWMA Spike Detector
- Trigger: `msgCount > baseline * 2.5`, `uniqueChatters >= 5`, `reactionRatio > 0.4`, 30s debounce.

### Task 9.4 — Rolling Buffer Capture (MediaProjection, 60s circular buffer)
### Task 9.5 — Live Clipper Screen (stream preview, telemetry, spike graph, clip cards)
### Task 9.6 — Live Settings Sheet (sensitivity 2.0x/2.5x/3.0x)
### Task 9.7 — Live Clip Preview Sheet

---

## 18. Phase 10 — Onboarding, Settings & Library

### Task 10.1 — Splash and Onboarding
### Task 10.2 — Sign-In and Device Check Screens
### Task 10.3 — Settings Screen (language, ratio, export quality, OSS licenses)
### Task 10.4 — Unified Library

---

## 19. Phase 11 — Cloudflare Security Layer (Future)

Deferred until backend exists. Turnstile via WebView, Cloudflare Worker validation.

---

## 20. Phase 12 — Codebase Security Audit

Uses Cloudflare security-audit skill. Generates architecture.md, coverage-ledger.json. Zero runtime deps.

---

## 21. Phase 13 — VYRO Suite Roadmap

- VYRO Live standalone module
- VYRO Studio full timeline editor
- VYRO AI on-device content assist (future exploration)

---

## 22. Legal & Compliance

- Google Play: Copyright/Downloader Policy, Data Safety Form, Privacy Policy
- YouTube ToS: Third-party downloading violates ToS
- DMCA: Third-party downloading ruled anti-circumvention (S.D.N.Y.)
- GDPR/CCPA: On-device processing is advantage but not exemption
- OSS Licenses: youtubedl-android (GPL-3.0 copyleft), FFmpeg (LGPL/GPL)
- Required docs: Privacy Policy, EULA, ToS

---

## 23. Future Enhancements

| Enhancement | Blocker |
|-------------|---------|
| Auto-publish | OAuth + YouTube API compliance |
| VYRO Live standalone | feature-live Gradle module |
| VYRO Studio | Complex UI, keyframe engine |
| VYRO AI | Would relax no-generative-AI principle |
| Cloudflare Turnstile | Backend required |
| iOS | No youtubedl-android equivalent |

---

## 24. File Map

| File | Purpose |
|------|---------|
| `data/DeviceChecker.kt` | Capability check |
| `data/YoutubeDownloader.kt` | Local download |
| `data/MetadataScraper.kt` | Title/desc/tags scrape |
| `data/LiveChatWatcher.kt` | YouTube Live chat ingestion (Phase 9) |
| `domain/AudioEnergyAnalyzer.kt` | Amplituda wrapper |
| `domain/SilenceDetector.kt` | FFmpeg silencedetect |
| `domain/SceneChangeDetector.kt` | JavaCV histogram diff |
| `domain/ClipScorer.kt` | Score formula |
| `domain/Transcriber.kt` | sherpa-onnx wrapper |
| `domain/Diarizer.kt` | Speaker tracking |
| `domain/FacecamDetector.kt` | ML Kit face detection |
| `domain/SplitScreenCommand.kt` | FFmpeg filter graph |
| `domain/TitleGenerator.kt` | YAKE + templates |
| `domain/VideoExporter.kt` | Ratio + trim export |
| `domain/SpikeDetector.kt` | EWMA chat spike detection (Phase 9) |
| `service/RollingBufferService.kt` | MediaProjection rolling buffer (Phase 9) |
| `ui/VyroSplashScreen.kt` | Animated splash |
| `ui/VyroHomeScreen.kt` | Single-screen MVP entry |
| `ui/VyroAnalyzingScreen.kt` | Seven-step pipeline progress |
| `ui/VyroClipListScreen.kt` | Results list |
| `ui/VyroManualEditorScreen.kt` | Full timeline editor |
| `ui/VyroLiveClipperScreen.kt` | Live clipper |
| `ui/VyroExportScreen.kt` | Staged export progress |
| `ui/VyroSettingsScreen.kt` | Settings |
| `ui/components/VyroColors.kt` | Compose color tokens |
| `assets/templates/en.json` | Title/description templates |
| `assets/templates/id.json` | Title/description templates |

---

## 25. AI Agent Prompting Template

```text
Follow Ponytail rules (DietrichGebert/ponytail):
1. Read the task and existing code first.
2. Walk the ladder: YAGNI -> reuse -> stdlib -> native -> existing dep -> one-liner -> minimum code.
3. Do not add new dependencies unless explicitly stated.
4. Mark any shortcut with `ponytail: <ceiling>, <upgrade path>`.
5. Leave one runnable check for non-trivial logic.

Task: [paste Task number and description]
Files to touch: [list exact paths]
Do not touch any other files.
```

### Global Rules

- If a task exceeds 30 lines of new code, split it before writing.
- If a dependency is requested, confirm it is in the Library Stack.
- If unsure between two libraries, ask before adding either.
- If a step in Phases 11 or 12 is reached, stop and confirm.
- Never modify Phase 12 artifacts by hand.
- All new files use the `com.vyro.app` package and VYRO design tokens.
- The product is VYRO. Do not use the name "ClipForge" in any new code.

---

## 26. Reconciliation Note

The plan matches the production UI prototypes. Every screen, modal, component, and interaction in `clipforge_production_ui.html` has a corresponding task. Phase 9 (Live Clipper) promoted from deferred to MVP. Product rebranded ClipForge -> VYRO throughout.

---

*End of plan.*