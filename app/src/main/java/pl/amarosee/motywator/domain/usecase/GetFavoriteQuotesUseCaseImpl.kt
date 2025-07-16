package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote
import pl.amarosee.motywator.data.repository.QuoteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteQuotesUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : GetFavoriteQuotesUseCase {
    override fun invoke(): Flow<List<Quote>> {
        return quoteRepository.getFavoriteQuotes()
    }
}
