package pl.amarosee.motywator.domain.usecase

interface GetNextNotificationTimeUseCase {
    suspend operator fun invoke(): Long?
}
