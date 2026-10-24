package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.ExamReminder
import com.example.model.FlashcardItem
import com.example.model.StudentProfile
import com.example.model.StudyBuddyPeer
import com.example.model.TaskItem
import com.example.model.TimetableSlot
import com.example.ui.components.MidnightContinueLearningCard
import com.example.ui.components.DailyStreakHeatmapSection
import com.example.ui.components.ExamCountdownMatrixSection
import com.example.ui.components.MidnightTopHeader
import com.example.ui.components.MyCoursesCarousel
import com.example.ui.components.QuickFlashcardDeckSection
import com.example.ui.components.StudyBuddyPeerSection
import com.example.ui.components.StudyStatisticsGrid
import com.example.ui.components.StudyToolsGrid
import com.example.ui.components.TodaysFocusCard
import com.example.ui.components.TodaysPlanSection
import com.example.ui.components.WeeklyActivityCard
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightFgMuted
import com.example.ui.theme.MidnightForeground
import com.example.ui.theme.MidnightSurface2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    studentProfile: StudentProfile,
    courses: List<Course>,
    tasks: List<TaskItem>,
    schedule: List<TimetableSlot>,
    weeklyActivity: List<DailyActivity>,
    examReminders: List<ExamReminder>,
    achievements: List<Achievement>,
    flashcards: List<FlashcardItem>,
    studyBuddies: List<StudyBuddyPeer>,
    onToggleTask: (String) -> Unit,
    onStartFocusSession: () -> Unit,
    onCourseClick: (Course) -> Unit,
    onSeeAllCourses: () -> Unit,
    onSeeFullPlan: () -> Unit,
    onToggleFlashcardMastered: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var activeToolSheet by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentContinueCourse = remember(courses) {
        courses.firstOrNull { it.id == "chem-2" } ?: courses.firstOrNull() ?: Course(
            id = "chem-2",
            title = "Organic Chemistry II",
            code = "CHEM 202",
            category = "Chemistry",
            instructor = "Prof. Rivera",
            progress = 42,
            totalLessons = 28,
            completedLessons = 12,
            colorHex = 0xFF7266FF,
            nextLessonTitle = "Reaction Mechanisms: SN1 vs SN2",
            description = "Advanced mechanisms and stereochemistry.",
            durationMinutes = 24,
            accentName = "violet"
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MidnightBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_screen_scroll"),
            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Header: Avatar, Greeting, Streak pill, Bell button (Ed)
            item(key = "header_section") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 430.dp)
                ) {
                    MidnightTopHeader(
                        profile = studentProfile,
                        onNotificationsClick = { activeToolSheet = "notifications" },
                        onStreakClick = { activeToolSheet = "streak" }
                    )
                }
            }

            // 2. Main content container constrained to max-width 430dp matching mobile viewport
            item(key = "dashboard_body") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 430.dp)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Op: Today's Focus Card
                    TodaysFocusCard(
                        studiedMinutes = studentProfile.todayFocusMinutes,
                        targetMinutes = studentProfile.dailyGoalMinutes,
                        headline = studentProfile.focusHeadline,
                        onStartStudying = onStartFocusSession
                    )

                    // Mp: Continue Learning Card
                    MidnightContinueLearningCard(
                        course = currentContinueCourse,
                        onResume = { onCourseClick(currentContinueCourse) }
                    )

                    // nU: Weekly Activity Card
                    WeeklyActivityCard(
                        weeklyActivity = weeklyActivity
                    )

                    // iU: Study Statistics Grid
                    StudyStatisticsGrid(
                        profile = studentProfile
                    )

                    // sU: My Courses Carousel
                    MyCoursesCarousel(
                        courses = courses,
                        onSeeAll = onSeeAllCourses,
                        onCourseClick = onCourseClick
                    )

                    // yW: Today's Plan Checklist
                    TodaysPlanSection(
                        tasks = tasks,
                        onToggleTask = onToggleTask,
                        onSeeFullPlan = onSeeFullPlan
                    )

                    // xW: Study Tools Grid
                    StudyToolsGrid(
                        onNotesClick = { activeToolSheet = "notes" },
                        onFlashcardsClick = { activeToolSheet = "flashcards" },
                        onQuizClick = { activeToolSheet = "quiz" },
                        onTimerClick = onStartFocusSession
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Active Tool Modal Bottom Sheet (Notes, Flashcards, Quiz, Notifications)
        if (activeToolSheet != null) {
            ModalBottomSheet(
                onDismissRequest = { activeToolSheet = null },
                sheetState = sheetState,
                containerColor = MidnightCard,
                dragHandle = null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (activeToolSheet) {
                                "notes" -> "Study Notes"
                                "flashcards" -> "Active Recall Deck"
                                "quiz" -> "Practice Quiz"
                                "notifications" -> "Notifications"
                                "streak" -> "Streak & Multiplier"
                                else -> "Study Tool"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightForeground
                        )
                        IconButton(onClick = { activeToolSheet = null }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MidnightFgMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    when (activeToolSheet) {
                        "flashcards" -> {
                            QuickFlashcardDeckSection(
                                flashcards = flashcards,
                                onToggleMastered = onToggleFlashcardMastered
                            )
                        }
                        "notes" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "Organic Chemistry II · Reaction Mechanisms",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MidnightForeground
                                )
                                Text(
                                    text = "• SN1: Unimolecular nucleophilic substitution. Rate = k[R-X]. Two-step mechanism via carbocation intermediate. Leads to racemization.\n• SN2: Bimolecular concerted mechanism. Rate = k[R-X][Nu⁻]. Stereochemical inversion (Walden inversion). Favored by polar aprotic solvents (DMSO, DMF, acetone).\n• LIATE Rule for Integration: Logarithmic, Inverse trigonometric, Algebraic, Trigonometric, Exponential.",
                                    fontSize = 13.sp,
                                    color = MidnightFgMuted,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                        "quiz" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Quick Knowledge Check (Organic Chemistry II)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MidnightForeground
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MidnightSurface2)
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = "Q: Which solvent strongly favors SN2 reactions?\nA) Ethanol (protic)\nB) Acetone (polar aprotic) ✓\nC) Water (polar protic)\nD) Acetic acid (polar protic)",
                                        fontSize = 13.sp,
                                        color = MidnightForeground,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                        "notifications" -> {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(
                                    text = "Daily Target Reminder",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MidnightForeground
                                )
                                Text(
                                    text = "You're 45 minutes away from completing your 120 min daily focus target. Your 12-day streak is active!",
                                    fontSize = 13.sp,
                                    color = MidnightFgMuted
                                )
                            }
                        }
                        "streak" -> {
                            DailyStreakHeatmapSection(
                                profile = studentProfile
                            )
                        }
                    }
                }
            }
        }
    }
}
