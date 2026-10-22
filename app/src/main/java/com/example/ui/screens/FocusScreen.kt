package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FocusSession
import com.example.ui.theme.Mint500
import com.example.ui.theme.Sky500
import com.example.viewmodel.FocusTimerViewModel
import com.example.viewmodel.PomodoroPhase
import com.example.viewmodel.TimerMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    viewModel: FocusTimerViewModel,
    recentSessions: List<FocusSession>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val timerMode by viewModel.timerMode.collectAsState()
    val pomodoroPhase by viewModel.pomodoroPhase.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val stopwatchSeconds by viewModel.stopwatchSeconds.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val completedCycles by viewModel.completedCycles.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val ambientSoundEnabled by viewModel.ambientSoundEnabled.collectAsState()

    var showSubjectDropdown by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    // Calculate progress ratio
    val progressRatio: Float = if (timerMode == TimerMode.POMODORO) {
        val totalSec = pomodoroPhase.defaultDurationSec.toFloat()
        (remainingSeconds.toFloat() / totalSec).coerceIn(0f, 1f)
    } else {
        // Stopwatch progress wraps every 60s
        (stopwatchSeconds % 60) / 60f
    }

    val animatedProgress by animateFloatAsState(targetValue = progressRatio, label = "timer_progress")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("focus_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        // Top App Bar
        item {
            TopAppBar(
                title = {
                    Text(
                        text = "Distraction-Free Focus",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("focus_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleAmbientSound() }) {
                        Icon(
                            imageVector = if (ambientSoundEnabled) Icons.Default.VolumeUp else Icons.Default.Headphones,
                            contentDescription = "Ambient sound toggle",
                            tint = if (ambientSoundEnabled) Mint500 else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }

        // Mode Segment Switcher: [ Pomodoro ] vs [ Stopwatch ]
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .testTag("timer_mode_selector")
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (timerMode == TimerMode.POMODORO) primaryColor else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.switchMode(TimerMode.POMODORO) }
                    ) {
                        Text(
                            text = "Pomodoro (25m)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (timerMode == TimerMode.POMODORO) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (timerMode == TimerMode.STOPWATCH) primaryColor else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.switchMode(TimerMode.STOPWATCH) }
                    ) {
                        Text(
                            text = "Stopwatch",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (timerMode == TimerMode.STOPWATCH) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Pomodoro Phase pills (if Pomodoro mode)
        if (timerMode == TimerMode.POMODORO) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    PomodoroPhase.entries.forEach { phase ->
                        val isCurrent = pomodoroPhase == phase
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) primaryColor.copy(alpha = 0.15f) else Color.Transparent
                        ) {
                            Text(
                                text = phase.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) primaryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Subject Selector Dropdown
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { showSubjectDropdown = true }
                        .testTag("focus_subject_selector")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(primaryColor)
                        )
                        Text(
                            text = selectedSubject,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                DropdownMenu(
                    expanded = showSubjectDropdown,
                    onDismissRequest = { showSubjectDropdown = false }
                ) {
                    viewModel.availableSubjects.forEach { subj ->
                        DropdownMenuItem(
                            text = { Text(subj, style = MaterialTheme.typography.bodySmall) },
                            onClick = {
                                viewModel.setSubject(subj)
                                showSubjectDropdown = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        // Circular Timer Canvas
        item {
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .testTag("circular_timer_container"),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()
                    val diameter = size.minDimension - strokeWidth
                    val radius = diameter / 2f

                    // Background track
                    drawCircle(
                        color = trackColor,
                        radius = radius,
                        style = Stroke(width = strokeWidth)
                    )

                    // Active progress sweep
                    val sweepAngle = 360f * animatedProgress
                    drawArc(
                        color = if (pomodoroPhase == PomodoroPhase.FOCUS) primaryColor else Mint500,
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Inner Time Display
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val timeString = if (timerMode == TimerMode.POMODORO) {
                        val minutes = remainingSeconds / 60
                        val seconds = remainingSeconds % 60
                        "%02d:%02d".format(minutes, seconds)
                    } else {
                        val hours = stopwatchSeconds / 3600
                        val minutes = (stopwatchSeconds % 3600) / 60
                        val seconds = stopwatchSeconds % 60
                        if (hours > 0) "%02d:%02d:%02d".format(hours, minutes, seconds)
                        else "%02d:%02d".format(minutes, seconds)
                    }

                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 46.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (timerMode == TimerMode.POMODORO) {
                            if (isRunning) "Deep study in progress" else "Paused"
                        } else {
                            if (isRunning) "Stopwatch running" else "Timer paused"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (timerMode == TimerMode.POMODORO) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cycle $completedCycles / 4",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Control Buttons Row: Reset | Play/Pause | Skip
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                OutlinedIconButton(
                    onClick = { viewModel.reset() },
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("timer_reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset timer",
                        modifier = Modifier.size(24.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { viewModel.togglePlayPause() },
                    containerColor = primaryColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp),
                    modifier = Modifier
                        .size(72.dp)
                        .testTag("timer_play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Play",
                        modifier = Modifier.size(34.dp)
                    )
                }

                if (timerMode == TimerMode.POMODORO) {
                    OutlinedIconButton(
                        onClick = { viewModel.skipInterval() },
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("timer_skip_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Skip interval",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(52.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Ambient sound status
        if (ambientSoundEnabled) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Mint500.copy(alpha = 0.12f),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Headphones, contentDescription = null, tint = Mint500, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Binaural alpha waves (40Hz) focus stream enabled",
                            style = MaterialTheme.typography.labelSmall,
                            color = Mint500,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Completed Focus Sessions Log
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Recent Focus Sessions Today",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(recentSessions) { session ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.subject,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${session.mode} • ${session.durationMinutes} minutes logged",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Mint500.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "+${session.durationMinutes}m",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Mint500,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 0.5.dp
            )
        }
    }
}
