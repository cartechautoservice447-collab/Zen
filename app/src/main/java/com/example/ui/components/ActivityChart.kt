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
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val outlineColor = MaterialTheme.colorScheme.outline
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    var selectedRange by remember { mutableStateOf("7D") }
    var selectedDay by remember {
        mutableStateOf<DailyActivity?>(weeklyData.firstOrNull { it.day == "Thu" } ?: weeklyData.firstOrNull())
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .testTag("learning_activity_chart"),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 0.dp
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                            text = "Track your study velocity",
                            style = MaterialTheme.typography.labelSmall,
                            color = textColor
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = primaryColor.copy(alpha = 0.12f),
                    modifier = Modifier.clickable(onClick = onViewAnalyticsClick)
                ) {
                    Text(
                        text = "${String.format(java.util.Locale.US, "%.1f", totalHours)}h total",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Range Controls (7D, 30D, 90D)
            val ranges = listOf("7D", "30D", "90D")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ranges.forEach { range ->
                    val isSelected = selectedRange == range
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) primaryColor else surfaceVariant
                            )
                            .clickable { selectedRange = range }
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = range,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart Body (Canvas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    val maxHours = (weeklyData.maxOfOrNull { it.hours } ?: 3.0f).coerceAtLeast(3.5f)
                    val availableWidth = size.width
                    val availableHeight = size.height
                    val barCount = weeklyData.size
                    if (barCount == 0) return@Canvas

                    val barWidth = 22.dp.toPx()
                    val cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                    val spacing = availableWidth / (barCount + 1)

                    val targetHours = weeklyData.firstOrNull()?.targetHours ?: 1.5f
                    val targetY = availableHeight - (targetHours / maxHours * availableHeight).coerceIn(0f, availableHeight)

                    // Dashed target line at targetHours level (outline color, 1dp)
                    drawLine(
                        color = outlineColor.copy(alpha = 0.7f),
                        start = Offset(0f, targetY),
                        end = Offset(availableWidth, targetY),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    // Draw pillars and filled bars
                    weeklyData.forEachIndexed { index, activity ->
                        val barHeight = ((activity.hours / maxHours).coerceIn(0.06f, 1.0f)) * availableHeight
                        val x = spacing * (index + 1) - (barWidth / 2f)
                        val y = availableHeight - barHeight

                        val isCurrentDay = activity.day == "Thu"
                        val isGoalMet = activity.hours >= activity.targetHours || activity.isGoalMet

                        // Background pillar (surfaceVariant at 0.4f alpha, 22dp wide, full height, rounded 10dp corners)
                        drawRoundRect(
                            color = surfaceVariant.copy(alpha = 0.4f),
                            topLeft = Offset(x, 0f),
                            size = Size(barWidth, availableHeight),
                            cornerRadius = cornerRadius
                        )

                        // Filled bar from bottom (primary for "Thu", Mint500 for goal met, Sky500.copy(0.7f) for others)
                        val barColor = when {
                            isCurrentDay -> primaryColor
                            isGoalMet -> Mint500
                            else -> Sky500.copy(alpha = 0.7f)
                        }

                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, y),
                            size = Size(barWidth, barHeight),
                            cornerRadius = cornerRadius
                        )
                    }
                }
            }

            // Below canvas: Row of day labels with SpaceAround arrangement
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weeklyData.forEach { activity ->
                    val isSelected = selectedDay?.day == activity.day
                    val isToday = activity.day == "Thu"
                    val isGoalMet = activity.hours >= activity.targetHours || activity.isGoalMet

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedDay = activity }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = activity.day,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = when {
                                isToday -> primaryColor
                                isSelected -> MaterialTheme.colorScheme.onSurface
                                else -> textColor
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${activity.hours}h",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = if (isGoalMet) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isGoalMet) Mint500 else textColor
                        )
                    }
                }
            }

            // Selected Day Summary
            selectedDay?.let { day ->
                Spacer(modifier = Modifier.height(14.dp))
                val isMet = day.hours >= day.targetHours || day.isGoalMet
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(surfaceVariant)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${day.day}: ${day.hours}h logged • ${day.completedLessons} lessons",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isMet) "Target met" else "Below target",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isMet) Mint500 else textColor
                    )
                }
            }

            // Most Productive Day
            val mostProductive = weeklyData.maxByOrNull { it.hours }
            if (mostProductive != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Most productive: ${mostProductive.day} (${mostProductive.hours}h)",
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }
        }
    }
}
