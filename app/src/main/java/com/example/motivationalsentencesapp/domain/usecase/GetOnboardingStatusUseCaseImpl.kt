package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetOnboardingStatusUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetOnboardingStatusUseCase {
    override fun invoke(): Flow<Boolean> = userPreferencesRepository.isOnboardingCompleted()
}
