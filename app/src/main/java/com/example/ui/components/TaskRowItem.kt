package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.model.TaskItem
import com.example.model.TaskPriority
import com.example.ui.theme.Amber500
import com.example.ui.theme.Mint500
import com.example.ui.theme.Rose500

@Composable
fun TaskRowItem(
    task: TaskItem,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .testTag("task_item_${task.id}"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Circular checkbox (24dp)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .then(
                        if (task.isCompleted) {
                            Modifier.background(Mint500)
                        } else {
                            Modifier.border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                shape = CircleShape
                            )
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Task info
            Column(modifier = Modifier.weight(1f)) {
                // Task title: bodyMedium, Medium weight. Completed: onSurfaceVariant at 0.5f alpha + LineThrough. Not completed: onSurface
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (task.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Below title: Row with subject chip (primary colored bg, labelSmall Bold) + due date (labelSmall) + priority pill if HIGH/MEDIUM
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Subject chip
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = task.subjectCode,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Due date: overdue (starts with "Today") in Rose500
                    val isOverdue = task.dueDate.startsWith("Today")
                    Text(
                        text = task.dueDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isOverdue && !task.isCompleted) Rose500 else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Priority pill if HIGH/MEDIUM
                    if (task.priority == TaskPriority.HIGH || task.priority == TaskPriority.MEDIUM) {
                        val priorityColor = if (task.priority == TaskPriority.HIGH) Rose500 else Amber500
                        val priorityText = if (task.priority == TaskPriority.HIGH) "High" else "Medium"
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = priorityColor.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = priorityText,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = priorityColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Priority indicator: HIGH = Rose500 dot, MEDIUM = Amber500 dot, NORMAL = no indicator
            if (task.priority != TaskPriority.NORMAL) {
                val dotColor = if (task.priority == TaskPriority.HIGH) Rose500 else Amber500
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }
        }

        // HorizontalDivider at bottom (0.5dp, outline at 0.15f alpha)
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
            thickness = 0.5.dp
        )
    }
}
