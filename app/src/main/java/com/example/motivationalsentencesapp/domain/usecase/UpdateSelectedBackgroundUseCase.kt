package com.example.motivationalsentencesapp.domain.usecase

interface UpdateSelectedBackgroundUseCase {
    suspend operator fun invoke(backgroundResId: Int)
}
