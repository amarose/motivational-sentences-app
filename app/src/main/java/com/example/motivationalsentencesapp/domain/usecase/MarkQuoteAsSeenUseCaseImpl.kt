package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.QuoteRepository

class MarkQuoteAsSeenUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : MarkQuoteAsSeenUseCase {
    override suspend fun invoke(quoteId: Int) {
        quoteRepository.markQuoteAsSeen(quoteId)
    }
}
