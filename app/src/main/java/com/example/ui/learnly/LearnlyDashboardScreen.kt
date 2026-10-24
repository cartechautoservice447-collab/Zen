package com.example.ui.learnly

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnlyDashboard() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var selectedDestination by remember { mutableStateOf(Destination.Home) }
    var activeSheet by remember { mutableStateOf<DashboardSheet?>(null) }
    var tasks by remember { mutableStateOf(taskSeed) }
    var period by remember { mutableStateOf("7D") }
    var timerSeconds by remember { mutableIntStateOf(25 * 60) }
    var timerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(timerRunning, timerSeconds) {
        if (timerRunning && timerSeconds > 0) {
            delay(1_000)
            timerSeconds -= 1
        } else if (timerSeconds <= 0) {
            timerRunning = false
        }
    }

    val openTimer = {
        activeSheet = DashboardSheet.FocusTimer
    }
    val openCourse: (CourseItem) -> Unit = { activeSheet = DashboardSheet.CourseDetails(it) }
    val onNavigate: (Destination) -> Unit = { destination ->
        selectedDestination = destination
        when (destination) {
            Destination.Home -> scope.launch { scrollState.animateScrollTo(0) }
            Destination.Learn -> scope.launch { scrollState.animateScrollTo(1400) }
            Destination.Plan -> scope.launch { scrollState.animateScrollTo(1800) }
            Destination.More -> activeSheet = DashboardSheet.More
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            HeaderAndFocus(
                onNotifications = { activeSheet = DashboardSheet.Notifications },
                onStartStudying = openTimer
            )
            ContinueLearningSection(onResume = { openCourse(courseSeed.first()) })
            LearningRhythmSection(
                period = period,
                onPeriodChanged = { period = it },
                onDetails = { activeSheet = DashboardSheet.Analytics }
            )
            QuickToolsSection(
                onToolClick = { tool ->
                    activeSheet = when (tool) {
                        ToolKind.Notes -> DashboardSheet.Notes
                        ToolKind.Flashcards -> DashboardSheet.Flashcards
                        ToolKind.Quiz -> DashboardSheet.Quiz
                        ToolKind.FocusTimer -> DashboardSheet.FocusTimer
                    }
                },
                onStartFocus = openTimer
            )
            AchievementsSection()
            CoursesSection(
                courses = courseSeed,
                onSeeAll = { activeSheet = DashboardSheet.AllCourses },
                onCourseClick = openCourse
            )
            TodayPlanSection(
                tasks = tasks,
                onToggleTask = { id ->
                    tasks = tasks.map { task -> if (task.id == id) task.copy(done = !task.done) else task }
                },
                onSeeFullPlan = { activeSheet = DashboardSheet.FullPlan }
            )
            Spacer(Modifier.height(116.dp))
        }

        BottomNavigationBar(
            selected = selectedDestination,
            onSelect = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    val sheet = activeSheet
    if (sheet != null) {
        ModalBottomSheet(
            onDismissRequest = {
                activeSheet = null
                if (timerSeconds <= 0) timerSeconds = 25 * 60
            },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White,
            contentColor = Ink,
            dragHandle = {
                Box(
                    Modifier
                        .padding(top = 10.dp, bottom = 6.dp)
                        .size(width = 38.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD8D7E0))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
            ) {
                when (sheet) {
                    DashboardSheet.FocusTimer -> FocusTimerSheet(
                        seconds = timerSeconds,
                        running = timerRunning,
                        onToggle = { timerRunning = !timerRunning },
                        onReset = { timerRunning = false; timerSeconds = 25 * 60 }
                    )
                    DashboardSheet.Notes -> NotesSheet()
                    DashboardSheet.Flashcards -> FlashcardsSheet()
                    DashboardSheet.Quiz -> QuizSheet()
                    DashboardSheet.Notifications -> NotificationsSheet()
                    DashboardSheet.Analytics -> AnalyticsSheet()
                    DashboardSheet.AllCourses -> AllCoursesSheet(onCourseClick = { course -> activeSheet = DashboardSheet.CourseDetails(course) })
                    DashboardSheet.FullPlan -> FullPlanSheet(tasks = tasks)
                    DashboardSheet.More -> MoreSheet(
                        onChoose = { destination ->
                            activeSheet = null
                            onNavigate(destination)
                        },
                        onOpenNotes = { activeSheet = DashboardSheet.Notes },
                        onOpenFlashcards = { activeSheet = DashboardSheet.Flashcards },
                        onOpenQuiz = { activeSheet = DashboardSheet.Quiz },
                        onOpenTimer = { activeSheet = DashboardSheet.FocusTimer }
                    )
                    is DashboardSheet.CourseDetails -> CourseDetailSheet(
                        course = sheet.course,
                        onResume = { activeSheet = DashboardSheet.FocusTimer }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderAndFocus(onNotifications: () -> Unit, onStartStudying: () -> Unit) {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Night)
            .padding(top = statusBarHeight + 10.dp, start = 24.dp, end = 24.dp, bottom = 28.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(PurpleToVioletLinear, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("A", fontSize = 21.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Good evening", color = Color(0xFFF7F8FF), fontSize = 21.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, lineHeight = 24.sp)
                Text("Ready for a focused session?", color = Muted, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp)
            }
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(NightCardAlt)
                    .height(40.dp)
                    .padding(horizontal = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(Icons.Filled.LocalFireDepartment, null, tint = Lime, modifier = Modifier.size(22.dp))
                Text("12", color = Lime, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Box {
                IconButton(
                    onClick = onNotifications,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(NightCardAlt)
                        .border(1.dp, NightBorder, RoundedCornerShape(20.dp))
                ) {
                    Icon(Icons.Filled.NotificationsNone, "Notifications", tint = Color(0xFF9AA4BC), modifier = Modifier.size(25.dp))
                }
                Box(Modifier.align(Alignment.TopEnd).padding(top = 6.dp, end = 5.dp).size(8.dp).clip(CircleShape).background(Cyan))
            }
        }
        Spacer(Modifier.height(28.dp))
        TodayFocusCard(onStartStudying)
    }
}

@Composable
fun TodayFocusCard(onStartStudying: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(FocusCardGradient)
            .border(1.dp, NightBorder, RoundedCornerShape(30.dp))
            .padding(24.dp)
    ) {
        Text(
            "TODAY'S FOCUS",
            color = Muted,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.4.sp
        )
        Spacer(Modifier.height(20.dp))
        Text(
            "Your next\nbreakthrough\nstarts here.",
            color = Color(0xFFF8F8FF),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 37.sp,
            letterSpacing = (-0.8).sp
        )
        Spacer(Modifier.height(26.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("75", color = Color(0xFFF8F8FF), fontSize = 42.sp, lineHeight = 44.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1.5).sp)
            Spacer(Modifier.width(9.dp))
            Text("of 120 min target", color = Color(0xFFA3ABC1), fontSize = 14.sp, maxLines = 1)
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF242653))
                    .padding(horizontal = 15.dp, vertical = 9.dp)
            ) {
                Text("63%", color = Purple, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.height(20.dp))
        ProgressTrack(progress = 0.625f, height = 9.dp, trackColor = Color(0xFF20283C), fill = PurpleToVioletGradient)
        Spacer(Modifier.height(24.dp))
        GradientButton(
            text = "Start studying",
            icon = { Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(27.dp)) },
            onClick = onStartStudying,
            height = 66.dp,
            textSize = 20.sp,
            radius = 24.dp
        )
    }
}

@Composable
fun ContinueLearningSection(onResume: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Canvas).padding(top = 26.dp, start = 24.dp, end = 24.dp)) {
        SectionHeading("Continue learning")
        Spacer(Modifier.height(14.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(ContinueCardGradient)
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.20f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(34.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text("Physics · Lesson 15", color = Color.White.copy(alpha = 0.78f), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(3.dp))
                    Text("Physics: Motion & Forces", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, lineHeight = 25.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            Spacer(Modifier.height(22.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.17f))
                    .padding(20.dp)
            ) {
                Text("Up next", color = Color.White.copy(alpha = 0.84f), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(7.dp))
                Text("Newton's Third Law in action", color = Color.White, fontSize = 23.sp, lineHeight = 29.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(18.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("14/22 lessons", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.weight(1f))
                    Text("64%", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                ProgressTrack(progress = 14f / 22f, height = 10.dp, trackColor = Color.White.copy(alpha = 0.25f), fill = Brush.horizontalGradient(listOf(Color.White, Color.White)))
            }
            Spacer(Modifier.height(22.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AccessTime, null, tint = Color.White.copy(alpha = 0.92f), modifier = Modifier.size(25.dp))
                Spacer(Modifier.width(9.dp))
                Text("18 min", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.weight(1f))
                Surface(
                    onClick = onResume,
                    color = Color.White,
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.height(60.dp)
                ) {
                    Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.PlayArrow, null, tint = Color(0xFF6757DC), modifier = Modifier.size(25.dp))
                        Text("Resume", color = Color(0xFF6757DC), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun LearningRhythmSection(period: String, onPeriodChanged: (String) -> Unit, onDetails: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(top = 30.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionHeading("Your learning rhythm", modifier = Modifier.weight(1f))
            TextButton(onClick = onDetails, contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp)) {
                Text("Details", color = Purple, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(2.dp))
                Icon(Icons.Filled.ChevronRight, null, tint = Purple, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFF0EFF5), RoundedCornerShape(30.dp))
                .padding(22.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text("Study time", color = Color(0xFF8A8B9A), fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(5.dp))
                    Text(if (period == "7D") "5h 54m" else if (period == "30D") "24h 18m" else "86h 25m", color = Ink, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, lineHeight = 40.sp, letterSpacing = (-1.2).sp)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Filled.TrendingUp, null, tint = Color(0xFF4BAF8B), modifier = Modifier.size(16.dp))
                        Text("18% vs last week", color = Color(0xFF4BAF8B), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                PeriodSelector(period = period, onPeriodChanged = onPeriodChanged)
            }
            Spacer(Modifier.height(24.dp))
            RhythmChart(period)
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFF7F5FF))
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Box(Modifier.size(38.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.AutoAwesome, null, tint = Purple, modifier = Modifier.size(21.dp))
                }
                Text(
                    "You're most consistent on Wednesdays. Keep that momentum.",
                    color = Color(0xFF707184),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun PeriodSelector(period: String, onPeriodChanged: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFFF3F2F7))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        listOf("7D", "30D", "90D").forEach { value ->
            val selected = period == value
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (selected) Color.White else Color.Transparent)
                    .clickable { onPeriodChanged(value) }
                    .padding(horizontal = 9.dp, vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(value, color = if (selected) Purple else Color(0xFF8C8D9C), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RhythmChart(period: String) {
    val seven = listOf(0.36f, 0.59f, 0.25f, 0.78f, 0.55f, 0.96f, 0.68f)
    val thirty = listOf(0.46f, 0.66f, 0.32f, 0.71f, 0.62f, 0.88f, 0.74f)
    val ninety = listOf(0.61f, 0.53f, 0.43f, 0.85f, 0.57f, 0.94f, 0.70f)
    val bars = when (period) { "30D" -> thirty; "90D" -> ninety; else -> seven }
    val labels = listOf("F", "S", "S", "M", "T", "W", "T")
    Row(
        modifier = Modifier.fillMaxWidth().height(156.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        bars.forEachIndexed { index, targetVal ->
            val animatedHeight by animateFloatAsState(
                targetValue = targetVal,
                animationSpec = tween(durationMillis = 260, easing = FastOutSlowInEasing),
                label = "barHeight_$index"
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(Modifier.height(120.dp).width(26.dp), contentAlignment = Alignment.BottomCenter) {
                    Box(
                        Modifier
                            .width(23.dp)
                            .fillMaxHeight(animatedHeight)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (index == 5) ActiveBarGradient else InactiveBarGradient)
                    )
                }
                Spacer(Modifier.height(9.dp))
                Text(labels[index], color = if (index == 5) Purple else Color(0xFF8E8E9D), fontSize = 13.sp, fontWeight = if (index == 5) FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}

@Composable
fun QuickToolsSection(onToolClick: (ToolKind) -> Unit, onStartFocus: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(top = 30.dp, start = 24.dp, end = 24.dp)) {
        SectionHeading("Quick tools")
        Spacer(Modifier.height(18.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ToolTile("Notes", SoftPurple, Purple, Icons.Filled.Edit, Modifier.weight(1f)) { onToolClick(ToolKind.Notes) }
            ToolTile("Flashcards", SoftCoral, Coral, Icons.Filled.Layers, Modifier.weight(1f)) { onToolClick(ToolKind.Flashcards) }
            ToolTile("Quiz", SoftYellow, Gold, Icons.Filled.HelpOutline, Modifier.weight(1f)) { onToolClick(ToolKind.Quiz) }
            ToolTile("Focus\nTimer", SoftMint, Mint, Icons.Filled.Timer, Modifier.weight(1f)) { onToolClick(ToolKind.FocusTimer) }
        }
        Spacer(Modifier.height(28.dp))
        GradientButton(
            text = "Start a focus session",
            icon = { Icon(Icons.Filled.Timer, null, tint = Color.White, modifier = Modifier.size(29.dp)) },
            onClick = onStartFocus,
            height = 68.dp,
            textSize = 20.sp,
            radius = 28.dp
        )
    }
}

@Composable
fun ToolTile(title: String, tileColor: Color, iconColor: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(tileColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(33.dp))
        }
        Spacer(Modifier.height(11.dp))
        Text(title, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center, lineHeight = 18.sp, minLines = 2, maxLines = 2)
    }
}

@Composable
fun AchievementsSection() {
    Column(Modifier.fillMaxWidth().padding(top = 30.dp, start = 24.dp, end = 24.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AchievementCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.LocalFireDepartment,
                tileColor = SoftCoral,
                iconColor = Coral,
                title = "12 days",
                subtitle = "Best: 21 days",
                kind = "streak"
            )
            AchievementCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.EmojiEvents,
                tileColor = SoftYellow,
                iconColor = Gold,
                title = "Quiz Ace",
                subtitle = "Score 90%+ on 5 quizzes",
                kind = "quiz"
            )
        }
    }
}

@Composable
fun AchievementCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tileColor: Color,
    iconColor: Color,
    title: String,
    subtitle: String,
    kind: String
) {
    Column(
        modifier
            .heightIn(min = 224.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF0EFF5), RoundedCornerShape(30.dp))
            .padding(18.dp)
    ) {
        Box(Modifier.size(54.dp).clip(RoundedCornerShape(22.dp)).background(tileColor), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(27.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, color = Ink, fontSize = if (kind == "streak") 32.sp else 22.sp, lineHeight = 36.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(5.dp))
        Text(subtitle, color = Color(0xFF77798A), fontSize = 14.sp, lineHeight = 19.sp, fontWeight = FontWeight.Medium, minLines = 2)
        Spacer(Modifier.weight(1f))
        if (kind == "streak") {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                repeat(7) { index ->
                    Box(Modifier.weight(1f).height(8.dp).clip(CircleShape).background(if (index < 5) Coral else Color(0xFFF0EFF5)))
                }
            }
        } else {
            ProgressTrack(0.8f, 8.dp, Color(0xFFF0EFF5), GoldProgressGradient)
            Spacer(Modifier.height(7.dp))
            Text("4/5", color = Color(0xFF77798A), fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CoursesSection(courses: List<CourseItem>, onSeeAll: () -> Unit, onCourseClick: (CourseItem) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(top = 32.dp)) {
        Row(Modifier.padding(horizontal = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            SectionHeading("Your courses", modifier = Modifier.weight(1f))
            TextButton(onClick = onSeeAll, contentPadding = PaddingValues(0.dp)) {
                Text("See all", color = Purple, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            courses.forEach { course ->
                CourseCard(course = course, modifier = Modifier.width(172.dp), onClick = { onCourseClick(course) })
            }
        }
    }
}

@Composable
fun CourseCard(course: CourseItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .heightIn(min = 194.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(NightCard)
            .border(1.dp, NightBorder, RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(19.dp)).background(course.accent.copy(alpha = 0.17f)), contentAlignment = Alignment.Center) {
                when (course.icon) {
                    CourseIcon.Physics -> Icon(Icons.Filled.AutoAwesome, null, tint = course.accent, modifier = Modifier.size(24.dp))
                    CourseIcon.Math -> Icon(Icons.Filled.Functions, null, tint = course.accent, modifier = Modifier.size(24.dp))
                    CourseIcon.Chemistry -> Icon(Icons.Filled.Science, null, tint = course.accent, modifier = Modifier.size(24.dp))
                    CourseIcon.Code -> Icon(Icons.Filled.School, null, tint = course.accent, modifier = Modifier.size(24.dp))
                    CourseIcon.Book -> Icon(Icons.Filled.MenuBook, null, tint = course.accent, modifier = Modifier.size(24.dp))
                }
            }
            Text(course.category, color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        Spacer(Modifier.height(13.dp))
        Text(course.title, color = Color(0xFFF4F5FC), fontSize = 17.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp, minLines = 2, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            Box(Modifier.weight(1f)) {
                ProgressTrack(course.progress, 7.dp, Color(0xFF20283C), Brush.horizontalGradient(listOf(course.accent, if (course.accent == Purple) Violet else course.accent)))
            }
            Text("${course.percent}%", color = Color(0xFFA2AAC0), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(2.dp))
    }
}

@Composable
fun TodayPlanSection(tasks: List<StudyTask>, onToggleTask: (Int) -> Unit, onSeeFullPlan: () -> Unit) {
    val completeCount = tasks.count { it.done }
    Column(Modifier.fillMaxWidth().padding(top = 32.dp, start = 24.dp, end = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SectionHeading("Today's plan", modifier = Modifier.weight(1f))
            Text("$completeCount/${tasks.size} done", color = MutedDark, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(Modifier.height(12.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(NightCard)
                .border(1.dp, NightBorder, RoundedCornerShape(30.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            tasks.forEachIndexed { index, task ->
                TaskRow(task = task, onToggle = { onToggleTask(task.id) })
                if (index < tasks.lastIndex) {
                    Spacer(Modifier.height(9.dp))
                    Divider(color = Color(0xFF232B40).copy(alpha = 0.75f), thickness = 1.dp, modifier = Modifier.padding(start = 38.dp))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onSeeFullPlan, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("See full plan", color = Purple, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(3.dp))
            Icon(Icons.Filled.ChevronRight, null, tint = Purple, modifier = Modifier.size(19.dp))
        }
    }
}

@Composable
fun TaskRow(task: StudyTask, onToggle: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 1.dp)
                .size(26.dp)
                .clip(CircleShape)
                .background(if (task.done) Color(0xFF49D6AA) else Color.Transparent)
                .border(if (task.done) 0.dp else 1.5.dp, if (task.done) Color.Transparent else Color(0xFF434B60), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (task.done) Icon(Icons.Filled.Check, null, tint = Night, modifier = Modifier.size(19.dp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                task.title,
                color = if (task.done) Color(0xFF777F97) else Color(0xFFF1F3FA),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textDecoration = if (task.done) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
            )
            Text(task.course, color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.widthIn(max = 106.dp)) {
            Box(Modifier.clip(CircleShape).background(NightCardAlt).padding(horizontal = 10.dp, vertical = 8.dp)) {
                Text("${task.time} · ${task.duration}", color = Color(0xFFAAB2C7), fontSize = 11.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selected: Destination, onSelect: (Destination) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(42.dp))
            .background(Lavender)
            .border(1.dp, Color(0xFFD3C9FF), RoundedCornerShape(42.dp))
            .padding(horizontal = 7.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(Destination.Home, selected, "Home", Icons.Filled.Home, Modifier.weight(1f), onSelect)
        NavigationItem(Destination.Learn, selected, "Learn", Icons.Filled.MenuBook, Modifier.weight(1f), onSelect)
        NavigationItem(Destination.Plan, selected, "Plan", Icons.Filled.CalendarMonth, Modifier.weight(1f), onSelect)
        NavigationItem(Destination.More, selected, "More", Icons.Filled.GridView, Modifier.weight(1f), onSelect)
    }
}

@Composable
fun NavigationItem(destination: Destination, selected: Destination, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onSelect: (Destination) -> Unit) {
    val active = destination == selected
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(28.dp))
            .clickable { onSelect(destination) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(if (active) 54.dp else 48.dp)
                .height(35.dp)
                .clip(CircleShape)
                .background(if (active) Purple else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = if (active) Color.White else Color(0xFF77798D), modifier = Modifier.size(23.dp))
        }
        Spacer(Modifier.height(1.dp))
        Text(label, color = if (active) Ink else Color(0xFF77798D), fontSize = 11.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Medium)
    }
}

@Composable
fun SectionHeading(title: String, modifier: Modifier = Modifier) {
    Text(title, modifier = modifier, color = Ink, fontSize = 22.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.35).sp)
}

@Composable
fun ProgressTrack(progress: Float, height: Dp, trackColor: Color, fill: Brush) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(trackColor)
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(CircleShape)
                .background(fill)
        )
    }
}

@Composable
fun GradientButton(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    height: Dp,
    textSize: TextUnit,
    radius: Dp
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(radius),
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Row(
            modifier = Modifier
                .background(PurpleToVioletGradient)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(Modifier.width(10.dp))
            Text(text, color = Color.White, fontSize = textSize, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844, name = "Learnly dashboard · mobile")
@Composable
fun LearnlyDashboardPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Purple,
            secondary = Violet,
            background = Canvas,
            surface = Color.White,
            onPrimary = Color.White,
            onBackground = Ink,
            onSurface = Ink
        )
    ) {
        LearnlyDashboard()
    }
}
