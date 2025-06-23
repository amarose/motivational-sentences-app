package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository

class SetOnboardingCompletedUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : SetOnboardingCompletedUseCase {
    override suspend fun invoke(completed: Boolean) {
        userPreferencesRepository.setOnboardingCompleted(completed)
    }
}
