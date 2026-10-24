package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Achievement
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.ExamReminder
import com.example.model.StudentProfile
import com.example.ui.theme.Amber100
import com.example.ui.theme.Amber500
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Mint100
import com.example.ui.theme.Mint500
import com.example.ui.theme.Rose100
import com.example.ui.theme.Rose500
import com.example.ui.theme.Sky500
import java.util.Locale

@Composable
fun TodaysLearningGoals(
    profile: StudentProfile,
    weeklyActivity: List<DailyActivity>,
    modifier: Modifier = Modifier
) {
    val totalWeeklyHours = remember(weeklyActivity) {
        weeklyActivity.sumOf { it.hours.toDouble() }.toFloat()
    }
    val isDailyGoalMet = profile.todayFocusMinutes >= profile.dailyGoalMinutes
    val dailyProgress = if (profile.dailyGoalMinutes > 0) {
        (profile.todayFocusMinutes.toFloat() / profile.dailyGoalMinutes.toFloat()).coerceIn(0f, 1f)
    } else 0f
    val weeklyTargetHours = 15.0f
    val weeklyProgress = (totalWeeklyHours / weeklyTargetHours).coerceIn(0f, 1f)
    val dailyProgressColor = if (isDailyGoalMet) Mint500 else MaterialTheme.colorScheme.primary

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("todays_learning_goals"),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Target icon + Section title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TrackChanges,
                    contentDescription = null,
                    tint = dailyProgressColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Today's Learning Goals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Daily progress info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${profile.todayFocusMinutes}m / ${profile.dailyGoalMinutes}m",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                val remainingText = if (isDailyGoalMet) {
                    "Goal completed!"
                } else {
                    val remaining = profile.dailyGoalMinutes - profile.todayFocusMinutes
                    "${remaining}m remaining"
                }
                Text(
                    text = remainingText,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDailyGoalMet) Mint500 else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isDailyGoalMet) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { dailyProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = dailyProgressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Weekly summary row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${String.format(Locale.US, "%.1f", totalWeeklyHours)}h / 15.0h weekly",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${(weeklyProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Sky500
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { weeklyProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Sky500,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun MyCoursesSection(
    courses: List<Course>,
    onCourseClick: (Course) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("my_courses_section")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Courses",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(
                onClick = onViewAllClick,
                modifier = Modifier.testTag("my_courses_view_all_button")
            ) {
                Text(
                    text = "View All",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("my_courses_lazy_row"),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(courses, key = { it.id }) { course ->
                CourseCarouselCard(
                    course = course,
                    onClick = { onCourseClick(course) }
                )
            }
        }
    }
}

@Composable
fun UpcomingDeadlinesSection(
    exams: List<ExamReminder>,
    modifier: Modifier = Modifier
) {
    val displayExams = exams.take(3)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .testTag("upcoming_deadlines_section")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Rose500,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = "Upcoming Deadlines",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        displayExams.forEachIndexed { index, exam ->
            val urgencyDotColor = when {
                exam.daysLeft <= 5 -> Rose500
                exam.daysLeft <= 10 -> Amber500
                else -> MaterialTheme.colorScheme.outline
            }
            val badgeBgColor = when {
                exam.daysLeft <= 5 -> Rose100
                exam.daysLeft <= 10 -> Amber100
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            val badgeTextColor = when {
                exam.daysLeft <= 5 -> Rose500
                exam.daysLeft <= 10 -> Amber500
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .testTag("deadline_row_${exam.id}"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Urgency dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(urgencyDotColor)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Column: examTitle + subject & date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exam.examTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${exam.subject} • ${exam.date}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: Badge pill "{daysLeft}d"
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = badgeBgColor
                ) {
                    Text(
                        text = "${exam.daysLeft}d",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (index < displayExams.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}

@Composable
fun StudyInsightCard(
    profile: StudentProfile,
    weeklyActivity: List<DailyActivity>,
    modifier: Modifier = Modifier,
    onStartFocusClick: () -> Unit = {}
) {
    val mostProductiveDay = remember(weeklyActivity) {
        weeklyActivity.maxByOrNull { it.hours }?.day ?: "Mon"
    }
    val isDailyGoalMet = profile.todayFocusMinutes >= profile.dailyGoalMinutes
    val remainingMinutes = (profile.dailyGoalMinutes - profile.todayFocusMinutes).coerceAtLeast(0)

    val headline = when {
        profile.streakDays > 7 -> "Amazing! Your ${profile.streakDays}-day streak shows real commitment."
        isDailyGoalMet -> "You've crushed your daily goal today!"
        else -> "Focus for ${remainingMinutes}m more to hit today's goal."
    }

    val detail = when {
        profile.streakDays > 7 && isDailyGoalMet ->
            "Peak study day was $mostProductiveDay. Daily focus target is already complete!"
        profile.streakDays > 7 ->
            "Keep up the great work! Best performance this week was on $mostProductiveDay."
        isDailyGoalMet ->
            "Highest output achieved on $mostProductiveDay. Keep building strong learning habits!"
        else ->
            "Your most productive day this week was $mostProductiveDay. Every session brings you closer to your goal."
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("study_insight_card"),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Indigo100.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left: Lightbulb icon in a circle
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Indigo500.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Indigo500,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = headline,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = detail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!isDailyGoalMet) {
                        Spacer(modifier = Modifier.height(6.dp))
                        TextButton(
                            onClick = onStartFocusClick,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.testTag("insight_start_focus_button")
                        ) {
                            Text(
                                text = "Start Focus",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
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
            }
        }
    }
}

@Composable
fun AchievementsSection(
    achievements: List<Achievement>,
    streakDays: Int,
    totalHours: Float,
    modifier: Modifier = Modifier
) {
    val unlockedCount = remember(achievements) { achievements.count { it.isUnlocked } }
    val totalCount = achievements.size
    val displayAchievements = remember(achievements) {
        achievements.filter { it.isUnlocked }.take(4).ifEmpty { achievements.take(4) }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("achievements_section")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Amber500,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Achievements",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "\uD83C\uDFC6 $unlockedCount/$totalCount unlocked",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("achievements_lazy_row"),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(displayAchievements, key = { it.id }) { achievement ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .width(140.dp)
                        .height(80.dp)
                        .testTag("achievement_item_${achievement.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Amber100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Amber500,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = achievement.title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = achievement.progressText,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier
                        .width(140.dp)
                        .height(80.dp)
                        .testTag("achievement_summary_pill")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\uD83C\uDFC6 $unlockedCount/$totalCount\nunlocked",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Amber500,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
