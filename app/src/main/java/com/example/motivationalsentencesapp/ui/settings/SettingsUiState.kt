package com.example.motivationalsentencesapp.ui.settings

data class SettingsUiState(
    val notificationsEnabled: Boolean = true,
    val notificationTimes: List<String> = listOf("09:00"),
    val notificationQuantity: Int = 1,
    val isLoading: Boolean = true
)
