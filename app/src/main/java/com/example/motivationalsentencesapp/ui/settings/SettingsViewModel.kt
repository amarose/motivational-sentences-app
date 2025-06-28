package com.example.motivationalsentencesapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.data.datastore.SettingsDataStore
import com.example.motivationalsentencesapp.domain.usecase.GetNotificationPreferencesUseCase
import com.example.motivationalsentencesapp.domain.usecase.UpdateNotificationPreferencesUseCase
import com.example.motivationalsentencesapp.ui.notification.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase,
    private val notificationScheduler: NotificationScheduler,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(isLoading = true))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _showSaveConfirmation = MutableStateFlow(false)
    val showSaveConfirmation: StateFlow<Boolean> = _showSaveConfirmation.asStateFlow()

    private val _showDuplicateTimeError = MutableStateFlow(false)
    val showDuplicateTimeError: StateFlow<Boolean> = _showDuplicateTimeError.asStateFlow()

    init {
        loadInitialSettings()
    }

    private fun loadInitialSettings() {
        viewModelScope.launch {
            val notificationPrefs = getNotificationPreferencesUseCase().first()
            val textColor = settingsDataStore.textColorFlow.first()
            _uiState.value = SettingsUiState(
                notificationsEnabled = notificationPrefs.notificationEnabled,
                notificationTimes = notificationPrefs.notificationTimes,
                notificationQuantity = notificationPrefs.notificationQuantity,
                selectedTextColor = textColor,
                isLoading = false
            )
        }
    }

    fun onColorSelected(color: Int) {
        _uiState.update { it.copy(selectedTextColor = color) }
    }

    fun onNotificationsEnabledChanged(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun onNotificationTimeChanged(index: Int, time: String) {
        _uiState.update { currentState ->
            val newTimes = currentState.notificationTimes.toMutableList()
            if (index in newTimes.indices) {
                newTimes[index] = time
            }
            currentState.copy(notificationTimes = newTimes)
        }
    }

    fun onNotificationQuantityChanged(quantity: Int) {
        _uiState.update { currentState ->
            val currentTimes = currentState.notificationTimes
            val newTimes = if (quantity > currentTimes.size) {
                currentTimes + List(quantity - currentTimes.size) { "09:00" }
            } else {
                currentTimes.take(quantity)
            }
            currentState.copy(
                notificationQuantity = quantity,
                notificationTimes = newTimes
            )
        }
    }

    fun savePreferences() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val hasDuplicates = currentState.notificationTimes.groupingBy { it }.eachCount().any { it.value > 1 }

            if (hasDuplicates) {
                _showDuplicateTimeError.value = true
            } else {
                updateNotificationPreferencesUseCase(
                    notificationEnabled = currentState.notificationsEnabled,
                    notificationTimes = currentState.notificationTimes,
                    notificationQuantity = currentState.notificationQuantity
                )
                settingsDataStore.saveTextColor(currentState.selectedTextColor)
                notificationScheduler.reschedule()
                _showSaveConfirmation.value = true
            }
        }
    }

    fun onSaveConfirmationShown() {
        _showSaveConfirmation.value = false
    }

    fun onDuplicateTimeErrorShown() {
        _showDuplicateTimeError.value = false
    }

}
