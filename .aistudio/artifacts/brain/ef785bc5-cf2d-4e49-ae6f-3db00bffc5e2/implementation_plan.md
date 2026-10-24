# Implementation Plan: High-Performance Smooth Scroll & Rendering Engine

Eliminate vertical scroll stutter and UI lag in the Jetpack Compose learning dashboard, achieving silky 60/120fps scrolling and instant touch responsiveness identical to the web version.

## Root Cause Analysis
1. **`LazyColumn` Chunking Overhead**: Using `LazyColumn` for 7 massive composite dashboard sections causes Compose to measure and compose entire complex subtrees (dozens of nested cards, charts, and buttons) on the fly in a single 16ms frame budget whenever a section enters the viewport.
2. **GPU-Intensive Tinted Shadows**: Multiple `Modifier.shadow(..., ambientColor = Purple.copy(...), spotColor = ...)` calls on large rounded shapes force expensive multi-pass offscreen blending on every scroll tick.
3. **Uncached Paint & Brush Allocations**: Recreating `Brush.linearGradient` and calculating `WindowInsets` repeatedly during scroll frames causes memory allocations and garbage collection pauses.
4. **Nested Scroll Gesture Contention**: The horizontal course carousel inside the vertical scroll container creates touch event contention without smooth velocity handling.

---

## Proposed Changes

### 1. Cached Vertical Scroll Architecture (`verticalScroll`)
- Replace the monolithic `LazyColumn` with an optimized `Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()))`.
- Pre-measure and cache the layout once on initial composition so subsequent vertical scrolls simply translate the viewport via the hardware-accelerated GPU display list without any on-the-fly composition drops.

### 2. Rendering Optimization & GPU Shadow Removal
- Remove expensive software-simulated ambient/spot color shadows (`ambientColor`, `spotColor`) from cards, buttons, and bottom dock.
- Replace with clean border strokes (`border(1.dp, NightBorder)`) and subtle hardware elevation (`elevation = CardDefaults.cardElevation(...)` or `tonalElevation`), reducing per-frame draw overhead to near zero.

### 3. Static Brush & Gradient Memory Caching
- Cache all gradient brushes (`PurpleToViolet`, `FocusCardGradient`, `ChartBarGradient`, `ProgressGradient`) as top-level immutable constants rather than creating new gradient instances on each draw call.
- Hoist `WindowInsets.statusBars` reading out of the scrollable content or use stable window insets padding.

### 4. State Isolation & Recomposition Guarding
- Isolate the Pomodoro timer state into its own scoped component or separate state holder so timer tick updates never invalidate or trigger recomposition of the scrolling dashboard.
- Mark all data classes (`CourseItem`, `StudyTask`) with `@Immutable` to ensure Compose compiler skips all unmodified cards and rows during scrolling.

### 5. Horizontal Carousel Smoothness
- Optimize the `CoursesSection` horizontal row with `rememberScrollState()` and avoid reading dynamic screen width inside item compositions.

---

## Verification Plan

### Automated Tests
- Run `compile_applet` to verify compilation.
- Run `gradle :app:testDebugUnitTest` to ensure all unit tests pass with zero errors.

### Manual / Performance Verification
- Verify vertical scrolling is buttery smooth from top to bottom with zero stutter or dropped frames.
- Verify horizontal course scrolling moves cleanly without freezing or interrupting vertical flings.
- Confirm all interactive sheets (Focus Timer, Flashcards, Quiz, Notes) open instantly without delay.
