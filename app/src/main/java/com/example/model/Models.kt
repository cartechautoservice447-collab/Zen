package com.example.model

enum class TaskPriority {
    HIGH, MEDIUM, NORMAL
}

data class StudentProfile(
    val name: String = "Alex Rivera",
    val email: String = "alex.rivera@edu.org",
    val gradeLevel: String = "Undergraduate - STEM",
    val studentType: String = "College",
    val dailyGoalMinutes: Int = 45,
    val streakDays: Int = 12,
    val totalStudyHours: Float = 48.5f,
    val completedLessonsCount: Int = 34,
    val todayFocusMinutes: Int = 50,
    val selectedSubjects: List<String> = listOf("Math", "Physics", "Computer Science")
)

data class Lesson(
    val id: String,
    val courseId: String,
    val title: String,
    val durationMinutes: Int,
    val type: String, // "Lecture", "Problem Set", "Quiz"
    val isCompleted: Boolean = false
)

data class Course(
    val id: String,
    val title: String,
    val code: String,
    val category: String, // "Math", "Physics", "Computer Science", "Chemistry"
    val instructor: String,
    val progress: Int, // 0..100
    val totalLessons: Int,
    val completedLessons: Int,
    val colorHex: Long,
    val nextLessonTitle: String,
    val description: String,
    val lessons: List<Lesson> = emptyList()
)

data class TaskItem(
    val id: String,
    val title: String,
    val subjectCode: String,
    val dueDate: String,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val isCompleted: Boolean = false
)

data class TimetableSlot(
    val id: String,
    val timeRange: String,
    val subjectName: String,
    val subjectCode: String,
    val roomLocation: String,
    val instructor: String,
    val isNow: Boolean = false
)

data class DailyActivity(
    val day: String, // "Mon", "Tue", ...
    val hours: Float,
    val targetHours: Float = 1.0f,
    val completedLessons: Int = 0,
    val isGoalMet: Boolean = false
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val progressText: String,
    val category: String
)

data class FocusSession(
    val id: String,
    val timestamp: Long,
    val durationMinutes: Int,
    val mode: String, // "Pomodoro", "Stopwatch"
    val subject: String
)

data class ExamReminder(
    val id: String,
    val subject: String,
    val examTitle: String,
    val daysLeft: Int,
    val date: String
)
