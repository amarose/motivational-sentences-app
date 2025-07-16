package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.ArchivedQuote
import pl.amarosee.motywator.data.model.Quote
import pl.amarosee.motywator.data.repository.ArchiveRepository

class ArchiveQuoteUseCaseImpl(
    private val archiveRepository: ArchiveRepository
) : ArchiveQuoteUseCase {
    override suspend fun invoke(quote: Quote) {
        val archivedQuote = ArchivedQuote(
            quoteId = quote.id,
            text = quote.text,
            timestamp = System.currentTimeMillis()
        )
        archiveRepository.archiveQuote(archivedQuote)
    }
}
