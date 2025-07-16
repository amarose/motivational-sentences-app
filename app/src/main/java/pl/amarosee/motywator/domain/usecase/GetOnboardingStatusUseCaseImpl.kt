package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetOnboardingStatusUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetOnboardingStatusUseCase {
    override fun invoke(): Flow<Boolean> = userPreferencesRepository.isOnboardingCompleted()
}
