package pl.amarosee.motywator.ui.onboarding

import android.app.Application

import android.content.pm.PackageManager
import android.os.Build

import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import pl.amarosee.motywator.domain.usecase.SetOnboardingCompletedUseCase
import pl.amarosee.motywator.domain.usecase.UpdateNotificationPreferencesUseCase
import pl.amarosee.motywator.ui.notification.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.util.Log
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class OnboardingUiState(
    val notificationEnabled: Boolean = true,
    val notificationQuantity: Int = 1,
    val notificationTimes: List<String> = listOf("09:00"),
    val timePickerError: String? = null,
    val isSaveEnabled: Boolean = true,
    val errorMessage: String? = null,
    val onboardingComplete: Boolean = false
)

class OnboardingViewModel(
    application: Application,
    private val updateNotificationPreferencesUseCase: UpdateNotificationPreferencesUseCase,
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase,
    private val notificationScheduler: NotificationScheduler
) : AndroidViewModel(application) {

    private val _launchPermissionRequest = MutableStateFlow(false)
    val launchPermissionRequest = _launchPermissionRequest.asStateFlow()

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()



    fun onNotificationEnabledChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(notificationEnabled = isEnabled) }
    }

    fun onNotificationQuantityChanged(quantity: Int) {
        _uiState.update {
            val currentTimes = it.notificationTimes
            val newTimes = if (quantity > currentTimes.size) {
                currentTimes + List(quantity - currentTimes.size) { "09:00" }
            } else {
                currentTimes.take(quantity)
            }
            val isSaveEnabled = newTimes.size == newTimes.toSet().size
            it.copy(
                notificationQuantity = quantity,
                notificationTimes = newTimes,
                isSaveEnabled = isSaveEnabled,
                timePickerError = if (isSaveEnabled) null else "Wybrane godziny muszą być różne."
            )
        }
    }

    fun onNotificationTimeChanged(index: Int, time: String) {
        _uiState.update {
            val newTimes = it.notificationTimes.toMutableList()
            if (index in newTimes.indices) {
                newTimes[index] = time
            }
            val isSaveEnabled = newTimes.size == newTimes.toSet().size
            it.copy(
                notificationTimes = newTimes,
                isSaveEnabled = isSaveEnabled,
                timePickerError = if (isSaveEnabled) null else "Wybrane godziny muszą być różne."
            )
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        _launchPermissionRequest.value = false
        _uiState.update { it.copy(notificationEnabled = isGranted) }
        if (isGranted) {
            // Permission was just granted, now proceed with saving.
            saveAndSchedule(_uiState.value)
        } else {
            _uiState.update {
                it.copy(errorMessage = "Zgoda odrzucona. Powiadomienia nie będą wyświetlane.")
            }
        }
    }

    fun onSavePreferences() {
        val currentState = _uiState.value
        if (!currentState.isSaveEnabled) {
            _uiState.update { it.copy(timePickerError = "Wybrane godziny muszą być różne.") }
            return
        }

        if (!currentState.notificationEnabled) {
            saveAndSchedule(currentState)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val context = getApplication<Application>().applicationContext
            val permissionStatus = ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                _launchPermissionRequest.value = true
                return
            }
        }

        saveAndSchedule(currentState)
    }

    internal fun saveAndSchedule(uiState: OnboardingUiState) {
        viewModelScope.launch {
            updateNotificationPreferencesUseCase(
                notificationEnabled = uiState.notificationEnabled,
                notificationTimes = uiState.notificationTimes,
                notificationQuantity = uiState.notificationQuantity
            )
            setOnboardingCompletedUseCase(true)

            notificationScheduler.reschedule()

            _uiState.update { it.copy(onboardingComplete = true) }
        }
    }


    fun onReturnedFromSettings() {
        Log.d("OnboardingViewModel", "Returned from settings, re-evaluating permissions.")
        viewModelScope.launch {
            // Add a small delay to give the system time to propagate the permission change.
            delay(500)
            // Re-run the save logic to check if permissions have been granted
            onSavePreferences()
        }
    }
}
