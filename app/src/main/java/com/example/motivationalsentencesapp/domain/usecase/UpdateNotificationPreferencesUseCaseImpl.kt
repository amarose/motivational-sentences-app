package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository

class UpdateNotificationPreferencesUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : UpdateNotificationPreferencesUseCase {
    override suspend fun invoke(
        notificationEnabled: Boolean,
        notificationTimes: List<String>,
        notificationQuantity: Int
    ) {
        userPreferencesRepository.updateNotificationPreferences(
            notificationEnabled,
            notificationTimes,
            notificationQuantity
        )
    }
}
