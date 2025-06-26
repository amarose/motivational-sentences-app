package com.example.motivationalsentencesapp.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetSelectedBackgroundUseCase {
    operator fun invoke(): Flow<Int>
}
