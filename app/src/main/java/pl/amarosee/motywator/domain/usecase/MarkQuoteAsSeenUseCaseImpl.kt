package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.QuoteRepository

class MarkQuoteAsSeenUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : MarkQuoteAsSeenUseCase {
    override suspend fun invoke(quoteId: Int) {
        quoteRepository.markQuoteAsSeen(quoteId)
    }
}
