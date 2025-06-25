package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.data.repository.ArchiveRepository

class ArchiveQuoteUseCaseImpl(
    private val archiveRepository: ArchiveRepository
) : ArchiveQuoteUseCase {
    override suspend fun invoke(quote: Quote) {
        val archivedQuote = ArchivedQuote(
            quoteId = quote.id,
            text = quote.text,
            author = quote.author,
            timestamp = System.currentTimeMillis()
        )
        archiveRepository.archiveQuote(archivedQuote)
    }
}
