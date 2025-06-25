package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.data.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteQuotesUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : GetFavoriteQuotesUseCase {
    override fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getFavoriteQuotes()
    }
}
