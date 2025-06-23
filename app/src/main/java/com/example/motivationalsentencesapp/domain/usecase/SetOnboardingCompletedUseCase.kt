package com.example.motivationalsentencesapp.domain.usecase

interface SetOnboardingCompletedUseCase {
    suspend operator fun invoke(completed: Boolean)
}
