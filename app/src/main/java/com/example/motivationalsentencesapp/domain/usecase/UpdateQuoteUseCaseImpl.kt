package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.data.repository.QuoteRepository

class UpdateQuoteUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : UpdateQuoteUseCase {
    override suspend fun invoke(quote: Quote) {
        quoteRepository.updateQuote(quote)
    }
}
