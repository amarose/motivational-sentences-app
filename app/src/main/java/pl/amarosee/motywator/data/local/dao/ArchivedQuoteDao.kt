package pl.amarosee.motywator.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.amarosee.motywator.data.model.ArchivedQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface ArchivedQuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateQuote(archivedQuote: ArchivedQuote): Long

    @Query("SELECT * FROM archived_quotes ORDER BY timestamp DESC")
    fun getArchivedQuotes(): Flow<List<ArchivedQuote>>

    @Query("DELETE FROM archived_quotes WHERE timestamp < :thirtyDaysAgo")
    suspend fun deleteQuotesOlderThan(thirtyDaysAgo: Long): Int

}
