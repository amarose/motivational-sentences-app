package pl.amarosee.motywator.domain.usecase

interface UpdateNotificationPreferencesUseCase {
    suspend operator fun invoke(
        notificationEnabled: Boolean,
        notificationTimes: List<String>,
        notificationQuantity: Int
    )
}
