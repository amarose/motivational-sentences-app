package com.example.motivationalsentencesapp.ui.main

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.PowerManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.domain.usecase.GetOnboardingStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class MainUiState(
    val showBatteryOptimizationDialog: Boolean = false
)

class MainViewModel(
    getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    val isOnboardingCompleted: StateFlow<Boolean?> = getOnboardingStatusUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        checkBatteryOptimizations()
    }

    fun checkBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getApplication<Application>().getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(getApplication<Application>().packageName)) {
                _uiState.update { it.copy(showBatteryOptimizationDialog = true) }
            }
        }
    }

    fun onBatteryOptimizationDialogDismissed() {
        _uiState.update { it.copy(showBatteryOptimizationDialog = false) }
    }

}
