package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository

class UpdateSelectedBackgroundUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : UpdateSelectedBackgroundUseCase {
    override suspend fun invoke(backgroundResId: Int) {
        userPreferencesRepository.updateSelectedBackground(backgroundResId)
    }
}
