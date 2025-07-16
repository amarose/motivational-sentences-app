package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.UserPreferencesRepository

class UpdateSelectedBackgroundUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : UpdateSelectedBackgroundUseCase {
    override suspend fun invoke(backgroundResId: Int) {
        userPreferencesRepository.updateSelectedBackground(backgroundResId)
    }
}
