package pl.amarosee.motywator.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.amarosee.motywator.data.model.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes")
    fun getAllQuotes(): Flow<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quotes: List<Quote>)

    @Update
    suspend fun updateQuote(quote: Quote)

    @Query("SELECT COUNT(*) FROM quotes")
    suspend fun count(): Int

    @Query("SELECT * FROM quotes WHERE wasDisplayed = 0 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomUnseenQuote(): Quote?

    @Query("UPDATE quotes SET wasDisplayed = 1 WHERE id = :quoteId")
    suspend fun markQuoteAsSeen(quoteId: Int)

    @Query("UPDATE quotes SET wasDisplayed = 0")
    suspend fun resetAllQuotesToUnseen()
}
