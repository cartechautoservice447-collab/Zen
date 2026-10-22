package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LearnlyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class TimerMode {
    POMODORO,
    STOPWATCH
}

enum class PomodoroPhase(val label: String, val defaultDurationSec: Int) {
    FOCUS("Deep Focus", 25 * 60),
    SHORT_BREAK("Short Break", 5 * 60),
    LONG_BREAK("Long Break", 15 * 60)
}

class FocusTimerViewModel(
    private val repository: LearnlyRepository
) : ViewModel() {

    private val _timerMode = MutableStateFlow(TimerMode.POMODORO)
    val timerMode: StateFlow<TimerMode> = _timerMode.asStateFlow()

    private val _pomodoroPhase = MutableStateFlow(PomodoroPhase.FOCUS)
    val pomodoroPhase: StateFlow<PomodoroPhase> = _pomodoroPhase.asStateFlow()

    // Remaining seconds for Pomodoro (counts down)
    private val _remainingSeconds = MutableStateFlow(PomodoroPhase.FOCUS.defaultDurationSec)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    // Elapsed seconds for Stopwatch (counts up)
    private val _stopwatchSeconds = MutableStateFlow(0)
    val stopwatchSeconds: StateFlow<Int> = _stopwatchSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _completedCycles = MutableStateFlow(0)
    val completedCycles: StateFlow<Int> = _completedCycles.asStateFlow()

    private val _selectedSubject = MutableStateFlow("MATH 201: Multivariable Calculus")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    private val _ambientSoundEnabled = MutableStateFlow(false)
    val ambientSoundEnabled: StateFlow<Boolean> = _ambientSoundEnabled.asStateFlow()

    private var tickerJob: Job? = null

    val availableSubjects = listOf(
        "MATH 201: Multivariable Calculus",
        "CS 210: Algorithms & Graphs",
        "PHYS 202: Classical Mechanics",
        "CHEM 220: Organic Chemistry",
        "MATH 215: Linear Algebra",
        "Independent Reading & Problem Sets"
    )

    fun switchMode(mode: TimerMode) {
        if (_timerMode.value != mode) {
            pause()
            _timerMode.value = mode
        }
    }

    fun setSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun toggleAmbientSound() {
        _ambientSoundEnabled.value = !_ambientSoundEnabled.value
    }

    fun start() {
        if (_isRunning.value) return
        _isRunning.value = true

        tickerJob?.cancel()
        tickerJob = viewModelScope.launch {
            while (isActive && _isRunning.value) {
                delay(1000L)
                if (_timerMode.value == TimerMode.POMODORO) {
                    if (_remainingSeconds.value > 0) {
                        _remainingSeconds.value -= 1
                    } else {
                        onPomodoroPhaseComplete()
                    }
                } else {
                    _stopwatchSeconds.value += 1
                }
            }
        }
    }

    fun pause() {
        _isRunning.value = false
        tickerJob?.cancel()
        tickerJob = null
    }

    fun togglePlayPause() {
        if (_isRunning.value) pause() else start()
    }

    fun reset() {
        pause()
        if (_timerMode.value == TimerMode.POMODORO) {
            _remainingSeconds.value = _pomodoroPhase.value.defaultDurationSec
        } else {
            // If stopwatch was run, optionally record session if > 1 min
            val minutes = _stopwatchSeconds.value / 60
            if (minutes >= 1) {
                repository.recordFocusSession(
                    subject = _selectedSubject.value,
                    minutes = minutes,
                    mode = "Stopwatch"
                )
            }
            _stopwatchSeconds.value = 0
        }
    }

    fun skipInterval() {
        if (_timerMode.value == TimerMode.POMODORO) {
            pause()
            advancePomodoroPhase()
        }
    }

    private fun onPomodoroPhaseComplete() {
        pause()
        if (_pomodoroPhase.value == PomodoroPhase.FOCUS) {
            val focusMinutes = PomodoroPhase.FOCUS.defaultDurationSec / 60
            repository.recordFocusSession(
                subject = _selectedSubject.value,
                minutes = focusMinutes,
                mode = "Pomodoro"
            )
            val newCycle = _completedCycles.value + 1
            _completedCycles.value = newCycle
            advancePomodoroPhase()
        } else {
            advancePomodoroPhase()
        }
    }

    private fun advancePomodoroPhase() {
        val nextPhase = when (_pomodoroPhase.value) {
            PomodoroPhase.FOCUS -> {
                if (_completedCycles.value > 0 && _completedCycles.value % 4 == 0) {
                    PomodoroPhase.LONG_BREAK
                } else {
                    PomodoroPhase.SHORT_BREAK
                }
            }
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.FOCUS
        }
        _pomodoroPhase.value = nextPhase
        _remainingSeconds.value = nextPhase.defaultDurationSec
    }

    override fun onCleared() {
        super.onCleared()
        tickerJob?.cancel()
    }
}
