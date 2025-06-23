package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<NotificationPreferences>

    suspend fun updateNotificationPreferences(notificationEnabled: Boolean, notificationTimes: List<String>)

    fun isOnboardingCompleted(): Flow<Boolean>

    suspend fun setOnboardingCompleted(completed: Boolean)
}
