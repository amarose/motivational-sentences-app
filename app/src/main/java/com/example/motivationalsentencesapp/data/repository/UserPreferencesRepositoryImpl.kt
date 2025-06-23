package com.example.motivationalsentencesapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.motivationalsentencesapp.data.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val NOTIFICATION_TIMES = stringSetPreferencesKey("notification_times")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override val userPreferences: Flow<NotificationPreferences> = context.dataStore.data
        .map { preferences ->
            val notificationEnabled = preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: true
            val notificationTimes = preferences[PreferencesKeys.NOTIFICATION_TIMES] ?: setOf("09:00")
            NotificationPreferences(notificationEnabled, notificationTimes.toList())
        }

    override suspend fun updateNotificationPreferences(notificationEnabled: Boolean, notificationTimes: List<String>) {
        context.dataStore.edit {
            it[PreferencesKeys.NOTIFICATION_ENABLED] = notificationEnabled
            it[PreferencesKeys.NOTIFICATION_TIMES] = notificationTimes.toSet()
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit {
            it[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }
}
