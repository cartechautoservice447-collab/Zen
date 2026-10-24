package com.example.model

enum class TaskPriority {
    HIGH, MEDIUM, NORMAL
}

data class StudentProfile(
    val name: String = "Alex",
    val email: String = "alex@midnightfocus.edu",
    val gradeLevel: String = "Undergraduate - STEM",
    val studentType: String = "College",
    val dailyGoalMinutes: Int = 120,
    val streakDays: Int = 12,
    val totalStudyHours: Float = 86.4f,
    val totalStudyTimeFormatted: String = "86h 25m",
    val completedLessonsCount: Int = 47,
    val todayFocusMinutes: Int = 75,
    val weeklyStudyHours: Float = 14.4f,
    val weeklyGoalHours: Float = 15.0f,
    val weeklyGoalPercent: Int = 68,
    val xpPoints: Int = 2450,
    val quizAccuracyPercent: Int = 94,
    val selectedSubjects: List<String> = listOf("Chemistry", "Mathematics", "Languages", "Computer Science", "Humanities"),
    val streakMultiplier: Float = 1.5f,
    val streakFreezesAvailable: Int = 2,
    val last7DaysStreak: List<Boolean> = listOf(true, true, true, true, true, true, true),
    val focusHeadline: String = "Your next breakthrough starts here.",
    val greeting: String = "Good evening, Alex",
    val tagline: String = "Ready for a focused session?",
    val initials: String = "A"
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
    val category: String, // "Chemistry", "Mathematics", "Languages", "Computer Science", "Humanities"
    val instructor: String,
    val progress: Int, // 0..100
    val totalLessons: Int,
    val completedLessons: Int,
    val colorHex: Long,
    val nextLessonTitle: String,
    val description: String,
    val durationMinutes: Int = 24,
    val accentName: String = "violet", // "violet", "cyan", "lime", "warning", "success"
    val lessons: List<Lesson> = emptyList()
)

data class TaskItem(
    val id: String,
    val title: String,
    val subjectCode: String,
    val dueDate: String,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val isCompleted: Boolean = false,
    val courseName: String = "",
    val windowTime: String = ""
)

data class TimetableSlot(
    val id: String,
    val timeRange: String,
    val subjectName: String,
    val subjectCode: String,
    val roomLocation: String,
    val instructor: String,
    val isNow: Boolean = false,
    val isCompleted: Boolean = false
)

data class DailyActivity(
    val day: String, // "Mon", "Tue", ...
    val dateLabel: String = "",
    val hours: Float,
    val targetHours: Float = 1.5f,
    val completedLessons: Int = 0,
    val isGoalMet: Boolean = false,
    val minutes: Int = 0
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean,
    val progressText: String,
    val category: String,
    val xpReward: Int = 150
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
    val date: String,
    val priorityLevel: String = "HIGH",
    val syllabusProgress: Int = 75,
    val totalTopics: Int = 12,
    val masteredTopics: Int = 9
)

data class StudyInsight(
    val id: String,
    val title: String,
    val recommendation: String,
    val subjectCode: String,
    val actionLabel: String,
    val insightType: String // "NEXT_LESSON", "NEEDS_ATTENTION", "STREAK_MOMENTUM"
)

data class FlashcardItem(
    val id: String,
    val subjectCode: String,
    val topic: String,
    val question: String,
    val answer: String,
    val hint: String = "",
    val isMastered: Boolean = false
)

data class StudyBuddyPeer(
    val id: String,
    val name: String,
    val avatarColorHex: Long,
    val subjectCode: String,
    val currentTask: String,
    val minutesActive: Int,
    val isLive: Boolean = true
)
