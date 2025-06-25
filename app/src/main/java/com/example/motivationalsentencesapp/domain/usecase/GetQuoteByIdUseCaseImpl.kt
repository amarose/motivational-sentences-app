package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.data.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetQuoteByIdUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : GetQuoteByIdUseCase {
    override fun invoke(id: Int): Flow<Quote?> {
        return quoteRepository.quotes.map { quotes ->
            quotes.find { it.id == id }
        }
    }
}
