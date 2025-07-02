package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.data.repository.QuoteRepository

class GetRandomQuoteUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : GetRandomQuoteUseCase {
    override suspend fun invoke(): Quote {
        var unseenQuote = quoteRepository.getRandomUnseenQuote()

        if (unseenQuote == null) {
            quoteRepository.resetAllQuotesToUnseen()
            unseenQuote = quoteRepository.getRandomUnseenQuote()
        }

        val quote = unseenQuote ?: quoteRepository.getRandomQuote()
        quoteRepository.markQuoteAsSeen(quote.id)
        return quote
    }
}
