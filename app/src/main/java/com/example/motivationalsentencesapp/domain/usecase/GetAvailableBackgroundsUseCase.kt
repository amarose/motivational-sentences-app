package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Background

interface GetAvailableBackgroundsUseCase {
    operator fun invoke(): List<Background>
}
