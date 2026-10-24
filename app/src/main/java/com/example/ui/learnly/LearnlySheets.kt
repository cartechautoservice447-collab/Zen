package com.example.ui.learnly

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SheetTitle(title: String, subtitle: String? = null) {
    Text(title, color = Ink, fontSize = 25.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold)
    if (subtitle != null) {
        Spacer(Modifier.height(5.dp))
        Text(subtitle, color = MutedDark, fontSize = 15.sp, lineHeight = 21.sp)
    }
    Spacer(Modifier.height(20.dp))
}

@Composable
fun FocusTimerSheet(seconds: Int, running: Boolean, onToggle: () -> Unit, onReset: () -> Unit) {
    SheetTitle("Focus session", "A calm 25-minute session. One thing at a time.")
    Box(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { seconds / (25f * 60f) },
            modifier = Modifier.size(190.dp),
            color = Purple,
            trackColor = Color(0xFFECE9FA),
            strokeWidth = 10.dp
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("FOCUS TIME", color = MutedDark, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
            Text(String.format("%02d:%02d", seconds / 60, seconds % 60), color = Ink, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1.2).sp)
            Text(if (seconds == 0) "Session complete" else if (running) "Stay in the flow" else "Ready when you are", color = MutedDark, fontSize = 13.sp)
        }
    }
    Spacer(Modifier.height(18.dp))
    GradientButton(
        text = if (running) "Pause session" else if (seconds == 0) "Start again" else "Start session",
        icon = { Icon(if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(24.dp)) },
        onClick = { if (seconds == 0) onReset() else onToggle() },
        height = 58.dp,
        textSize = 17.sp,
        radius = 20.dp
    )
    TextButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Filled.Refresh, null, tint = Purple, modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(6.dp))
        Text("Reset to 25 minutes", color = Purple, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun NotesSheet() {
    var note by remember { mutableStateOf("Newton's Third Law\nFor every action, there is an equal and opposite reaction.") }
    SheetTitle("Quick notes", "A local note draft for this prototype.")
    OutlinedTextField(
        value = note,
        onValueChange = { note = it },
        modifier = Modifier.fillMaxWidth().height(180.dp),
        shape = RoundedCornerShape(18.dp),
        label = { Text("Your note") },
        placeholder = { Text("Write a thought to remember…") }
    )
    Spacer(Modifier.height(12.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Filled.Edit, null, tint = Purple, modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(6.dp))
        Text("Changes stay in this prototype session.", color = MutedDark, fontSize = 13.sp)
    }
}

@Composable
fun FlashcardsSheet() {
    val cards = remember {
        listOf(
            "What is Newton's Third Law?" to "Every action has an equal and opposite reaction.",
            "What is the SI unit of force?" to "The newton (N).",
            "What does acceleration measure?" to "The rate at which velocity changes over time."
        )
    }
    var index by remember { mutableIntStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    SheetTitle("Flashcards", "Physics · Motion & Forces")
    Card(
        onClick = { revealed = !revealed },
        modifier = Modifier.fillMaxWidth().height(220.dp),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = SoftPurple)
    ) {
        Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Text("CARD ${index + 1} OF ${cards.size}", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.3.sp)
            Text(if (revealed) cards[index].second else cards[index].first, color = Ink, fontSize = 22.sp, lineHeight = 29.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Text(if (revealed) "Tap to see question" else "Tap to reveal answer", color = MutedDark, fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    Spacer(Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedActionButton("× Still learning", Modifier.weight(1f)) {
            revealed = false
            index = (index + 1) % cards.size
        }
        GradientButton("✓ Got it", { }, { revealed = false; index = (index + 1) % cards.size }, 52.dp, 15.sp, 18.dp)
    }
}

@Composable
fun QuizSheet() {
    var selected by remember { mutableStateOf<Int?>(null) }
    val options = listOf("Equal and opposite", "Always in the same direction", "Only applies at rest")
    SheetTitle("Quick quiz", "Question 1 of 5 · Physics")
    Text("Newton's Third Law says that action and reaction forces are…", color = Ink, fontSize = 19.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(18.dp))
    options.forEachIndexed { index, answer ->
        val correct = index == 0
        val background = when {
            selected == index && correct -> SoftMint
            selected == index -> SoftCoral
            else -> Color(0xFFF7F6FB)
        }
        Row(
            Modifier.fillMaxWidth().padding(bottom = 10.dp).clip(RoundedCornerShape(16.dp)).background(background).clickable { selected = index }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(27.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                Text(('A' + index).toString(), color = if (selected == index && correct) Mint else MutedDark, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Text(answer, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            if (selected == index) Icon(if (correct) Icons.Filled.CheckCircle else Icons.Filled.Close, null, tint = if (correct) Mint else Coral, modifier = Modifier.size(20.dp))
        }
    }
    if (selected != null) {
        Text(if (selected == 0) "Correct — the forces are equal in magnitude and opposite in direction." else "Not quite. Think about the direction of the reaction force.", color = if (selected == 0) Mint else Coral, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun NotificationsSheet() {
    SheetTitle("Notifications", "You're all caught up on your learning updates.")
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(SoftPurple).padding(16.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(42.dp).clip(RoundedCornerShape(15.dp)).background(Color.White), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.LocalFireDepartment, null, tint = Coral, modifier = Modifier.size(23.dp))
        }
        Column(Modifier.weight(1f)) {
            Text("12-day streak", color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("A small session today keeps your momentum going.", color = MutedDark, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
    Spacer(Modifier.height(12.dp))
    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color(0xFFF7F6FB)).padding(16.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(42.dp).clip(RoundedCornerShape(15.dp)).background(SoftMint), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.CheckCircle, null, tint = Mint, modifier = Modifier.size(23.dp))
        }
        Column(Modifier.weight(1f)) {
            Text("Weekly progress is up 18%", color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("You've spent 5 hours and 54 minutes learning this week.", color = MutedDark, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
fun AnalyticsSheet() {
    SheetTitle("Study insights", "Your consistency is building week over week.")
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        InsightMetric("5h 54m", "Study time", Purple, Modifier.weight(1f))
        InsightMetric("+18%", "vs last week", Mint, Modifier.weight(1f))
    }
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        InsightMetric("12 days", "Current streak", Coral, Modifier.weight(1f))
        InsightMetric("46 min", "Daily average", Gold, Modifier.weight(1f))
    }
    Spacer(Modifier.height(18.dp))
    Text("Your strongest study day is Wednesday. Try protecting a similar time slot next week.", color = MutedDark, fontSize = 15.sp, lineHeight = 22.sp)
}

@Composable
fun InsightMetric(value: String, label: String, accent: Color, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFFF7F6FB)).padding(16.dp)) {
        Text(value, color = accent, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(4.dp))
        Text(label, color = MutedDark, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AllCoursesSheet(onCourseClick: (CourseItem) -> Unit) {
    SheetTitle("All courses", "Pick up where you left off.")
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        courseSeed.forEach { course ->
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color(0xFFF7F6FB)).clickable { onCourseClick(course) }.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(Modifier.size(44.dp).clip(RoundedCornerShape(15.dp)).background(course.accent.copy(alpha = 0.13f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.MenuBook, null, tint = course.accent, modifier = Modifier.size(23.dp))
                }
                Column(Modifier.weight(1f)) {
                    Text(course.title, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(3.dp))
                    Text("${course.completed}/${course.total} lessons", color = MutedDark, fontSize = 13.sp)
                }
                Text("${course.percent}%", color = course.accent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FullPlanSheet(tasks: List<StudyTask>) {
    SheetTitle("Today's plan", "${tasks.count { it.done }} of ${tasks.size} tasks completed")
    Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        tasks.forEach { task ->
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color(0xFFF7F6FB)).padding(14.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(if (task.done) Icons.Filled.CheckCircle else Icons.Filled.AccessTime, null, tint = if (task.done) Mint else Purple, modifier = Modifier.padding(top = 2.dp).size(22.dp))
                Column(Modifier.weight(1f)) {
                    Text(task.title, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(task.course, color = MutedDark, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("${task.time} · ${task.duration}", color = Purple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MoreSheet(
    onChoose: (Destination) -> Unit,
    onOpenNotes: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenTimer: () -> Unit
) {
    SheetTitle("More", "Your study space, all in one place.")
    val actions = listOf(
        Triple("Notes", "Capture ideas and reminders", Icons.Filled.Edit),
        Triple("Flashcards", "Review key concepts", Icons.Filled.Layers),
        Triple("Quick quiz", "Test what you remember", Icons.Filled.HelpOutline),
        Triple("Focus timer", "Start a focused session", Icons.Filled.Timer),
    )
    val callbacks = listOf(onOpenNotes, onOpenFlashcards, onOpenQuiz, onOpenTimer)
    actions.forEachIndexed { index, action ->
        Row(Modifier.fillMaxWidth().padding(bottom = 9.dp).clip(RoundedCornerShape(17.dp)).background(Color(0xFFF7F6FB)).clickable { callbacks[index]() }.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(action.third, null, tint = Purple, modifier = Modifier.size(23.dp))
            Column(Modifier.weight(1f)) {
                Text(action.first, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(action.second, color = MutedDark, fontSize = 13.sp)
            }
            Icon(Icons.Filled.ChevronRight, null, tint = MutedDark, modifier = Modifier.size(18.dp))
        }
    }
    Spacer(Modifier.height(5.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedActionButton("Home", Modifier.weight(1f)) { onChoose(Destination.Home) }
        OutlinedActionButton("Learn", Modifier.weight(1f)) { onChoose(Destination.Learn) }
        OutlinedActionButton("Today's plan", Modifier.weight(1f)) { onChoose(Destination.Plan) }
    }
}

@Composable
fun CourseDetailSheet(course: CourseItem, onResume: () -> Unit) {
    SheetTitle(course.title, "${course.category} · ${course.completed}/${course.total} lessons completed")
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(Modifier.size(60.dp).clip(RoundedCornerShape(22.dp)).background(course.accent.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.MenuBook, null, tint = course.accent, modifier = Modifier.size(29.dp))
        }
        Column(Modifier.weight(1f)) {
            Text("Course progress", color = MutedDark, fontSize = 13.sp)
            Spacer(Modifier.height(5.dp))
            Text("${course.percent}% complete", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
    Spacer(Modifier.height(18.dp))
    ProgressTrack(course.progress, 10.dp, Color(0xFFECEAF4), Brush.horizontalGradient(listOf(Purple, Violet)))
    Spacer(Modifier.height(22.dp))
    Text("Up next", color = MutedDark, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(5.dp))
    Text(if (course.title.contains("Physics")) "Newton's Third Law in action" else "Continue the next lesson", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(20.dp))
    GradientButton("Resume course", { Icon(Icons.Filled.PlayArrow, null, tint = Color.White, modifier = Modifier.size(23.dp)) }, onResume, 58.dp, 17.sp, 20.dp)
}

@Composable
fun OutlinedActionButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White,
        contentColor = Purple,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Color(0xFFE2DDF8)),
        modifier = modifier.height(52.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text, color = Purple, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
