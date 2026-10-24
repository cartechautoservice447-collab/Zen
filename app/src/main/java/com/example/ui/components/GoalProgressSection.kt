package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StudentProfile
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Sky500

@Composable
fun GoalProgressSection(
    profile: StudentProfile,
    modifier: Modifier = Modifier
) {
    val dailyProgress = (profile.todayFocusMinutes.toFloat() / profile.dailyGoalMinutes.toFloat()).coerceIn(0f, 1f)
    val weeklyProgress = (profile.weeklyStudyHours / profile.weeklyGoalHours).coerceIn(0f, 1f)
    val lessonsProgress = (profile.completedLessonsCount.toFloat() / 50f).coerceIn(0f, 1f) // semester milestone: 50 lessons

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .testTag("goal_progress_section"),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column {
                    Text(
                        text = "Daily & Weekly Goals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Study momentum & progress milestones",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Today's Study Target
            GoalProgressBarItem(
                title = "Today's Focus Goal",
                currentText = "${profile.todayFocusMinutes}m / ${profile.dailyGoalMinutes}m",
                statusText = if (profile.todayFocusMinutes >= profile.dailyGoalMinutes) "Goal Met (+5m surplus) 🎉" else "${profile.dailyGoalMinutes - profile.todayFocusMinutes}m remaining",
                progress = dailyProgress,
                accentColor = if (profile.todayFocusMinutes >= profile.dailyGoalMinutes) Emerald500 else MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Weekly Study-Hours Goal
            GoalProgressBarItem(
                title = "Weekly Study Commitment",
                currentText = "%.1fh / %.1fh".format(profile.weeklyStudyHours, profile.weeklyGoalHours),
                statusText = "36m remaining to reach 100%",
                progress = weeklyProgress,
                accentColor = Emerald500
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Semester Lesson Milestone
            GoalProgressBarItem(
                title = "Semester Lessons Milestone",
                currentText = "${profile.completedLessonsCount} / 50 modules",
                statusText = "${50 - profile.completedLessonsCount} modules to STEM Certificate",
                progress = lessonsProgress,
                accentColor = Sky500
            )
        }
    }
}

@Composable
fun GoalProgressBarItem(
    title: String,
    currentText: String,
    statusText: String,
    progress: Float,
    accentColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = currentText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = accentColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
