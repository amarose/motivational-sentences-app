package pl.amarosee.motywator.data.repository

import pl.amarosee.motywator.data.local.dao.ArchivedQuoteDao
import pl.amarosee.motywator.data.model.ArchivedQuote
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class ArchiveRepositoryImpl(
    private val archivedQuoteDao: ArchivedQuoteDao
) : ArchiveRepository {

    override fun getArchivedQuotes(): Flow<List<ArchivedQuote>> {
        return archivedQuoteDao.getArchivedQuotes()
    }

    override suspend fun archiveQuote(quote: ArchivedQuote) {
        archivedQuoteDao.insertOrUpdateQuote(quote)
    }

    override suspend fun cleanUpArchive() {
        val thirtyDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        archivedQuoteDao.deleteQuotesOlderThan(thirtyDaysAgo)
    }
}
