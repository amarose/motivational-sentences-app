package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedBackgroundUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetSelectedBackgroundUseCase {
    override fun invoke(): Flow<Int> = userPreferencesRepository.selectedBackgroundResId
}
