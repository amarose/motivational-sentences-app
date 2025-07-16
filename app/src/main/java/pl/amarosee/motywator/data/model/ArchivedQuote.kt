package pl.amarosee.motywator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archived_quotes")
data class ArchivedQuote(
    @PrimaryKey val quoteId: Int,
    val text: String,
    val timestamp: Long
)
