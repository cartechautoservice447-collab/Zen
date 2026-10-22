# Learnly — Native Android Learning App
*“A little progress, every day.”*

Learnly is a premium, distraction-free native Android learning companion built with Kotlin, Jetpack Compose, and Material 3. Designed specifically for high school and college STEM students, Learnly synthesizes the structured curriculum tracking of Khan Academy, the minimalist checklist clarity of Todoist, the telemetry and streak momentum of Strava & HabitNow, and the disciplined data visualization of Money Manager into a cohesive, tactile mobile experience.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The following product decisions were confirmed during the Phase 1 clarification interview and guide the architecture of this release:

- **Curriculum & Content Focus**: High School & College STEM curriculum (Multivariable Calculus, Classical Mechanics & Physics, Data Structures & Algorithms, Organic Chemistry, Linear Algebra) with realistic progress states, module breakdowns, and active quizzes.
- **Focus Mode Architecture**: Dual-mode study timer featuring both interval-based Pomodoro (25m Focus / 5m Break / 15m Long Break with auto-cycling) and a flexible stopwatch timer with lap/session logging.
- **Onboarding Experience**: Full interactive 4-step onboarding flow (Welcome with tagline -> Student Persona Selection -> STEM Subject Preferences -> Daily Commitment Goal) with direct Google / Guest sign-in and a quick "Skip to Dashboard" capability.
- **Visual & Theme Architecture**: Restrained editorial aesthetic with Deep Indigo (`#2A4365` / `#4F46E5`) primary tone, Sky Blue (`#0EA5E9`) and Mint Green (`#10B981`) accent indicators, warm tinted neutral surface canvas, high-contrast readable typography, and complete Dark Theme parity.
- **Edge-to-Edge & Dashboard Styling Rule**: Strict adherence to the dashboard layout rule: only KPI cards and the activity chart are placed in rounded boxed containers, while section lists (Today's Schedule, Assignments, Continue Learning) use clean unboxed edge-to-edge typography and dividers to eliminate nested card fatigue.

---

## 1. Overview & Core Concept

- **What It Does**: Learnly provides an end-to-end learning workspace for students to track enrolled STEM courses, plan daily timetable slots, manage assignment deadlines with checkoff states, execute distraction-free Pomodoro/stopwatch study sessions, visualize study velocity (weekly hours, subject breakdown, completion streaks), and configure personalized study goals.
- **Target Audience / Persona**: High school and college STEM learners who value clean, clutter-free utility over gamified distraction, seeking steady daily academic momentum.
- **Key Value**: Unifies academic scheduling, coursework tracking, focus timing, and habit analytics into one seamless, responsive native Android app without subscription popups or cluttered UI.

---

## 2. User Experience & Visual Design

### Screen Flow & Information Architecture

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Learnly App Flow Graph                          │
└────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
       ┌──────────────────────────────────────────────────────────┐
       │     Onboarding & Auth Flow (4 Steps + Skip / Login)      │
       │  1: Welcome ──► 2: Level ──► 3: Subjects ──► 4: Daily Goal│
       └────────────────────────────┬─────────────────────────────┘
                                    │ (Complete / Skip)
                                    ▼
       ┌──────────────────────────────────────────────────────────┐
       │             Persistent Native Bottom Bar                 │
       │   [Home]     [Courses]     [Planner]   [Progress]  [Profile] │
       └─────┬────────────┬─────────────┬───────────┬───────────┬─────┘
             │            │             │           │           │
      ┌──────┴─────┐ ┌────┴─────┐ ┌─────┴─────┐ ┌───┴────┐ ┌────┴─────┐
      │1. Home     │ │2. Courses│ │3. Planner │ │4. Prog-│ │5. Profile│
      │Dashboard   │ │• Search  │ │• Timetable│ │   ress │ │• Student │
      │• Momentum  │ │• Filters │ │• Deadlines│ │• Strava│ │  Info    │
      │• KPI Cards │ │• Enrolled│ │• Exam Cnt-│ │  Graph │ │• Prefs   │
      │• Activity  │ │• Lesson  │ │  down     │ │• Streak│ │• Theme   │
      │  Chart     │ │  Sheet   │ │• Add Task │ │• Badges│ │  Toggle  │
      │• Schedule  │ └──────────┘ └───────────┘ └────────┘ └──────────┘
      │• Quick Act.│
      └──────┬─────┘
             │ (Tap "Focus Mode" Quick Action or Focus Pill)
             ▼
      ┌──────────────────────────────────────────────────────────┐
      │           6. Focus Mode (Interactive Session)             │
      │  • Dual Toggle: Pomodoro (25/5/15) vs Stopwatch Mode     │
      │  • Animated Circular Timer & Live Progress Arc           │
      │  • Active Subject Selector & Daily Study Time Log        │
      └──────────────────────────────────────────────────────────┘
```

### Visual Identity & Theme Tokens

- **Light Color Palette**:
  - `Primary`: `#4338CA` (Deep Indigo)
  - `OnPrimary`: `#FFFFFF`
  - `PrimaryContainer`: `#EEF2FF` (Soft Lavender Tint)
  - `Secondary`: `#0284C7` (Sky Teal)
  - `Tertiary`: `#059669` (Emerald Mint - Streak & Completion)
  - `Background`: `#F8FAFC` (Slate Neutral 50)
  - `Surface`: `#FFFFFF`
  - `SurfaceVariant`: `#F1F5F9` (Slate 100)
  - `Outline`: `#E2E8F0`
- **Dark Color Palette**:
  - `Primary`: `#818CF8` (Bright Periwinkle)
  - `OnPrimary`: `#1E1B4B`
  - `PrimaryContainer`: `#312E81`
  - `Secondary`: `#38BDF8`
  - `Tertiary`: `#34D399`
  - `Background`: `#0F172A` (Deep Slate Midnight)
  - `Surface`: `#1E293B`
  - `SurfaceVariant`: `#334155`
  - `Outline`: `#475569`
- **Typography & Rhythm**:
  - Heading Display: Crisp Sans (Inter / Roboto font weights from 600 to 700 with generous line height).
  - Body & Numbers: Tabular numerals for timer countdowns and stats counters to prevent jitter.
- **Component Styling & Layout Rules**:
  - Touch targets minimum 48dp on all chips, buttons, list items, and tabs.
  - KPI Stat cards in Home: Rounded 20dp cards in a horizontal scroll row displaying Streak, Today's Focus, Lesson Velocity, and Exam Countdown.
  - Learning Activity Graph: Dedicated custom Canvas line/bar chart displaying daily study hours over the week with rounded container.
  - Section lists (Schedule, Deadlines, Assignments) rendered directly on surface without heavy outer card shells to respect the dashboard layout rule.
  - Haptic feedback and subtle animated state changes for task completion and timer states.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Dual-Mode Study Timer (Pomodoro + Stopwatch)**
  - *Chosen Approach*: Provide a clean segment toggle at the top of Focus Mode to switch between classic 25m/5m Pomodoro cycles and open-ended Stopwatch timing.
  - *Why*: Direct user choice from Turn 1; caters to both structured review intervals and open-ended deep problem-solving sessions (e.g., math derivations, coding).
  - *Trade-off*: Adds slight state complexity, handled cleanly via a dedicated `FocusTimerViewModel` managing coroutine timer loops.
- **Decision 2: Edge-to-Edge Single-Activity Navigation with StateFlow**
  - *Chosen Approach*: Single `MainActivity` with Material 3 `NavigationBar` driving the 5 primary tabs, while Focus Mode and Lesson Detail sheets open with smooth transition overlays or bottom sheets with `BackHandler` support.
  - *Why*: Instant transitions without navigation graph overhead or fragment lifecycle bugs, maintaining smooth interactive prototype fidelity.
- **Decision 3: Realistic In-Memory & Local Reactive State Architecture**
  - *Chosen Approach*: Robust Kotlin `LearnlyRepository` with comprehensive initial STEM data models (Calculus, Quantum Physics, Algorithms), supporting instant task toggling, adding custom study tasks, logging focus minutes into the weekly graph, and resetting demo data.
  - *Why*: Provides an immediately responsive, zero-friction interactive experience on device boot while maintaining production-grade clean architecture.

---

## 4. Technical Architecture & Data Strategy

### System & Component Hierarchy

```
┌────────────────────────────────────────────────────────────────────────┐
│                        Learnly App Architecture                        │
└────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
       ┌──────────────────────────────────────────────────────────┐
       │                      MainActivity                        │
       │  • enableEdgeToEdge()                                    │
       │  • LearnlyTheme (Dark / Light toggleable)                │
       │  • Scaffold with NavigationBar & WindowInsets            │
       └────────────────────────────┬─────────────────────────────┘
                                    │
         ┌──────────────────────────┴──────────────────────────┐
         ▼                                                     ▼
┌───────────────────────────────┐             ┌───────────────────────────────┐
│     LearnlyViewModel          │             │     FocusTimerViewModel       │
│ • currentTab & navigation     │             │ • timerMode (Pomodoro/Stop)   │
│ • studentProfile State        │             │ • remainingSeconds & totalSec │
│ • enrolledCourses State       │             │ • isRunning & activeSubject   │
│ • timetable & assignments     │             │ • completedSessionsLog        │
│ • weeklyActivityData State    │             │ • coroutine ticker engine     │
└───────────────┬───────────────┘             └───────────────┬───────────────┘
                │                                             │
                ▼                                             ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                             UI Screen Layer                                 │
├─────────────────┬──────────────────┬─────────────────┬──────────────────────┤
│ 1. HomeScreen   │ 2. CoursesScreen │ 3. PlannerScreen│ 4. FocusScreen       │
│ • Momentum KPI  │ • Category Pills │ • Timetable view│ • Radial timer arc   │
│ • ActivityChart │ • Course Cards   │ • Assignment chk│ • Controls & Subject │
│ • Schedule list │ • Lesson Sheet   │ • Add Task modal│ • Session History    │
├─────────────────┴──────────────────┴─────────────────┴──────────────────────┤
│ 5. ProgressScreen                  │ 6. ProfileScreen │ 7. OnboardingScreen │
│ • Weekly Strava-like chart         │ • Settings & Bio │ • 4-Step wizard     │
│ • Subject breakdown progress       │ • Dark theme     │ • Grade / Goal pick │
│ • Streak matrix & Achievement grid │ • Reset demo     │ • Skip to dashboard │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           Data & Domain Layer                               │
│ • LearnlyRepository (In-memory reactive store with initial STEM data)       │
│ • Models: Course, Lesson, TaskItem, ScheduleSlot, DailyActivity, Badge      │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Data Models & Interactive State Mapping

1. **`Course` & `Lesson`**: Title, subject code (e.g., "MATH 201"), progress percentage (0-100%), total lessons, completed lessons, instructor, color tag.
2. **`TaskItem`**: ID, title, subject, due date text, isCompleted boolean (tappable checkbox with immediate strikethrough and haptic feedback), priority level (High, Medium, Normal).
3. **`ScheduleSlot`**: Time span (e.g. "09:00 - 10:30 AM"), class name, room/location, instructor, isNowActive flag.
4. **`DailyActivity`**: Day of week (Mon-Sun), focus minutes logged, lessons completed, target reached flag.
5. **`AchievementBadge`**: Title, description, icon, unlocked status, progress ratio.
6. **`FocusSession`**: Timestamp, subject, durationMinutes, mode (Pomodoro or Stopwatch).

---

## 5. Verification & Implementation Sequence

1. **Resource & Setup**:
   - Update `strings.xml` with "Learnly" app name and descriptive labels.
   - Sync `metadata.json` with app title "Learnly" and description.
   - Configure theme tokens (`Color.kt`, `Theme.kt`, `Type.kt`) with restrained indigo/slate palette and dark theme support.
2. **Data Layer & Repository**:
   - Create clean model classes (`Course.kt`, `Planner.kt`, `Analytics.kt`, `User.kt`).
   - Implement `LearnlyRepository` with realistic STEM coursework, schedule items, and activity metrics.
3. **ViewModels**:
   - Implement `LearnlyViewModel` managing active tab, onboarding status, search queries, category filters, task completion, and theme override.
   - Implement `FocusTimerViewModel` managing dual Pomodoro/Stopwatch state, play/pause/reset actions, and logging.
4. **UI Components & Screens**:
   - Reusable components: `KpiStatCard`, `ActivityChartCanvas`, `CourseCard`, `TaskRowItem`, `AchievementCard`, `FocusCircularTimer`.
   - Implement `OnboardingScreen` (4 steps with skip).
   - Implement `HomeScreen` (KPIs, Activity Graph, Schedule, Quick Actions).
   - Implement `CoursesScreen` (Category filter chips, search bar, course grid/list, lesson preview dialog).
   - Implement `PlannerScreen` (Daily timetable, deadlines checklist, exam reminder pill, add task bottom sheet).
   - Implement `FocusScreen` (Pomodoro/Stopwatch toggle, radial progress, session log).
   - Implement `ProgressScreen` (Strava-style weekly breakdown, subject distribution, streak grid, achievements).
   - Implement `ProfileScreen` (Student info, stats summary, daily goal setting, theme selector, about).
5. **Compilation & Verification**:
   - Compile using `compile_applet` to verify clean builds without lint or type errors.
   - Verify edge-to-edge system insets and touch target responsiveness.
