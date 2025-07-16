package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow

interface GetNotificationPreferencesUseCase {
    operator fun invoke(): Flow<NotificationPreferences>
}
