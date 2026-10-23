package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ui.components.AddTaskDialog
import com.example.ui.components.LessonDetailSheet
import com.example.ui.screens.CoursesScreen
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PlannerScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.theme.LearnlyTheme
import com.example.viewmodel.AppTab
import com.example.viewmodel.FocusTimerViewModel
import com.example.viewmodel.LearnlyViewModel

class MainActivity : ComponentActivity() {

    private val learnlyViewModel: LearnlyViewModel by viewModels()

    private val focusTimerViewModel: FocusTimerViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FocusTimerViewModel(learnlyViewModel.repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeMode by learnlyViewModel.themeMode.collectAsState()
            val isDark = when (themeMode) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            LearnlyTheme(darkTheme = isDark) {
                LearnlyApp(
                    learnlyViewModel = learnlyViewModel,
                    focusTimerViewModel = focusTimerViewModel
                )
            }
        }
    }
}

@Composable
fun LearnlyApp(
    learnlyViewModel: LearnlyViewModel,
    focusTimerViewModel: FocusTimerViewModel
) {
    val currentTab by learnlyViewModel.currentTab.collectAsState()
    val showFocusScreen by learnlyViewModel.showFocusScreen.collectAsState()
    val showOnboarding by learnlyViewModel.showOnboarding.collectAsState()
    val selectedCourseForDetail by learnlyViewModel.selectedCourseForDetail.collectAsState()
    val showAddTaskDialog by learnlyViewModel.showAddTaskDialog.collectAsState()
    val themeMode by learnlyViewModel.themeMode.collectAsState()

    val studentProfile by learnlyViewModel.studentProfile.collectAsState()
    val courses by learnlyViewModel.filteredCourses.collectAsState()
    val tasks by learnlyViewModel.tasks.collectAsState()
    val schedule by learnlyViewModel.schedule.collectAsState()
    val weeklyActivity by learnlyViewModel.weeklyActivity.collectAsState()
    val examReminders by learnlyViewModel.examReminders.collectAsState()
    val achievements by learnlyViewModel.achievements.collectAsState()
    val focusSessions by learnlyViewModel.focusSessions.collectAsState()
    val searchQuery by learnlyViewModel.searchQuery.collectAsState()
    val selectedCategory by learnlyViewModel.selectedCategory.collectAsState()

    // 1. Onboarding Flow
    if (showOnboarding) {
        OnboardingScreen(
            onComplete = { grade, studentType, goal ->
                learnlyViewModel.completeOnboarding(grade, studentType, goal)
            },
            onSkip = { learnlyViewModel.skipOnboarding() }
        )
        return
    }

    // 2. Fullscreen Focus Mode
    if (showFocusScreen) {
        FocusScreen(
            viewModel = focusTimerViewModel,
            recentSessions = focusSessions,
            onBack = { learnlyViewModel.closeFocusScreen() }
        )
        return
    }

    // 3. Main Scaffold with Redesigned Bottom Navigation Bar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                AppTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    val (selectedIcon, unselectedIcon) = when (tab) {
                        AppTab.HOME -> Pair(Icons.Filled.Home, Icons.Outlined.Home)
                        AppTab.COURSES -> Pair(Icons.Filled.School, Icons.Outlined.School)
                        AppTab.PLANNER -> Pair(
                            Icons.Filled.CalendarToday,
                            Icons.Outlined.CalendarToday
                        )
                        AppTab.PROGRESS -> Pair(Icons.Filled.BarChart, Icons.Outlined.BarChart)
                        AppTab.PROFILE -> Pair(Icons.Filled.Person, Icons.Outlined.Person)
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { learnlyViewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                contentDescription = tab.label,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> {
                    HomeScreen(
                        studentProfile = studentProfile,
                        primaryCourse = courses.firstOrNull(),
                        courses = courses,
                        tasks = tasks,
                        schedule = schedule,
                        weeklyActivity = weeklyActivity,
                        nextExam = examReminders.firstOrNull(),
                        examReminders = examReminders,
                        achievements = achievements,
                        onStartFocusClick = { learnlyViewModel.openFocusScreen() },
                        onAddTaskClick = { learnlyViewModel.openAddTaskDialog() },
                        onViewAllCoursesClick = { learnlyViewModel.selectTab(AppTab.COURSES) },
                        onViewAllPlannerClick = { learnlyViewModel.selectTab(AppTab.PLANNER) },
                        onViewAllProgressClick = { learnlyViewModel.selectTab(AppTab.PROGRESS) },
                        onToggleTask = { learnlyViewModel.toggleTask(it) },
                        onResumeCourse = { learnlyViewModel.openCourseDetail(it) },
                        onCourseClick = { learnlyViewModel.openCourseDetail(it) },
                        onProfileClick = { learnlyViewModel.selectTab(AppTab.PROFILE) }
                    )
                }

                AppTab.COURSES -> {
                    BackHandler { learnlyViewModel.selectTab(AppTab.HOME) }
                    CoursesScreen(
                        courses = courses,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        onSearchChange = { learnlyViewModel.setSearchQuery(it) },
                        onCategorySelect = { learnlyViewModel.selectCategory(it) },
                        onCourseClick = { learnlyViewModel.openCourseDetail(it) }
                    )
                }

                AppTab.PLANNER -> {
                    BackHandler { learnlyViewModel.selectTab(AppTab.HOME) }
                    PlannerScreen(
                        schedule = schedule,
                        tasks = tasks,
                        exams = examReminders,
                        onToggleTask = { learnlyViewModel.toggleTask(it) },
                        onAddTaskClick = { learnlyViewModel.openAddTaskDialog() }
                    )
                }

                AppTab.PROGRESS -> {
                    BackHandler { learnlyViewModel.selectTab(AppTab.HOME) }
                    ProgressScreen(
                        studentProfile = studentProfile,
                        weeklyActivity = weeklyActivity,
                        achievements = achievements
                    )
                }

                AppTab.PROFILE -> {
                    BackHandler { learnlyViewModel.selectTab(AppTab.HOME) }
                    ProfileScreen(
                        studentProfile = studentProfile,
                        themeMode = themeMode,
                        onThemeModeChange = { learnlyViewModel.setThemeMode(it) },
                        onDailyGoalChange = { learnlyViewModel.updateDailyGoal(it) },
                        onResetDemoData = { learnlyViewModel.resetDemoData() },
                        onReplayOnboarding = { learnlyViewModel.openOnboarding() }
                    )
                }
            }

            // Interactive Course Detail Syllabus Bottom Sheet
            selectedCourseForDetail?.let { course ->
                LessonDetailSheet(
                    course = course,
                    onDismiss = { learnlyViewModel.closeCourseDetail() },
                    onCompleteLesson = { cId, lId ->
                        learnlyViewModel.completeLesson(cId, lId)
                    }
                )
            }

            // Add Task Dialog
            if (showAddTaskDialog) {
                AddTaskDialog(
                    onDismiss = { learnlyViewModel.closeAddTaskDialog() },
                    onConfirm = { title, subj, due, prio ->
                        learnlyViewModel.addTask(title, subj, due, prio)
                    }
                )
            }
        }
    }
}
