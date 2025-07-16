package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.UserPreferencesRepository

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
