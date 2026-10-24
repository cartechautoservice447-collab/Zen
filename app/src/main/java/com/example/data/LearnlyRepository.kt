package com.example.data

import com.example.model.Achievement
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.ExamReminder
import com.example.model.FlashcardItem
import com.example.model.FocusSession
import com.example.model.Lesson
import com.example.model.StudentProfile
import com.example.model.StudyBuddyPeer
import com.example.model.StudyInsight
import com.example.model.TaskItem
import com.example.model.TaskPriority
import com.example.model.TimetableSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class LearnlyRepository {

    private val _studentProfile = MutableStateFlow(
        StudentProfile(
            name = "Alex",
            email = "alex@midnightfocus.edu",
            gradeLevel = "Undergraduate - STEM",
            studentType = "College",
            dailyGoalMinutes = 120,
            streakDays = 12,
            totalStudyHours = 86.4f,
            totalStudyTimeFormatted = "86h 25m",
            completedLessonsCount = 47,
            todayFocusMinutes = 75,
            weeklyStudyHours = 14.4f,
            weeklyGoalHours = 15.0f,
            weeklyGoalPercent = 68,
            xpPoints = 2450,
            quizAccuracyPercent = 94,
            selectedSubjects = listOf("Chemistry", "Mathematics", "Languages", "Computer Science", "Humanities"),
            streakMultiplier = 1.5f,
            streakFreezesAvailable = 2,
            last7DaysStreak = listOf(true, true, true, true, true, true, true),
            focusHeadline = "Your next breakthrough starts here.",
            greeting = "Good evening, Alex",
            tagline = "Ready for a focused session?",
            initials = "A"
        )
    )
    val studentProfile: StateFlow<StudentProfile> = _studentProfile.asStateFlow()

    private val _courses = MutableStateFlow(initialCourses())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _tasks = MutableStateFlow(initialTasks())
    val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

    private val _schedule = MutableStateFlow(initialSchedule())
    val schedule: StateFlow<List<TimetableSlot>> = _schedule.asStateFlow()

    private val _weeklyActivity = MutableStateFlow(initialWeeklyActivity())
    val weeklyActivity: StateFlow<List<DailyActivity>> = _weeklyActivity.asStateFlow()

    private val _monthlyActivity = MutableStateFlow(initialMonthlyActivity())
    val monthlyActivity: StateFlow<List<DailyActivity>> = _monthlyActivity.asStateFlow()

    private val _quarterlyActivity = MutableStateFlow(initialQuarterlyActivity())
    val quarterlyActivity: StateFlow<List<DailyActivity>> = _quarterlyActivity.asStateFlow()

    private val _examReminders = MutableStateFlow(initialExams())
    val examReminders: StateFlow<List<ExamReminder>> = _examReminders.asStateFlow()

    private val _achievements = MutableStateFlow(initialAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _focusSessions = MutableStateFlow(initialFocusSessions())
    val focusSessions: StateFlow<List<FocusSession>> = _focusSessions.asStateFlow()

    private val _studyInsights = MutableStateFlow(initialStudyInsights())
    val studyInsights: StateFlow<List<StudyInsight>> = _studyInsights.asStateFlow()

    private val _flashcards = MutableStateFlow(initialFlashcards())
    val flashcards: StateFlow<List<FlashcardItem>> = _flashcards.asStateFlow()

    private val _studyBuddies = MutableStateFlow(initialStudyBuddies())
    val studyBuddies: StateFlow<List<StudyBuddyPeer>> = _studyBuddies.asStateFlow()

    fun toggleTask(taskId: String) {
        _tasks.update { currentTasks ->
            currentTasks.map { task ->
                if (task.id == taskId) {
                    val updated = task.copy(isCompleted = !task.isCompleted)
                    if (updated.isCompleted) {
                        _studentProfile.update {
                            it.copy(
                                completedLessonsCount = it.completedLessonsCount + 1,
                                xpPoints = it.xpPoints + 50
                            )
                        }
                    } else {
                        _studentProfile.update {
                            it.copy(
                                completedLessonsCount = (it.completedLessonsCount - 1).coerceAtLeast(0),
                                xpPoints = (it.xpPoints - 50).coerceAtLeast(0)
                            )
                        }
                    }
                    updated
                } else task
            }
        }
    }

    fun addTask(title: String, subjectCode: String, dueDate: String, priority: TaskPriority) {
        val newTask = TaskItem(
            id = UUID.randomUUID().toString(),
            title = title,
            subjectCode = subjectCode,
            dueDate = dueDate,
            priority = priority,
            isCompleted = false,
            courseName = subjectCode,
            windowTime = dueDate
        )
        _tasks.update { listOf(newTask) + it }
    }

    fun deleteTask(taskId: String) {
        _tasks.update { currentTasks -> currentTasks.filter { it.id != taskId } }
    }

    fun markLessonCompleted(courseId: String, lessonId: String) {
        _courses.update { currentCourses ->
            currentCourses.map { course ->
                if (course.id == courseId) {
                    val updatedLessons = course.lessons.map { lesson ->
                        if (lesson.id == lessonId) lesson.copy(isCompleted = true) else lesson
                    }
                    val completed = updatedLessons.count { it.isCompleted }
                    val progress = if (course.totalLessons > 0) {
                        ((completed.toFloat() / course.totalLessons.toFloat()) * 100).toInt()
                    } else course.progress

                    course.copy(
                        lessons = updatedLessons,
                        completedLessons = completed,
                        progress = progress.coerceIn(0, 100)
                    )
                } else course
            }
        }

        _studentProfile.update {
            it.copy(
                completedLessonsCount = it.completedLessonsCount + 1,
                xpPoints = it.xpPoints + 100
            )
        }
    }

    fun completeLesson(courseId: String, lessonId: String) = markLessonCompleted(courseId, lessonId)

    fun recordFocusSession(subject: String, minutes: Int, mode: String) = logFocusSession(minutes, mode, subject)

    fun logFocusSession(minutes: Int, mode: String, subject: String) {
        val session = FocusSession(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            durationMinutes = minutes,
            mode = mode,
            subject = subject
        )
        _focusSessions.update { listOf(session) + it }
        val hoursAdded = minutes / 60.0f

        _studentProfile.update {
            it.copy(
                todayFocusMinutes = it.todayFocusMinutes + minutes,
                totalStudyHours = it.totalStudyHours + hoursAdded,
                weeklyStudyHours = it.weeklyStudyHours + hoursAdded,
                xpPoints = it.xpPoints + (minutes * 2)
            )
        }

        _weeklyActivity.update { current ->
            current.map { day ->
                if (day.day == "Sat") {
                    val updatedMinutes = day.minutes + minutes
                    val updatedHours = updatedMinutes / 60.0f
                    day.copy(
                        minutes = updatedMinutes,
                        hours = updatedHours,
                        isGoalMet = updatedHours >= day.targetHours
                    )
                } else day
            }
        }
    }

    fun updateDailyGoal(minutes: Int) {
        _studentProfile.update { it.copy(dailyGoalMinutes = minutes) }
    }

    fun updateProfile(name: String, gradeLevel: String, studentType: String) {
        _studentProfile.update {
            it.copy(
                name = name,
                gradeLevel = gradeLevel,
                studentType = studentType
            )
        }
    }

    fun toggleFlashcardMastered(cardId: String) {
        _flashcards.update { currentCards ->
            currentCards.map { card ->
                if (card.id == cardId) {
                    val updated = card.copy(isMastered = !card.isMastered)
                    if (updated.isMastered) {
                        _studentProfile.update { it.copy(xpPoints = it.xpPoints + 25) }
                    }
                    updated
                } else card
            }
        }
    }

    fun resetDemoData() {
        _studentProfile.value = StudentProfile()
        _courses.value = initialCourses()
        _tasks.value = initialTasks()
        _schedule.value = initialSchedule()
        _weeklyActivity.value = initialWeeklyActivity()
        _monthlyActivity.value = initialMonthlyActivity()
        _quarterlyActivity.value = initialQuarterlyActivity()
        _examReminders.value = initialExams()
        _achievements.value = initialAchievements()
        _focusSessions.value = initialFocusSessions()
        _studyInsights.value = initialStudyInsights()
        _flashcards.value = initialFlashcards()
        _studyBuddies.value = initialStudyBuddies()
    }

    companion object {
        fun initialCourses(): List<Course> = listOf(
            Course(
                id = "chem-2",
                title = "Organic Chemistry II",
                code = "CHEM 202",
                category = "Chemistry",
                instructor = "Prof. Rivera",
                progress = 42,
                totalLessons = 28,
                completedLessons = 12,
                colorHex = 0xFF7266FF,
                nextLessonTitle = "Reaction Mechanisms: SN1 vs SN2",
                description = "Nucleophilic substitutions, eliminations, addition to carbonyls, aromaticity, and spectroscopic characterization.",
                durationMinutes = 24,
                accentName = "violet",
                lessons = listOf(
                    Lesson("chem-l1", "chem-2", "Reaction Mechanisms: SN1 vs SN2", 24, "Lecture", false),
                    Lesson("chem-l2", "chem-2", "E1 and E2 Elimination Pathways", 30, "Lecture", false),
                    Lesson("chem-l3", "chem-2", "Carbonyl Additions & Grignard Reagents", 35, "Problem Set", false),
                    Lesson("chem-l4", "chem-2", "NMR and IR Spectroscopy Drill", 20, "Quiz", false)
                )
            ),
            Course(
                id = "calc-2",
                title = "Calculus II",
                code = "MATH 202",
                category = "Mathematics",
                instructor = "Prof. Elena Rostova",
                progress = 68,
                totalLessons = 32,
                completedLessons = 22,
                colorHex = 0xFF55D8F2,
                nextLessonTitle = "Integration by Parts Practice",
                description = "Techniques of integration, improper integrals, parametric curves, polar coordinates, and infinite series.",
                durationMinutes = 30,
                accentName = "cyan",
                lessons = listOf(
                    Lesson("calc-l1", "calc-2", "Integration by Parts Practice", 30, "Lecture", false),
                    Lesson("calc-l2", "calc-2", "Trigonometric Substitution Mastery", 25, "Problem Set", false),
                    Lesson("calc-l3", "calc-2", "Taylor and Maclaurin Series", 40, "Lecture", false)
                )
            ),
            Course(
                id = "spanish-a2",
                title = "Spanish A2",
                code = "SPAN 102",
                category = "Languages",
                instructor = "Señora Martinez",
                progress = 25,
                totalLessons = 32,
                completedLessons = 8,
                colorHex = 0xFFC4F36A,
                nextLessonTitle = "Subjunctive Triggers",
                description = "Intermediate vocabulary, past tenses (preterite vs imperfect), imperative commands, and the present subjunctive mood.",
                durationMinutes = 15,
                accentName = "lime",
                lessons = listOf(
                    Lesson("span-l1", "spanish-a2", "Subjunctive Triggers", 15, "Lecture", false),
                    Lesson("span-l2", "spanish-a2", "Conversational Practice: Travel & Food", 20, "Problem Set", false)
                )
            ),
            Course(
                id = "dsa",
                title = "Data Structures",
                code = "CS 201",
                category = "Computer Science",
                instructor = "Prof. Aris Vance",
                progress = 81,
                totalLessons = 32,
                completedLessons = 26,
                colorHex = 0xFFFFC46B,
                nextLessonTitle = "Graph Traversal: BFS & DFS",
                description = "Graphs, balanced search trees, heaps, hash tables, asymptotic analysis, and dynamic programming.",
                durationMinutes = 35,
                accentName = "warning",
                lessons = listOf(
                    Lesson("dsa-l1", "dsa", "Graph Traversal: BFS & DFS", 35, "Lecture", false),
                    Lesson("dsa-l2", "dsa", "Dijkstra's Algorithm Drill", 30, "Problem Set", false)
                )
            ),
            Course(
                id = "history",
                title = "World History",
                code = "HIST 101",
                category = "Humanities",
                instructor = "Dr. Alistair Finch",
                progress = 54,
                totalLessons = 26,
                completedLessons = 14,
                colorHex = 0xFF44D7A1,
                nextLessonTitle = "The Cold War: Origins",
                description = "Global modern conflicts, geopolitical shifts, revolutions, and political philosophies from 1900 to present.",
                durationMinutes = 20,
                accentName = "success",
                lessons = listOf(
                    Lesson("hist-l1", "history", "The Cold War: Origins", 20, "Lecture", false),
                    Lesson("hist-l2", "history", "Post-War Decolonization", 25, "Lecture", false)
                )
            )
        )

        fun initialTasks(): List<TaskItem> = listOf(
            TaskItem(
                id = "t1",
                title = "Review SN1 vs SN2 mechanisms",
                subjectCode = "CHEM 202",
                dueDate = "18:00 · 45 min",
                priority = TaskPriority.HIGH,
                isCompleted = true,
                courseName = "Organic Chemistry II",
                windowTime = "18:00 · 45 min"
            ),
            TaskItem(
                id = "t2",
                title = "Integration by parts drill set",
                subjectCode = "MATH 202",
                dueDate = "19:00 · 30 min",
                priority = TaskPriority.HIGH,
                isCompleted = false,
                courseName = "Calculus II",
                windowTime = "19:00 · 30 min"
            ),
            TaskItem(
                id = "t3",
                title = "Spanish flashcards: 20 new words",
                subjectCode = "SPAN 102",
                dueDate = "20:30 · 15 min",
                priority = TaskPriority.NORMAL,
                isCompleted = false,
                courseName = "Spanish A2",
                windowTime = "20:30 · 15 min"
            ),
            TaskItem(
                id = "t4",
                title = "Graph traversal practice problems",
                subjectCode = "CS 201",
                dueDate = "21:15 · 40 min",
                priority = TaskPriority.HIGH,
                isCompleted = false,
                courseName = "Data Structures",
                windowTime = "21:15 · 40 min"
            ),
            TaskItem(
                id = "t5",
                title = "Weekly recap notes",
                subjectCode = "ALL",
                dueDate = "22:00 · 20 min",
                priority = TaskPriority.NORMAL,
                isCompleted = false,
                courseName = "All subjects",
                windowTime = "22:00 · 20 min"
            )
        )

        fun initialSchedule(): List<TimetableSlot> = listOf(
            TimetableSlot("s1", "09:00 - 10:15", "Organic Chemistry II", "CHEM 202", "Hall B-12", "Prof. Rivera", isNow = false, isCompleted = true),
            TimetableSlot("s2", "11:00 - 12:30", "Calculus II", "MATH 202", "Science 304", "Prof. Elena Rostova", isNow = false, isCompleted = true),
            TimetableSlot("s3", "14:00 - 15:30", "Data Structures Lab", "CS 201", "Turing Lab 3", "Prof. Aris Vance", isNow = true, isCompleted = false),
            TimetableSlot("s4", "16:00 - 17:00", "Spanish A2 Discussion", "SPAN 102", "Humanities 102", "Señora Martinez", isNow = false, isCompleted = false)
        )

        fun initialWeeklyActivity(): List<DailyActivity> = listOf(
            DailyActivity("Mon", "Mon", 52f / 60f, 2.0f, 4, true, minutes = 52),
            DailyActivity("Tue", "Tue", 68f / 60f, 2.0f, 5, true, minutes = 68),
            DailyActivity("Wed", "Wed", 41f / 60f, 2.0f, 3, false, minutes = 41),
            DailyActivity("Thu", "Thu", 84f / 60f, 2.0f, 6, true, minutes = 84),
            DailyActivity("Fri", "Fri", 63f / 60f, 2.0f, 4, true, minutes = 63),
            DailyActivity("Sat", "Sat", 75f / 60f, 2.0f, 5, true, minutes = 75), // Active Sat
            DailyActivity("Sun", "Sun", 36f / 60f, 2.0f, 2, false, minutes = 36)
        )

        fun initialMonthlyActivity(): List<DailyActivity> = listOf(
            DailyActivity("W1", "Oct 1-7", 13.5f, 12.0f, 18, true, minutes = 810),
            DailyActivity("W2", "Oct 8-14", 15.2f, 12.0f, 21, true, minutes = 912),
            DailyActivity("W3", "Oct 15-21", 14.4f, 12.0f, 19, true, minutes = 864),
            DailyActivity("W4", "Oct 22-28", 9.4f, 12.0f, 12, false, minutes = 564)
        )

        fun initialQuarterlyActivity(): List<DailyActivity> = listOf(
            DailyActivity("Aug", "Aug 2026", 48.0f, 45.0f, 62, true, minutes = 2880),
            DailyActivity("Sep", "Sep 2026", 54.5f, 45.0f, 74, true, minutes = 3270),
            DailyActivity("Oct", "Oct 2026", 86.4f, 45.0f, 68, true, minutes = 5184)
        )

        fun initialExams(): List<ExamReminder> = listOf(
            ExamReminder("e1", "CHEM 202", "Organic Chemistry II Midterm", 3, "Oct 27", "HIGH", syllabusProgress = 75, totalTopics = 12, masteredTopics = 9),
            ExamReminder("e2", "MATH 202", "Calculus II Integration Exam", 7, "Oct 31", "HIGH", syllabusProgress = 68, totalTopics = 16, masteredTopics = 11),
            ExamReminder("e3", "CS 201", "Data Structures Midterm", 12, "Nov 05", "MEDIUM", syllabusProgress = 81, totalTopics = 14, masteredTopics = 12)
        )

        fun initialFlashcards(): List<FlashcardItem> = listOf(
            FlashcardItem(
                id = "fc1",
                subjectCode = "CHEM 202",
                topic = "Reaction Mechanisms",
                question = "What is the key mechanistic difference between SN1 and SN2 substitutions?",
                answer = "SN1 is a two-step mechanism via a carbocation intermediate (rate = k[R-X]), giving racemization. SN2 is a concerted one-step mechanism with backside attack (rate = k[R-X][Nu⁻]), leading to complete inversion of stereochemistry.",
                hint = "Think about unimolecular vs biomolecular transition state.",
                isMastered = false
            ),
            FlashcardItem(
                id = "fc2",
                subjectCode = "MATH 202",
                topic = "Integration by Parts",
                question = "What is the standard formula for Integration by Parts and how do you choose 'u'?",
                answer = "∫ u dv = uv - ∫ v du. Choose u using the LIATE rule: Logarithmic, Inverse trig, Algebraic, Trigonometric, Exponential.",
                hint = "Derived from the product rule of differentiation.",
                isMastered = true
            ),
            FlashcardItem(
                id = "fc3",
                subjectCode = "CS 201",
                topic = "Graph Traversal",
                question = "What are the core differences between Breadth-First Search (BFS) and Depth-First Search (DFS)?",
                answer = "BFS uses a FIFO Queue and explores neighbor by neighbor, finding the shortest unweighted path. DFS uses a LIFO Stack (or recursion) and explores as deep as possible before backtracking.",
                hint = "Queue vs Stack data structure.",
                isMastered = false
            ),
            FlashcardItem(
                id = "fc4",
                subjectCode = "SPAN 102",
                topic = "Subjunctive Mood",
                question = "What mnemonic acronym helps remember common triggers for the Spanish subjunctive?",
                answer = "WEIRDOS: Wishes, Emotions, Impersonal expressions, Recommendations, Doubt/Denial, Ojalá, and Speculation.",
                hint = "Expresses subjectivity, doubt, desires, or non-reality.",
                isMastered = false
            )
        )

        fun initialStudyBuddies(): List<StudyBuddyPeer> = listOf(
            StudyBuddyPeer(
                id = "sb1",
                name = "Maya Lin",
                avatarColorHex = 0xFF7266FF, // MidnightPrimary
                subjectCode = "CHEM 202",
                currentTask = "SN1 vs SN2 Mechanisms Lab",
                minutesActive = 38,
                isLive = true
            ),
            StudyBuddyPeer(
                id = "sb2",
                name = "Julian Vance",
                avatarColorHex = 0xFF55D8F2, // MidnightCyan
                subjectCode = "MATH 202",
                currentTask = "Integration by Parts Drill",
                minutesActive = 22,
                isLive = true
            ),
            StudyBuddyPeer(
                id = "sb3",
                name = "Sophia Patel",
                avatarColorHex = 0xFFC4F36A, // MidnightLime
                subjectCode = "SPAN 102",
                currentTask = "Subjunctive Trigger Practice",
                minutesActive = 45,
                isLive = true
            ),
            StudyBuddyPeer(
                id = "sb4",
                name = "Liam Connor",
                avatarColorHex = 0xFFFFC46B, // MidnightWarning
                subjectCode = "CS 201",
                currentTask = "Graph BFS Problems",
                minutesActive = 15,
                isLive = false
            )
        )

        fun initialAchievements(): List<Achievement> = listOf(
            Achievement("a1", "12-Day Streak", "Studied consistently for 12 consecutive days", "local_fire_department", true, "12/12 days", "Consistency", 200),
            Achievement("a2", "Deep Work Titan", "Completed 25+ focus sessions", "timer", true, "28/25 sessions", "Focus", 250),
            Achievement("a3", "STEM Polymath", "Active progress in 5 distinct subjects", "science", true, "5/5 subjects", "Breadth", 150),
            Achievement("a4", "Algorithm Master", "Completed all graph theory modules", "code", false, "81% completed", "Mastery", 300),
            Achievement("a5", "Night Owl Scholar", "Logged 2 hours of focus after 9:00 PM", "bedtime", true, "Unlocked", "Dedication", 100),
            Achievement("a6", "Centurion Club", "Surpassed 80 total logged study hours", "workspace_premium", true, "86.4/80 hours", "Milestone", 500)
        )

        fun initialFocusSessions(): List<FocusSession> = listOf(
            FocusSession("fs1", System.currentTimeMillis() - 3600000, 45, "Pomodoro", "CHEM 202 - Organic Chemistry II"),
            FocusSession("fs2", System.currentTimeMillis() - 7200000, 30, "Pomodoro", "MATH 202 - Calculus II"),
            FocusSession("fs3", System.currentTimeMillis() - 14400000, 40, "Stopwatch", "CS 201 - Data Structures")
        )

        fun initialStudyInsights(): List<StudyInsight> = listOf(
            StudyInsight(
                id = "si1",
                title = "Today's Target Velocity",
                recommendation = "You have studied 75 of 120 minutes today (63%). One 45-minute focus session on Organic Chemistry II will complete your daily goal!",
                subjectCode = "CHEM 202",
                actionLabel = "Start 45m Focus",
                insightType = "STREAK_MOMENTUM"
            ),
            StudyInsight(
                id = "si2",
                title = "Exam Readiness",
                recommendation = "Organic Chemistry II midterm is approaching. You have completed 12 of 28 lessons (42%). Reviewing SN1 vs SN2 tonight will boost your mastery.",
                subjectCode = "CHEM 202",
                actionLabel = "Resume Course",
                insightType = "NEXT_LESSON"
            )
        )
    }
}
