package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.NotificationPreferences
import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationPreferencesUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetNotificationPreferencesUseCase {
    override fun invoke(): Flow<NotificationPreferences> = userPreferencesRepository.userPreferences
}
