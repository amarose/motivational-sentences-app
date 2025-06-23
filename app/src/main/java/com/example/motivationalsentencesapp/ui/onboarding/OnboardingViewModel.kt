package com.example.motivationalsentencesapp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.domain.usecase.GetNotificationPreferencesUseCase
import com.example.motivationalsentencesapp.domain.usecase.SetOnboardingCompletedUseCase
import com.example.motivationalsentencesapp.domain.usecase.UpdateNotificationPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val notificationEnabled: Boolean = true,
    val notificationCount: Int = 1,
    val notificationTimes: List<String> = listOf("09:00"),
    val timePickerError: String? = null,
    val isSaveEnabled: Boolean = true
)

class OnboardingViewModel(
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase,
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getNotificationPreferencesUseCase().collect { preferences ->
                _uiState.update {
                    it.copy(
                        notificationEnabled = preferences.notificationEnabled,
                        notificationTimes = preferences.notificationTimes,
                        notificationCount = preferences.notificationTimes.size
                    )
                }
            }
        }
    }

    fun onNotificationEnabledChanged(enabled: Boolean) {
        _uiState.update { it.copy(notificationEnabled = enabled) }
    }

    private fun findNextAvailableTime(existingTimes: List<String>): String {
        for (hour in 9..23) {
            val time = String.format("%02d:00", hour)
            if (!existingTimes.contains(time)) return time
        }
        for (hour in 0..8) {
            val time = String.format("%02d:00", hour)
            if (!existingTimes.contains(time)) return time
        }
        return "00:00" // Fallback
    }

    fun onNotificationCountChanged(count: Int) {
        _uiState.update { state ->
            val currentTimes = state.notificationTimes.toMutableList()

            while (currentTimes.size > count) {
                currentTimes.removeLast()
            }
            while (currentTimes.size < count) {
                currentTimes.add(findNextAvailableTime(currentTimes))
            }

            val hasDuplicates = currentTimes.size != currentTimes.toSet().size
            state.copy(
                notificationCount = count,
                notificationTimes = currentTimes,
                isSaveEnabled = !hasDuplicates
            )
        }
    }

    fun onNotificationTimeChanged(index: Int, time: String) {
        val currentTimes = _uiState.value.notificationTimes
        if (currentTimes.filterIndexed { i, _ -> i != index }.contains(time)) {
            _uiState.update { it.copy(timePickerError = "Wybrana godzina jest już zajęta.", isSaveEnabled = false) }
            return
        }

        _uiState.update { state ->
            val updatedTimes = state.notificationTimes.toMutableList()
            updatedTimes[index] = time
            val hasDuplicates = updatedTimes.size != updatedTimes.toSet().size
            state.copy(
                notificationTimes = updatedTimes,
                timePickerError = null,
                isSaveEnabled = !hasDuplicates
            )
        }
    }

    fun clearTimePickerError() {
        _uiState.update { it.copy(timePickerError = null) }
    }

    fun onSavePreferences() {
        if (!_uiState.value.isSaveEnabled) {
            _uiState.update { it.copy(timePickerError = "Wybrane godziny muszą być różne.") }
            return
        }
        viewModelScope.launch {
            updateNotificationPreferencesUseCase(
                notificationEnabled = _uiState.value.notificationEnabled,
                notificationTimes = _uiState.value.notificationTimes
            )
            setOnboardingCompletedUseCase(true)
        }
    }
}
