package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote
import pl.amarosee.motywator.data.repository.QuoteRepository
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
