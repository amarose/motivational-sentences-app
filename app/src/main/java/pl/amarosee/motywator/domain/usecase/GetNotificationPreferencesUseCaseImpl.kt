package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.NotificationPreferences
import pl.amarosee.motywator.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetNotificationPreferencesUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetNotificationPreferencesUseCase {
    override fun invoke(): Flow<NotificationPreferences> = userPreferencesRepository.userPreferences
}
