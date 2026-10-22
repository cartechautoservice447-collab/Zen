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
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamReminder
import com.example.model.StudentProfile
import com.example.ui.theme.Amber500
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
        item {
            KpiStatCard(
                title = "Study Streak",
                value = "${profile.streakDays} Days",
                subtitle = "Active habit streak",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = Amber500,
                tag = "streak_kpi",
                onClick = { onCardClick("streak") }
            )
        }
        item {
            val goalReached = profile.todayFocusMinutes >= profile.dailyGoalMinutes
            KpiStatCard(
                title = "Today's Focus",
                value = "${profile.todayFocusMinutes}m",
                subtitle = if (goalReached) "Daily goal met 🎉" else "Goal: ${profile.dailyGoalMinutes}m",
                icon = Icons.Default.Timer,
                accentColor = if (goalReached) Mint500 else MaterialTheme.colorScheme.primary,
                tag = "focus_kpi",
                onClick = { onCardClick("focus") }
            )
        }
        item {
            KpiStatCard(
                title = "Lessons Finished",
                value = "${profile.completedLessonsCount}",
                subtitle = "+4 this week",
                icon = Icons.Default.CheckCircle,
                accentColor = Sky500,
                tag = "lessons_kpi",
                onClick = { onCardClick("lessons") }
            )
        }
        item {
            val examText = nextExam?.let { "${it.subject} Midterm" } ?: "Calculus II"
            val examDays = nextExam?.let { "In ${it.daysLeft} days" } ?: "In 4 days"
            KpiStatCard(
                title = "Upcoming Exam",
                value = examDays,
                subtitle = examText,
                icon = Icons.Default.EventNote,
                accentColor = Rose500,
                tag = "exam_kpi",
                onClick = { onCardClick("exam") }
            )
        }
    }
}

@Composable
fun KpiStatCard(
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
            .width(160.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag(tag),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}
