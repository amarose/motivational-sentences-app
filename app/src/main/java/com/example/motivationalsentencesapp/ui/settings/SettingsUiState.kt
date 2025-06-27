package com.example.motivationalsentencesapp.ui.settings

data class SettingsUiState(
    val notificationsEnabled: Boolean = false,
    val notificationTimes: List<String> = emptyList(),
    val notificationQuantity: Int = 1,
    val isLoading: Boolean = true,
    val selectedTextColor: Int = android.graphics.Color.WHITE
)
