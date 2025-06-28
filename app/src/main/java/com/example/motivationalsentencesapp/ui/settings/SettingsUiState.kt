package com.example.motivationalsentencesapp.ui.settings

import com.example.motivationalsentencesapp.ui.theme.Pink40

data class SettingsUiState(
    val notificationsEnabled: Boolean = false,
    val notificationTimes: List<String> = emptyList(),
    val notificationQuantity: Int = 1,
    val isLoading: Boolean = false,
    val nextNotificationTime: Long? = null,
    val selectedTextColor: Int = android.graphics.Color.WHITE
)
