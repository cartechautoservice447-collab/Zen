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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.ExamReminder
import com.example.model.StudentProfile
import com.example.model.TaskItem
import com.example.model.TimetableSlot
import com.example.ui.components.ActivityChartContainer
import com.example.ui.components.AchievementsSection
import com.example.ui.components.ContinueLearningCard
import com.example.ui.components.KpiStatCardsRow
import com.example.ui.components.LearnlyTopBar
import com.example.ui.components.MyCoursesSection
import com.example.ui.components.StudyInsightCard
import com.example.ui.components.TaskRowItem
import com.example.ui.components.TodaysLearningGoals
import com.example.ui.components.UpcomingDeadlinesSection

@Composable
fun HomeScreen(
    studentProfile: StudentProfile,
    primaryCourse: Course?,
    courses: List<Course>,
    tasks: List<TaskItem>,
    schedule: List<TimetableSlot>,
    weeklyActivity: List<DailyActivity>,
    nextExam: ExamReminder?,
    examReminders: List<ExamReminder>,
    achievements: List<Achievement>,
    onStartFocusClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onViewAllCoursesClick: () -> Unit,
    onViewAllPlannerClick: () -> Unit,
    onViewAllProgressClick: () -> Unit,
    onToggleTask: (String) -> Unit,
    onResumeCourse: (Course) -> Unit,
    onCourseClick: (Course) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── A. Welcome Header ──────────────────────────────────────
        item {
            LearnlyTopBar(
                studentName = studentProfile.name,
                streakDays = studentProfile.streakDays,
                onAvatarClick = onProfileClick,
                onNotificationClick = onViewAllPlannerClick
            )
        }

        // ── B. Learning Statistics Carousel ────────────────────────
        item {
            Spacer(modifier = Modifier.height(4.dp))
            KpiStatCardsRow(
                profile = studentProfile,
                nextExam = nextExam,
                onCardClick = { cardType ->
                    when (cardType) {
                        "focus" -> onStartFocusClick()
                        "streak", "lessons", "weekly_hours", "xp" -> onViewAllProgressClick()
                        "exam", "weekly_goal" -> onViewAllPlannerClick()
                    }
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        // ── C. Quick Actions ───────────────────────────────────────
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onStartFocusClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("home_start_focus_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Focus Session",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }

                OutlinedButton(
                    onClick = onAddTaskClick,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("home_add_task_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Add New Task",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── D. Continue Learning — Featured Course ─────────────────
        primaryCourse?.let { course ->
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Continue Learning",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        TextButton(onClick = onViewAllCoursesClick) {
                            Text(
                                "All Courses",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ContinueLearningCard(
                        course = course,
                        onContinueClick = { onResumeCourse(course) }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // ── E. Today's Learning Goals ──────────────────────────────
        item {
            TodaysLearningGoals(
                profile = studentProfile,
                weeklyActivity = weeklyActivity,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── F. Learning Activity Chart ─────────────────────────────
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                ActivityChartContainer(
                    weeklyData = weeklyActivity,
                    onViewAnalyticsClick = onViewAllProgressClick
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── G. My Courses ──────────────────────────────────────────
        if (courses.size > 1) {
            item {
                MyCoursesSection(
                    courses = courses,
                    onCourseClick = onCourseClick,
                    onViewAllClick = onViewAllCoursesClick
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // ── H. Today's Schedule ────────────────────────────────────
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Today's Schedule",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextButton(onClick = onViewAllPlannerClick) {
                        Text(
                            "View Planner",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        items(schedule.take(3), key = { it.id }) { slot ->
            ScheduleRowItem(
                slot = slot,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        // ── I. Assignments and Tasks ───────────────────────────────
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Assignments & Tasks",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${tasks.count { it.isCompleted }}/${tasks.size} done",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        items(tasks.take(4), key = { it.id }) { task ->
            TaskRowItem(
                task = task,
                onToggle = { onToggleTask(task.id) },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        if (tasks.size > 4) {
            item {
                TextButton(
                    onClick = onViewAllPlannerClick,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        "View All Tasks",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // ── J. Upcoming Deadlines ──────────────────────────────────
        if (examReminders.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                UpcomingDeadlinesSection(
                    exams = examReminders
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // ── K. Study Insights ──────────────────────────────────────
        item {
            StudyInsightCard(
                profile = studentProfile,
                weeklyActivity = weeklyActivity,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── L. Achievements & Milestones ───────────────────────────
        if (achievements.isNotEmpty()) {
            item {
                AchievementsSection(
                    achievements = achievements,
                    streakDays = studentProfile.streakDays,
                    totalHours = studentProfile.totalStudyHours
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ScheduleRowItem(
    slot: TimetableSlot,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time column
            Column(modifier = Modifier.width(88.dp)) {
                Text(
                    text = slot.timeRange.split("-").firstOrNull()?.trim() ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (slot.isNow) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = slot.timeRange.split("-").getOrNull(1)?.trim() ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // Timeline bar
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(1.5.dp))
                    .background(
                        if (slot.isNow) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = slot.subjectName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (slot.isNow) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "NOW",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(
                                    horizontal = 5.dp,
                                    vertical = 1.dp
                                )
                            )
                        }
                    }
                }

                Text(
                    text = "${slot.subjectCode} · ${slot.roomLocation} · ${slot.instructor}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            thickness = 0.5.dp
        )
    }
}
