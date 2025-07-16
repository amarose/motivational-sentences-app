package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote
import pl.amarosee.motywator.data.repository.QuoteRepository

class UpdateQuoteUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : UpdateQuoteUseCase {
    override suspend fun invoke(quote: Quote) {
        quoteRepository.updateQuote(quote)
    }
}
