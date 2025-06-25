package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote
import kotlinx.coroutines.flow.Flow

interface GetQuoteByIdUseCase {
    operator fun invoke(id: Int): Flow<Quote?>
}
