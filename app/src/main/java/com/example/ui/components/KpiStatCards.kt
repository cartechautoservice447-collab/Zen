package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.ExamReminder
import com.example.model.StudentProfile
import com.example.ui.theme.Amber500
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Mint500
import com.example.ui.theme.Rose500
import com.example.ui.theme.Sky500

@Composable
fun KpiStatCardsRow(
    profile: StudentProfile,
    nextExam: ExamReminder?,
    onCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("kpi_cards_row"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        // 1. Study Streak
        item {
            KpiStatCard(
                title = "Study Streak",
                value = "${profile.streakDays} Days",
                subtitle = "Keep it up!",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = Amber500,
                tag = "streak_kpi",
                onClick = { onCardClick("streak") }
            )
        }

        // 2. Today's Focus
        item {
            val goalReached = profile.todayFocusMinutes >= profile.dailyGoalMinutes
            KpiStatCard(
                title = "Today's Focus",
                value = "${profile.todayFocusMinutes}m",
                subtitle = if (goalReached) "Daily goal met 🎉" else "Goal: ${profile.dailyGoalMinutes}m",
                icon = Icons.Default.Timer,
                accentColor = Mint500,
                tag = "focus_kpi",
                onClick = { onCardClick("focus") }
            )
        }

        // 3. Weekly Hours
        item {
            val hoursValue = if (profile.totalStudyHours % 1f == 0f) {
                "${profile.totalStudyHours.toInt()}h"
            } else {
                "${profile.totalStudyHours}h"
            }
            KpiStatCard(
                title = "Weekly Hours",
                value = hoursValue,
                subtitle = "Total study time",
                icon = Icons.Default.Schedule,
                accentColor = Indigo500,
                tag = "hours_kpi",
                onClick = { onCardClick("hours") }
            )
        }

        // 4. Weekly Goal
        item {
            KpiStatCard(
                title = "Weekly Goal",
                value = "96%",
                subtitle = "14.4/15h",
                icon = Icons.Default.TrendingUp,
                accentColor = Rose500,
                tag = "goal_kpi",
                onClick = { onCardClick("goal") }
            )
        }

        // 5. Lessons Done
        item {
            KpiStatCard(
                title = "Lessons Done",
                value = "${profile.completedLessonsCount}",
                subtitle = "+4 this week",
                icon = Icons.Default.CheckCircle,
                accentColor = Sky500,
                tag = "lessons_kpi",
                onClick = { onCardClick("lessons") }
            )
        }

        // 6. XP Points
        item {
            val xp = profile.completedLessonsCount * 10 + profile.streakDays * 5
            KpiStatCard(
                title = "XP Points",
                value = "$xp XP",
                subtitle = "Earned score",
                icon = Icons.Default.Star,
                accentColor = Amber500,
                tag = "xp_kpi",
                onClick = { onCardClick("xp") }
            )
        }
    }
}

@Composable
private fun KpiStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(148.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag(tag),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Row: Title + Icon Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // Bottom Column: Large Value + Subtitle
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
