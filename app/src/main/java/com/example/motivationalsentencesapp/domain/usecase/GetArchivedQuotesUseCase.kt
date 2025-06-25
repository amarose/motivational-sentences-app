package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import kotlinx.coroutines.flow.Flow

interface GetArchivedQuotesUseCase {
    operator fun invoke(): Flow<List<ArchivedQuote>>
}
