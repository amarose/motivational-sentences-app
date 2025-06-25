package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import com.example.motivationalsentencesapp.data.repository.ArchiveRepository
import kotlinx.coroutines.flow.Flow

class GetArchivedQuotesUseCaseImpl(
    private val archiveRepository: ArchiveRepository
) : GetArchivedQuotesUseCase {
    override fun invoke(): Flow<List<ArchivedQuote>> {
        return archiveRepository.getArchivedQuotes()
    }
}
