package com.example.ui.learnly

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Night = Color(0xFF0B1020)
val NightCard = Color(0xFF141B2D)
val NightCardAlt = Color(0xFF1A2238)
val NightBorder = Color(0xFF283149)
val Purple = Color(0xFF7860FF)
val Violet = Color(0xFF9965F8)
val Muted = Color(0xFF8992AB)
val MutedDark = Color(0xFF70798E)
val Canvas = Color(0xFFF7F6FB)
val Ink = Color(0xFF171729)
val Coral = Color(0xFFF47F69)
val Gold = Color(0xFFF5C65E)
val Mint = Color(0xFF45B88B)
val Cyan = Color(0xFF53D5E8)
val Lime = Color(0xFFC6EF68)
val Lavender = Color(0xFFEAE5FF)
val SoftPurple = Color(0xFFEEEBFF)
val SoftCoral = Color(0xFFFFE7E1)
val SoftYellow = Color(0xFFFFF1C9)
val SoftMint = Color(0xFFD9F7E9)

// Pre-cached immutable Brushes to avoid allocations during scroll and draw passes
val PurpleToVioletGradient = Brush.horizontalGradient(listOf(Purple, Violet))
val PurpleToVioletLinear = Brush.linearGradient(listOf(Purple, Violet))
val FocusCardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF141B2D), Color(0xFF151C31), Color(0xFF181C3A)),
    start = Offset(0f, 0f),
    end = Offset(900f, 400f)
)
val ContinueCardGradient = Brush.linearGradient(
    listOf(Color(0xFF6257F2), Color(0xFF8251F4), Color(0xFF9554F1))
)
val ActiveBarGradient = Brush.verticalGradient(listOf(Color(0xFF5F45E8), Color(0xFF7846E9)))
val InactiveBarGradient = Brush.verticalGradient(listOf(Color(0xFFD8D2FA), Color(0xFFC9C0F6)))
val GoldProgressGradient = Brush.horizontalGradient(listOf(Gold, Color(0xFFFFD46D)))

@Immutable
data class CourseItem(
    val category: String,
    val title: String,
    val completed: Int,
    val total: Int,
    val accent: Color,
    val icon: CourseIcon = CourseIcon.Book
) {
    val progress: Float get() = completed.toFloat() / total.coerceAtLeast(1)
    val percent: Int get() = (progress * 100).toInt()
}

enum class CourseIcon { Physics, Math, Chemistry, Book, Code }

@Immutable
data class StudyTask(
    val id: Int,
    val title: String,
    val course: String,
    val time: String,
    val duration: String,
    val done: Boolean = false
)

enum class ToolKind { Notes, Flashcards, Quiz, FocusTimer }
enum class Destination { Home, Learn, Plan, More }

sealed class DashboardSheet {
    object FocusTimer : DashboardSheet()
    object Notes : DashboardSheet()
    object Flashcards : DashboardSheet()
    object Quiz : DashboardSheet()
    object Notifications : DashboardSheet()
    object Analytics : DashboardSheet()
    object AllCourses : DashboardSheet()
    object FullPlan : DashboardSheet()
    object More : DashboardSheet()
    data class CourseDetails(val course: CourseItem) : DashboardSheet()
}

val courseSeed = listOf(
    CourseItem("Physics", "Physics: Motion & Forces", 14, 22, Purple, CourseIcon.Physics),
    CourseItem("Mathematics", "Algebra II", 9, 20, Coral, CourseIcon.Math),
    CourseItem("Literature", "Modern Literature", 6, 12, Gold, CourseIcon.Book),
    CourseItem("Computer Science", "Intro to Python", 17, 24, Cyan, CourseIcon.Code),
    CourseItem("Chemistry", "Organic Chemistry II", 12, 28, Purple, CourseIcon.Chemistry),
)

val taskSeed = listOf(
    StudyTask(1, "Watch: Newton's Third Law", "Physics · Motion & Forces", "18:00", "18 min", true),
    StudyTask(2, "Practice set — quadratics", "Algebra II", "19:00", "25 min"),
    StudyTask(3, "Review 20 flashcards", "Organic Chemistry", "20:00", "10 min"),
    StudyTask(4, "Read chapter 4 notes", "Modern Literature", "20:30", "15 min"),
    StudyTask(5, "Python practice problems", "Intro to Python", "21:15", "30 min"),
)
