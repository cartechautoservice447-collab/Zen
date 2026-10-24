package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LearnlyRepository
import com.example.model.Course
import com.example.model.DailyActivity
import com.example.model.StudentProfile
import com.example.model.TaskPriority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class AppTab(val label: String) {
    HOME("Home"),
    COURSES("Courses"),
    PLANNER("Planner"),
    PROGRESS("Progress"),
    PROFILE("Profile")
}

class LearnlyViewModel(
    val repository: LearnlyRepository = LearnlyRepository()
) : ViewModel() {

    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _showFocusScreen = MutableStateFlow(false)
    val showFocusScreen: StateFlow<Boolean> = _showFocusScreen.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    private val _themeMode = MutableStateFlow("system") // "system", "light", "dark"
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedCourseForDetail = MutableStateFlow<Course?>(null)
    val selectedCourseForDetail: StateFlow<Course?> = _selectedCourseForDetail.asStateFlow()

    private val _showAddTaskDialog = MutableStateFlow(false)
    val showAddTaskDialog: StateFlow<Boolean> = _showAddTaskDialog.asStateFlow()

    private val _chartRange = MutableStateFlow("7D") // "7D", "30D", "90D"
    val chartRange: StateFlow<String> = _chartRange.asStateFlow()

    val studentProfile: StateFlow<StudentProfile> = repository.studentProfile
    val coursesList: StateFlow<List<Course>> = repository.courses
    val tasks = repository.tasks
    val schedule = repository.schedule
    val weeklyActivity = repository.weeklyActivity
    val monthlyActivity = repository.monthlyActivity
    val quarterlyActivity = repository.quarterlyActivity
    val examReminders = repository.examReminders
    val achievements = repository.achievements
    val focusSessions = repository.focusSessions
    val studyInsights = repository.studyInsights
    val flashcards = repository.flashcards
    val studyBuddies = repository.studyBuddies

    // Active chart activity based on selected range
    val currentChartActivity: StateFlow<List<DailyActivity>> = combine(
        _chartRange,
        weeklyActivity,
        monthlyActivity,
        quarterlyActivity
    ) { range, weekly, monthly, quarterly ->
        when (range) {
            "30D" -> monthly
            "90D" -> quarterly
            else -> weekly
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.weeklyActivity.value)

    // Filtered courses based on category and search query
    val filteredCourses: StateFlow<List<Course>> = combine(
        repository.courses,
        _searchQuery,
        _selectedCategory
    ) { courses, query, category ->
        courses.filter { course ->
            val matchesCategory = (category == "All" || course.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                course.title.contains(query, ignoreCase = true) ||
                course.code.contains(query, ignoreCase = true) ||
                course.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun openFocusScreen() {
        _showFocusScreen.value = true
    }

    fun closeFocusScreen() {
        _showFocusScreen.value = false
    }

    fun openOnboarding() {
        _showOnboarding.value = true
    }

    fun setChartRange(range: String) {
        _chartRange.value = range
    }

    fun completeOnboarding(gradeLevel: String, studentType: String, dailyGoal: Int) {
        repository.updateProfile(
            name = studentProfile.value.name,
            gradeLevel = gradeLevel,
            studentType = studentType
        )
        repository.updateDailyGoal(dailyGoal)
        _showOnboarding.value = false
    }

    fun skipOnboarding() {
        _showOnboarding.value = false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun openCourseDetail(course: Course) {
        _selectedCourseForDetail.value = course
    }

    fun closeCourseDetail() {
        _selectedCourseForDetail.value = null
    }

    fun openAddTaskDialog() {
        _showAddTaskDialog.value = true
    }

    fun closeAddTaskDialog() {
        _showAddTaskDialog.value = false
    }

    fun toggleTask(taskId: String) {
        repository.toggleTask(taskId)
    }

    fun toggleFlashcardMastered(cardId: String) {
        repository.toggleFlashcardMastered(cardId)
    }

    fun addTask(title: String, subjectCode: String, dueDate: String, priority: TaskPriority) {
        repository.addTask(title, subjectCode, dueDate, priority)
        closeAddTaskDialog()
    }

    fun completeLesson(courseId: String, lessonId: String) {
        repository.completeLesson(courseId, lessonId)
        _selectedCourseForDetail.value?.let { current ->
            if (current.id == courseId) {
                val updatedCourse = repository.courses.value.firstOrNull { it.id == courseId }
                _selectedCourseForDetail.value = updatedCourse
            }
        }
    }

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
    }

    fun updateDailyGoal(minutes: Int) {
        repository.updateDailyGoal(minutes)
    }

    fun resetDemoData() {
        repository.resetDemoData()
        _searchQuery.value = ""
        _selectedCategory.value = "All"
        _selectedCourseForDetail.value = null
        _chartRange.value = "7D"
    }
}
