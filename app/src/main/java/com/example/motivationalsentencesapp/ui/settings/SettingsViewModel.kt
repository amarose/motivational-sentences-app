package com.example.motivationalsentencesapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.domain.usecase.GetNotificationPreferencesUseCase
import com.example.motivationalsentencesapp.domain.usecase.UpdateNotificationPreferencesUseCase
import com.example.motivationalsentencesapp.ui.notification.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase,
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _showSaveConfirmation = MutableStateFlow(false)
    val showSaveConfirmation = _showSaveConfirmation.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        getNotificationPreferencesUseCase()
            .onEach { preferences ->
                _uiState.update {
                    it.copy(
                        notificationsEnabled = preferences.notificationEnabled,
                        notificationTimes = preferences.notificationTimes,
                        notificationQuantity = preferences.notificationQuantity,
                        isLoading = false
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onNotificationsEnabledChanged(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }

    fun onSaveConfirmationShown() {
        _showSaveConfirmation.value = false
    }

    fun onNotificationTimeChanged(index: Int, time: String) {
        _uiState.update {
            val newTimes = it.notificationTimes.toMutableList()
            if (index in newTimes.indices) {
                newTimes[index] = time
            }
            it.copy(notificationTimes = newTimes)
        }
    }

    fun onNotificationQuantityChanged(quantity: Int) {
        _uiState.update {
            val currentTimes = it.notificationTimes
            val newTimes = if (quantity > currentTimes.size) {
                currentTimes + List(quantity - currentTimes.size) { "09:00" }
            } else {
                currentTimes.take(quantity)
            }
            it.copy(notificationQuantity = quantity, notificationTimes = newTimes)
        }
    }

    fun savePreferences() {
        viewModelScope.launch {
            updateNotificationPreferencesUseCase(
                notificationEnabled = _uiState.value.notificationsEnabled,
                notificationTimes = _uiState.value.notificationTimes,
                notificationQuantity = _uiState.value.notificationQuantity
            )
            _showSaveConfirmation.value = true
            notificationScheduler.reschedule()
        }
    }
}
