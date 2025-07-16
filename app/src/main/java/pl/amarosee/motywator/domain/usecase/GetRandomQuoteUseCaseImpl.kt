package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote
import pl.amarosee.motywator.data.repository.QuoteRepository

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
