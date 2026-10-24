package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.StudentProfile
import com.example.model.TaskItem
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.MidnightBackground
import com.example.ui.theme.MidnightBorder
import com.example.ui.theme.MidnightBorderStrong
import com.example.ui.theme.MidnightCard
import com.example.ui.theme.MidnightCyan
import com.example.ui.theme.MidnightFgMuted
import com.example.ui.theme.MidnightFgSecondary
import com.example.ui.theme.MidnightForeground
import com.example.ui.theme.MidnightLime
import com.example.ui.theme.MidnightMuted
import com.example.ui.theme.MidnightPrimary
import com.example.ui.theme.MidnightPrimaryGradient
import com.example.ui.theme.MidnightSuccess
import com.example.ui.theme.MidnightSurface2
import com.example.ui.theme.MidnightWarning
import com.example.ui.theme.SoraFontFamily
import com.example.ui.theme.TextCaption
import com.example.ui.theme.TextH1
import com.example.ui.theme.TextH2
import com.example.ui.theme.TextStat

/**
 * Top App Header matching Nocturne Learning Lounge / Midnight Focus (Ed component)
 */
@Composable
fun MidnightTopHeader(
    profile: StudentProfile,
    onNotificationsClick: () -> Unit = {},
    onStreakClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar circle: 40dp, MidnightPrimaryGradient, bold white initial in Sora font
        Box(
            modifier = Modifier
                .size(40.dp)
                .shadow(elevation = 10.dp, shape = CircleShape, spotColor = MidnightPrimary.copy(alpha = 0.45f))
                .clip(CircleShape)
                .background(MidnightPrimaryGradient),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profile.initials.ifEmpty { "A" },
                style = TextStat.copy(fontSize = 15.sp, lineHeight = 18.sp),
                color = MidnightForeground
            )
        }

        // Greeting & Tagline
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = profile.greeting,
                style = TextH2,
                color = MidnightForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = profile.tagline,
                style = TextCaption,
                color = MidnightFgMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Streak pill: bg-surface-2, flame icon + streak days in lime accent
        Row(
            modifier = Modifier
                .height(32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MidnightSurface2)
                .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
                .clickable { onStreakClick() }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak",
                tint = MidnightLime,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = "${profile.streakDays}",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MidnightLime
            )
        }

        // Notification button: 40dp square, rounded-lg, border, with cyan dot
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MidnightSurface2)
                .border(1.dp, MidnightBorder, RoundedCornerShape(10.dp))
                .clickable { onNotificationsClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = MidnightFgSecondary,
                modifier = Modifier.size(18.dp)
            )
            // Cyan notification indicator dot
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .clip(CircleShape)
                    .background(MidnightCyan)
            )
        }
    }
}

/**
 * Today's Focus Card matching Nocturne Learning Lounge (Op component)
 */
@Composable
fun TodaysFocusCard(
    studiedMinutes: Int,
    targetMinutes: Int,
    headline: String,
    onStartStudying: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progressPercent = if (targetMinutes > 0) {
        ((studiedMinutes.toFloat() / targetMinutes.toFloat()) * 100).toInt().coerceIn(0, 100)
    } else 0

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercent / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "FocusProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MidnightCard)
            .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
    ) {
        // Dual Radial Gradient glow overlay: .radial-focus & .radial-focus-soft
        Canvas(modifier = Modifier.matchParentSize()) {
            // Violet radial glow top-right at 88% -12%
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(MidnightPrimary.copy(alpha = 0.32f), Color.Transparent),
                    center = Offset(size.width * 0.88f, size.height * -0.12f),
                    radius = size.width * 0.70f
                ),
                radius = size.width * 0.70f,
                center = Offset(size.width * 0.88f, size.height * -0.12f)
            )
            // Soft cyan radial glow bottom-left at 0% 100%
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(MidnightCyan.copy(alpha = 0.10f), Color.Transparent),
                    center = Offset(0f, size.height),
                    radius = size.width * 0.60f
                ),
                radius = size.width * 0.60f,
                center = Offset(0f, size.height)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Label
            Text(
                text = "TODAY'S FOCUS",
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MidnightFgMuted,
                letterSpacing = 1.68.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Headline in Sora bold
            Text(
                text = headline,
                style = TextH1,
                color = MidnightForeground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$studiedMinutes",
                    style = TextStat.copy(fontSize = 32.sp, lineHeight = 36.sp),
                    color = MidnightForeground
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "of $targetMinutes min target",
                    style = TextCaption,
                    color = MidnightFgSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MidnightPrimary.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$progressPercent%",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MidnightPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MidnightMuted)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(MidnightPrimaryGradient)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Big CTA: Start studying (h-[52px], rounded-[16px])
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(16.dp), spotColor = MidnightPrimary.copy(alpha = 0.45f))
                    .clip(RoundedCornerShape(16.dp))
                    .background(MidnightPrimaryGradient)
                    .clickable { onStartStudying() }
                    .testTag("start_studying_btn"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MidnightForeground,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start studying",
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MidnightForeground
                )
            }
        }
    }
}

/**
 * Continue Learning Card matching Nocturne Learning Lounge (Mp component)
 */
@Composable
fun MidnightContinueLearningCard(
    course: Course,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Continue learning",
            style = TextH2,
            color = MidnightForeground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MidnightCard)
                .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Subject Icon Box
                    val iconColor = getAccentColor(course.accentName)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(iconColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getSubjectImageVector(course.category),
                            contentDescription = course.category,
                            tint = iconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = course.category,
                            style = TextCaption.copy(fontWeight = FontWeight.Medium),
                            color = MidnightFgMuted
                        )
                        Text(
                            text = course.title,
                            style = TextH2,
                            color = MidnightForeground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Next: ${course.nextLessonTitle} · ${course.durationMinutes} min",
                            style = TextCaption,
                            color = MidnightFgSecondary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress row with Resume button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Progress bar
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(MidnightMuted)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(course.progress / 100f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(3.dp))
                                .background(MidnightPrimaryGradient)
                        )
                    }

                    Text(
                        text = "${course.progress}%",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MidnightFgSecondary
                    )

                    // Resume button (action-sm: h-11, rounded-lg)
                    Row(
                        modifier = Modifier
                            .height(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MidnightSurface2)
                            .border(1.dp, MidnightBorder, RoundedCornerShape(10.dp))
                            .clickable { onResume() }
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = MidnightForeground,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Resume",
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MidnightForeground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${course.completedLessons} of ${course.totalLessons} lessons completed",
                    style = TextCaption,
                    color = MidnightFgMuted
                )
            }
        }
    }
}

/**
 * Weekly Activity Card matching Nocturne Learning Lounge (nU component)
 */
@Composable
fun WeeklyActivityCard(
    weeklyActivity: List<DailyActivity>,
    modifier: Modifier = Modifier
) {
    var selectedDayIndex by remember { mutableStateOf(5) } // Sat by default

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weekly activity",
                style = TextH2,
                color = MidnightForeground
            )
            Text(
                text = "Study minutes per day",
                style = TextCaption,
                color = MidnightFgMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MidnightCard)
                .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Interactive selected day tooltip banner
                val currentSelected = weeklyActivity.getOrNull(selectedDayIndex)
                if (currentSelected != null) {
                    val displayMinutes = if (currentSelected.minutes > 0) currentSelected.minutes else (currentSelected.hours * 60).toInt()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${currentSelected.day} Focus",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MidnightFgSecondary
                        )
                        Text(
                            text = "$displayMinutes mins",
                            fontFamily = SoraFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedDayIndex == 5) MidnightPrimary else MidnightCyan
                        )
                    }
                }

                // 7-day Bar Chart matching Recharts
                val maxMinutes = 100f
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weeklyActivity.forEachIndexed { index, day ->
                        val minutes = if (day.minutes > 0) day.minutes else (day.hours * 60).toInt()
                        val heightFraction = (minutes / maxMinutes).coerceIn(0.1f, 1f)
                        val isSaturday = (day.day == "Sat" || index == 5)
                        val isSelected = (index == selectedDayIndex)

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { selectedDayIndex = index },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            // Bar
                            Box(
                                modifier = Modifier
                                    .width(26.dp)
                                    .fillMaxHeight(heightFraction)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        if (isSaturday) MidnightPrimaryGradient
                                        else Brush.verticalGradient(
                                            listOf(MidnightCyan.copy(alpha = 0.38f), MidnightCyan.copy(alpha = 0.22f))
                                        )
                                    )
                                    .border(
                                        width = if (isSelected) 1.dp else 0.dp,
                                        color = if (isSelected) MidnightBorderStrong else Color.Transparent,
                                        shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                    )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Day Label
                            Text(
                                text = day.day,
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                fontWeight = if (isSaturday) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSaturday) MidnightForeground else MidnightFgMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Study Statistics 2x2 Grid matching Nocturne Learning Lounge (iU component)
 */
@Composable
fun StudyStatisticsGrid(
    profile: StudentProfile,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MidnightCard)
            .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Row 1: Total study time & Lessons completed
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Total study time
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.totalStudyTimeFormatted,
                        style = TextStat,
                        color = MidnightForeground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Total study time",
                        style = TextCaption,
                        color = MidnightFgMuted
                    )
                }

                // Lessons completed
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${profile.completedLessonsCount}",
                        style = TextStat,
                        color = MidnightForeground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Lessons completed",
                        style = TextCaption,
                        color = MidnightFgMuted
                    )
                }
            }

            // Row 2: Day streak & Weekly goal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Day streak
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${profile.streakDays}",
                        style = TextStat,
                        color = MidnightForeground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Day streak",
                        style = TextCaption,
                        color = MidnightFgMuted
                    )
                }

                // Weekly goal with mini progress bar
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${profile.weeklyGoalPercent}%",
                        style = TextStat,
                        color = MidnightForeground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Weekly goal",
                        style = TextCaption,
                        color = MidnightFgMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MidnightMuted)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(profile.weeklyGoalPercent / 100f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(2.dp))
                                .background(MidnightPrimaryGradient)
                        )
                    }
                }
            }
        }
    }
}

/**
 * My Courses Carousel matching Nocturne Learning Lounge (sU component)
 */
@Composable
fun MyCoursesCarousel(
    courses: List<Course>,
    onSeeAll: () -> Unit,
    onCourseClick: (Course) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My courses",
                style = TextH2,
                color = MidnightForeground
            )
            Text(
                text = "See all",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MidnightPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onSeeAll() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp)
        ) {
            items(courses, key = { it.id }) { course ->
                val accentColor = getAccentColor(course.accentName)

                Box(
                    modifier = Modifier
                        .width(152.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MidnightCard)
                        .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
                        .clickable { onCourseClick(course) }
                        .padding(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Subject icon box
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(accentColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getSubjectImageVector(course.category),
                                contentDescription = course.category,
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = course.category,
                            style = TextCaption.copy(fontWeight = FontWeight.Medium),
                            color = MidnightFgMuted
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = course.title,
                            fontFamily = SoraFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MidnightForeground,
                            lineHeight = 21.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.height(42.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MidnightMuted)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(course.progress / 100f)
                                        .fillMaxHeight()
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(accentColor)
                                )
                            }
                            Text(
                                text = "${course.progress}%",
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MidnightFgSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Today's Plan Checklist matching Nocturne Learning Lounge (yW component)
 */
@Composable
fun TodaysPlanSection(
    tasks: List<TaskItem>,
    onToggleTask: (String) -> Unit,
    onSeeFullPlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = tasks.count { it.isCompleted }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's plan",
                style = TextH2,
                color = MidnightForeground
            )
            Text(
                text = "$completedCount/${tasks.size} done",
                style = TextCaption,
                color = MidnightFgMuted
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card with divided tasks
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MidnightCard)
                .border(1.dp, MidnightBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                tasks.forEachIndexed { index, task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleTask(task.id) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Custom Checkbox (size 20dp, rounded-md 6dp)
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (task.isCompleted) MidnightSuccess else Color.Transparent)
                                .border(
                                    1.5.dp,
                                    if (task.isCompleted) MidnightSuccess else MidnightFgMuted.copy(alpha = 0.4f),
                                    RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (task.isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MidnightBackground,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        // Title & Course
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (task.isCompleted) MidnightFgMuted else MidnightForeground,
                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = task.courseName.ifEmpty { task.subjectCode },
                                style = TextCaption,
                                color = MidnightFgMuted
                            )
                        }

                        // Window time pill
                        val windowText = task.windowTime.ifEmpty { task.dueDate }
                        if (windowText.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MidnightSurface2)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = windowText,
                                    style = TextCaption.copy(fontWeight = FontWeight.Medium),
                                    color = MidnightFgSecondary
                                )
                            }
                        }
                    }

                    if (index < tasks.size - 1) {
                        HorizontalDivider(color = MidnightBorder, thickness = 1.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // See full plan button (action-sm elevated)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MidnightSurface2)
                .border(1.dp, MidnightBorder, RoundedCornerShape(10.dp))
                .clickable { onSeeFullPlan() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "See full plan",
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MidnightForeground
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MidnightForeground,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Study Tools Grid matching Nocturne Learning Lounge (xW component)
 */
@Composable
fun StudyToolsGrid(
    onNotesClick: () -> Unit,
    onFlashcardsClick: () -> Unit,
    onQuizClick: () -> Unit,
    onTimerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Study tools",
            style = TextH2,
            color = MidnightForeground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudyToolCard(
                    title = "Notes",
                    icon = Icons.Outlined.Notes,
                    onClick = onNotesClick,
                    modifier = Modifier.weight(1f)
                )
                StudyToolCard(
                    title = "Flashcards",
                    icon = Icons.Outlined.Layers,
                    onClick = onFlashcardsClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StudyToolCard(
                    title = "Quiz",
                    icon = Icons.Outlined.ListAlt,
                    onClick = onQuizClick,
                    modifier = Modifier.weight(1f)
                )
                StudyToolCard(
                    title = "Focus Timer",
                    icon = Icons.Outlined.Timer,
                    onClick = onTimerClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StudyToolCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(84.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MidnightSurface2)
            .border(1.dp, MidnightBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MidnightFgSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MidnightFgSecondary
            )
        }
    }
}

/**
 * Helpers for accents and subject icons
 */
fun getAccentColor(accent: String): Color {
    return when (accent.lowercase()) {
        "violet", "primary" -> MidnightPrimary
        "cyan" -> MidnightCyan
        "lime" -> MidnightLime
        "warning", "amber" -> MidnightWarning
        "success", "mint" -> MidnightSuccess
        else -> MidnightPrimary
    }
}

fun getSubjectImageVector(category: String): ImageVector {
    return when (category.lowercase()) {
        "chemistry" -> Icons.Default.Science
        "mathematics", "math" -> Icons.Default.Functions
        "languages", "spanish" -> Icons.Default.Translate
        "computer science", "cs" -> Icons.Outlined.Code
        "humanities", "history" -> Icons.Outlined.AccountBalance
        else -> Icons.Outlined.MenuBook
    }
}
