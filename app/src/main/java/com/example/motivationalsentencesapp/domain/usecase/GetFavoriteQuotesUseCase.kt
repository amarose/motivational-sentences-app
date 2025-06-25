package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote
import kotlinx.coroutines.flow.Flow

interface GetFavoriteQuotesUseCase {
    operator fun invoke(): Flow<List<Quote>>
}
