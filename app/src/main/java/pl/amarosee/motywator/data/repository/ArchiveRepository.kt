package pl.amarosee.motywator.data.repository

import pl.amarosee.motywator.data.model.ArchivedQuote
import kotlinx.coroutines.flow.Flow

interface ArchiveRepository {
    fun getArchivedQuotes(): Flow<List<ArchivedQuote>>
    suspend fun archiveQuote(quote: ArchivedQuote)
    suspend fun cleanUpArchive()
}
