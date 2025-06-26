package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val selectedBackgroundResId: Flow<Int>
    val userPreferences: Flow<NotificationPreferences>

    suspend fun updateNotificationPreferences(
        notificationEnabled: Boolean,
        notificationTimes: List<String>,
        notificationQuantity: Int
    )

    fun isOnboardingCompleted(): Flow<Boolean>

        suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun updateSelectedBackground(backgroundResId: Int)
}
