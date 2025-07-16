package pl.amarosee.motywator.data.repository

import pl.amarosee.motywator.data.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    val quotes: Flow<List<Quote>>
    suspend fun getRandomQuote(): Quote
    suspend fun updateQuote(quote: Quote)
    fun getFavoriteQuotes(): Flow<List<Quote>>
    suspend fun getRandomUnseenQuote(): Quote?
    suspend fun markQuoteAsSeen(quoteId: Int)
    suspend fun resetAllQuotesToUnseen()
}
