package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import kotlinx.coroutines.flow.Flow

interface ArchiveRepository {
    fun getArchivedQuotes(): Flow<List<ArchivedQuote>>
    suspend fun archiveQuote(quote: ArchivedQuote)
    suspend fun cleanUpArchive()
}
