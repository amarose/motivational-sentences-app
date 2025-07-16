package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedBackgroundUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetSelectedBackgroundUseCase {
    override fun invoke(): Flow<Int> = userPreferencesRepository.selectedBackgroundResId
}
