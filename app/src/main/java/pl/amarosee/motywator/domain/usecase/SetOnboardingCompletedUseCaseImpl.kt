package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.UserPreferencesRepository

class SetOnboardingCompletedUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : SetOnboardingCompletedUseCase {
    override suspend fun invoke(completed: Boolean) {
        userPreferencesRepository.setOnboardingCompleted(completed)
    }
}
