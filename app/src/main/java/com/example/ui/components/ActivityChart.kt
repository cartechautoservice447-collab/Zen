package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyActivity
import com.example.ui.theme.Mint500
import com.example.ui.theme.Sky500

@Composable
fun ActivityChartContainer(
    weeklyData: List<DailyActivity>,
    modifier: Modifier = Modifier,
    onViewAnalyticsClick: () -> Unit = {}
) {
    val totalHours = weeklyData.sumOf { it.hours.toDouble() }.toFloat()
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val outlineColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant
    val goalColor = Mint500

    var selectedDay by remember { mutableStateOf<DailyActivity?>(weeklyData.firstOrNull { it.day == "Thu" }) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .testTag("learning_activity_chart"),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(primaryColor.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoGraph,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Learning Activity",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Weekly velocity & study hours",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = primaryColor.copy(alpha = 0.1f),
                    modifier = Modifier.clickable(onClick = onViewAnalyticsClick)
                ) {
                    Text(
                        text = "%.1fh total".format(totalHours),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Canvas Bar Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(130.dp)) {
                    val maxHours = 3.5f
                    val availableWidth = size.width
                    val availableHeight = size.height - 20.dp.toPx()
                    val barCount = weeklyData.size
                    val spacing = availableWidth / (barCount + 1)
                    val barWidth = 24.dp.toPx()

                    // Draw dashed target line (1.5 hrs)
                    val targetY = availableHeight - (1.5f / maxHours * availableHeight)
                    drawLine(
                        color = outlineColor.copy(alpha = 0.6f),
                        start = Offset(0f, targetY),
                        end = Offset(availableWidth, targetY),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    // Draw bars for each day
                    weeklyData.forEachIndexed { index, activity ->
                        val barHeight = ((activity.hours / maxHours).coerceIn(0.08f, 1.0f)) * availableHeight
                        val x = spacing * (index + 1) - (barWidth / 2f)
                        val y = availableHeight - barHeight

                        val isCurrentDay = activity.day == "Thu"
                        val isGoalMet = activity.hours >= activity.targetHours

                        // Background pillar
                        drawRoundRect(
                            color = surfaceVariant.copy(alpha = 0.5f),
                            topLeft = Offset(x, 0f),
                            size = Size(barWidth, availableHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )

                        // Filled active bar
                        val barColor = when {
                            isCurrentDay -> primaryColor
                            isGoalMet -> Mint500
                            else -> Sky500.copy(alpha = 0.8f)
                        }

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                    }
                }
            }

            // Day labels and interactive selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weeklyData.forEach { activity ->
                    val isSelected = selectedDay?.day == activity.day
                    val isToday = activity.day == "Thu"

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedDay = activity }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = activity.day,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) primaryColor else if (isSelected) MaterialTheme.colorScheme.onSurface else textColor
                        )
                        Text(
                            text = "%.1fh".format(activity.hours),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = if (activity.hours >= activity.targetHours) Mint500 else textColor
                        )
                    }
                }
            }

            // Selected day dynamic tip/stat
            selectedDay?.let { day ->
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceVariant.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${day.day}: ${day.hours}h logged • ${day.completedLessons} lessons finished",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (day.hours >= day.targetHours) "Target met" else "Below target",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (day.hours >= day.targetHours) Mint500 else textColor
                    )
                }
            }
        }
    }
}
