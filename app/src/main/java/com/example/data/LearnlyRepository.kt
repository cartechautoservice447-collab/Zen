package com.example.data

import com.example.model.Achievement
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.ExamReminder
import com.example.model.FocusSession
import com.example.model.Lesson
import com.example.model.StudentProfile
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
            name = "Alex Rivera",
            email = "alex.rivera@edu.org",
            gradeLevel = "Undergraduate - Computer Science & Math",
            studentType = "College",
            dailyGoalMinutes = 45,
            streakDays = 14,
            totalStudyHours = 52.5f,
            completedLessonsCount = 38,
            todayFocusMinutes = 50,
            selectedSubjects = listOf("Math", "Physics", "Computer Science", "Chemistry")
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

    private val _examReminders = MutableStateFlow(initialExams())
    val examReminders: StateFlow<List<ExamReminder>> = _examReminders.asStateFlow()

    private val _achievements = MutableStateFlow(initialAchievements())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _focusSessions = MutableStateFlow(initialFocusSessions())
    val focusSessions: StateFlow<List<FocusSession>> = _focusSessions.asStateFlow()

    fun toggleTask(taskId: String) {
        _tasks.update { currentTasks ->
            currentTasks.map { task ->
                if (task.id == taskId) {
                    val updated = task.copy(isCompleted = !task.isCompleted)
                    if (updated.isCompleted) {
                        _studentProfile.update { it.copy(completedLessonsCount = it.completedLessonsCount + 1) }
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
            isCompleted = false
        )
        _tasks.update { listOf(newTask) + it }
    }

    fun completeLesson(courseId: String, lessonId: String) {
        _courses.update { currentList ->
            currentList.map { course ->
                if (course.id == courseId) {
                    val updatedLessons = course.lessons.map { lesson ->
                        if (lesson.id == lessonId) lesson.copy(isCompleted = true) else lesson
                    }
                    val completedCount = updatedLessons.count { it.isCompleted }
                    val newProgress = ((completedCount.toFloat() / course.totalLessons.toFloat()) * 100).toInt()
                    course.copy(
                        lessons = updatedLessons,
                        completedLessons = completedCount,
                        progress = newProgress
                    )
                } else course
            }
        }
        _studentProfile.update {
            it.copy(
                completedLessonsCount = it.completedLessonsCount + 1,
                totalStudyHours = it.totalStudyHours + 0.5f
            )
        }
    }

    fun recordFocusSession(subject: String, minutes: Int, mode: String) {
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
                totalStudyHours = it.totalStudyHours + hoursAdded
            )
        }

        // Update today's activity in weekly graph
        _weeklyActivity.update { current ->
            current.map { day ->
                if (day.day == "Thu") { // current simulated day
                    val updatedHours = day.hours + hoursAdded
                    day.copy(
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

    fun resetDemoData() {
        _studentProfile.value = StudentProfile()
        _courses.value = initialCourses()
        _tasks.value = initialTasks()
        _schedule.value = initialSchedule()
        _weeklyActivity.value = initialWeeklyActivity()
        _examReminders.value = initialExams()
        _achievements.value = initialAchievements()
        _focusSessions.value = initialFocusSessions()
    }

    companion object {
        private fun initialCourses(): List<Course> = listOf(
            Course(
                id = "c1",
                title = "Multivariable Calculus",
                code = "MATH 201",
                category = "Math",
                instructor = "Prof. Elena Rostova",
                progress = 68,
                totalLessons = 24,
                completedLessons = 16,
                colorHex = 0xFF4338CA, // Indigo
                nextLessonTitle = "Green's Theorem & Surface Integrals",
                description = "Vector calculus, directional derivatives, Lagrange multipliers, double and triple integrals, Stokes' and Divergence theorems.",
                lessons = listOf(
                    Lesson("l101", "c1", "Vector Functions and Space Curves", 35, "Lecture", true),
                    Lesson("l102", "c1", "Partial Derivatives & Tangent Planes", 40, "Lecture", true),
                    Lesson("l103", "c1", "Lagrange Multipliers Problem Set", 45, "Problem Set", true),
                    Lesson("l104", "c1", "Double Integrals in Polar Coordinates", 30, "Lecture", true),
                    Lesson("l105", "c1", "Green's Theorem & Surface Integrals", 45, "Lecture", false),
                    Lesson("l106", "c1", "Vector Fields & Curl Practice Quiz", 25, "Quiz", false),
                    Lesson("l107", "c1", "Stokes' Theorem in 3D Space", 50, "Lecture", false)
                )
            ),
            Course(
                id = "c2",
                title = "Data Structures & Algorithms",
                code = "CS 210",
                category = "Computer Science",
                instructor = "Dr. Marcus Chen",
                progress = 82,
                totalLessons = 28,
                completedLessons = 23,
                colorHex = 0xFF0284C7, // Sky
                nextLessonTitle = "Dijkstra's Algorithm & Priority Queues",
                description = "Asymptotic notation, balanced search trees, graph algorithms, dynamic programming, and complexity theory.",
                lessons = listOf(
                    Lesson("l201", "c2", "Red-Black Trees & Balancing", 40, "Lecture", true),
                    Lesson("l202", "c2", "Heap Sort & Array Representation", 30, "Lecture", true),
                    Lesson("l203", "c2", "Breadth-First and Depth-First Search", 45, "Problem Set", true),
                    Lesson("l204", "c2", "Dijkstra's Algorithm & Priority Queues", 35, "Lecture", false),
                    Lesson("l205", "c2", "Dynamic Programming: Knapsack", 50, "Problem Set", false),
                    Lesson("l206", "c2", "Midterm Algorithm Review Quiz", 30, "Quiz", false)
                )
            ),
            Course(
                id = "c3",
                title = "Classical Mechanics & Electromagnetism",
                code = "PHYS 202",
                category = "Physics",
                instructor = "Dr. Sarah Thornton",
                progress = 45,
                totalLessons = 20,
                completedLessons = 9,
                colorHex = 0xFF059669, // Mint
                nextLessonTitle = "Gauss's Law & Electric Flux",
                description = "Rigid body motion, Lagrangian mechanics, electrostatics, Gauss's law, capacitance, and magnetic dipole moments.",
                lessons = listOf(
                    Lesson("l301", "c3", "Moment of Inertia & Rotational Kinematics", 45, "Lecture", true),
                    Lesson("l302", "c3", "Coulomb's Law and Electric Field Vectors", 35, "Lecture", true),
                    Lesson("l303", "c3", "Gauss's Law & Electric Flux", 40, "Lecture", false),
                    Lesson("l304", "c3", "Electrostatic Potential Energy Quiz", 25, "Quiz", false),
                    Lesson("l305", "c3", "Capacitors and Dielectrics Workshop", 45, "Problem Set", false)
                )
            ),
            Course(
                id = "c4",
                title = "Organic Chemistry I",
                code = "CHEM 220",
                category = "Chemistry",
                instructor = "Prof. Robert Lang",
                progress = 30,
                totalLessons = 22,
                completedLessons = 7,
                colorHex = 0xFFD97706, // Amber
                nextLessonTitle = "SN1 vs SN2 Nucleophilic Substitution",
                description = "Stereochemistry, reaction energetics, carbocation stability, substitution and elimination pathways.",
                lessons = listOf(
                    Lesson("l401", "c4", "Lewis Structures and Resonance Hybrids", 30, "Lecture", true),
                    Lesson("l402", "c4", "Stereoisomers & R/S Configuration", 45, "Lecture", true),
                    Lesson("l403", "c4", "SN1 vs SN2 Nucleophilic Substitution", 40, "Lecture", false),
                    Lesson("l404", "c4", "Electrophilic Alkene Addition Practice", 35, "Problem Set", false)
                )
            ),
            Course(
                id = "c5",
                title = "Linear Algebra & Matrix Methods",
                code = "MATH 215",
                category = "Math",
                instructor = "Prof. Hiroshi Tanaka",
                progress = 90,
                totalLessons = 18,
                completedLessons = 16,
                colorHex = 0xFF4F46E5, // Indigo
                nextLessonTitle = "Singular Value Decomposition (SVD)",
                description = "Vector spaces, linear transformations, orthogonality, determinants, eigenvalues, eigenvectors, and SVD.",
                lessons = listOf(
                    Lesson("l501", "c5", "Gram-Schmidt Orthonormalization", 35, "Lecture", true),
                    Lesson("l502", "c5", "Eigenvalues and Diagonalization", 45, "Lecture", true),
                    Lesson("l503", "c5", "Singular Value Decomposition (SVD)", 40, "Lecture", false)
                )
            )
        )

        private fun initialTasks(): List<TaskItem> = listOf(
            TaskItem("t1", "Problem Set 4: Stokes' Theorem (Q1 - Q8)", "MATH 201", "Today, 11:59 PM", TaskPriority.HIGH, false),
            TaskItem("t2", "Implement Graph BFS/DFS in Python", "CS 210", "Tomorrow, 5:00 PM", TaskPriority.HIGH, false),
            TaskItem("t3", "Read Chapter 24: Gauss's Law in Cylinders", "PHYS 202", "Oct 24, 9:00 AM", TaskPriority.MEDIUM, false),
            TaskItem("t4", "Organic Chem Synthesis Flashcards (20 cards)", "CHEM 220", "Oct 25, 6:00 PM", TaskPriority.NORMAL, true),
            TaskItem("t5", "Linear Algebra Quiz 3 Prep (Eigenbases)", "MATH 215", "Oct 26, 2:00 PM", TaskPriority.NORMAL, false)
        )

        private fun initialSchedule(): List<TimetableSlot> = listOf(
            TimetableSlot("s1", "09:00 - 10:15 AM", "Multivariable Calculus Lecture", "MATH 201", "Hall B-14", "Prof. Rostova", false),
            TimetableSlot("s2", "11:00 - 12:30 PM", "Data Structures Lab (Graph Theory)", "CS 210", "Turing Lab 3", "Dr. Chen", true),
            TimetableSlot("s3", "02:00 - 03:15 PM", "Classical Mechanics Discussion", "PHYS 202", "Science 102", "Dr. Thornton", false),
            TimetableSlot("s4", "04:00 - 05:00 PM", "Office Hours / Study Group", "MATH 201", "Library Room 4", "Peer Tutor", false)
        )

        private fun initialWeeklyActivity(): List<DailyActivity> = listOf(
            DailyActivity("Mon", 2.2f, 1.5f, 3, true),
            DailyActivity("Tue", 1.8f, 1.5f, 2, true),
            DailyActivity("Wed", 3.1f, 1.5f, 4, true),
            DailyActivity("Thu", 1.5f, 1.5f, 2, true),
            DailyActivity("Fri", 2.6f, 1.5f, 3, true),
            DailyActivity("Sat", 0.9f, 1.0f, 1, false),
            DailyActivity("Sun", 2.4f, 1.0f, 3, true)
        )

        private fun initialExams(): List<ExamReminder> = listOf(
            ExamReminder("e1", "MATH 201", "Multivariable Calculus Midterm II", 4, "Oct 26"),
            ExamReminder("e2", "CS 210", "Algorithms Midterm Exam", 9, "Oct 31"),
            ExamReminder("e3", "PHYS 202", "Electromagnetism Quiz", 14, "Nov 05")
        )

        private fun initialAchievements(): List<Achievement> = listOf(
            Achievement("a1", "14-Day Streak", "Studied consistently for 14 consecutive days", "local_fire_department", true, "14/14 days", "Consistency"),
            Achievement("a2", "Deep Work Titan", "Completed 25+ Pomodoro focus sessions", "timer", true, "28/25 sessions", "Focus"),
            Achievement("a3", "STEM Polymath", "Active progress in 4 distinct STEM subjects", "science", true, "4/4 subjects", "Breadth"),
            Achievement("a4", "Algorithm Master", "Completed all graph theory and dynamic programming modules", "code", false, "82% completed", "Mastery"),
            Achievement("a5", "Night Owl Scholar", "Logged 2 hours of focus after 9:00 PM", "bedtime", true, "Unlocked", "Dedication"),
            Achievement("a6", "Centurion Club", "Surpassed 50 total logged study hours", "workspace_premium", true, "52.5/50 hours", "Milestone")
        )

        private fun initialFocusSessions(): List<FocusSession> = listOf(
            FocusSession("fs1", System.currentTimeMillis() - 3600000, 25, "Pomodoro", "MATH 201 - Multivariable Calculus"),
            FocusSession("fs2", System.currentTimeMillis() - 7200000, 25, "Pomodoro", "CS 210 - Graph Algorithms"),
            FocusSession("fs3", System.currentTimeMillis() - 14400000, 40, "Stopwatch", "PHYS 202 - Problem Set")
        )
    }
}
