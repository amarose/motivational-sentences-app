package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow

interface GetNotificationPreferencesUseCase {
    operator fun invoke(): Flow<NotificationPreferences>
}
