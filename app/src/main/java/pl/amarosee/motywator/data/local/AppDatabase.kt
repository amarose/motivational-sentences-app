package pl.amarosee.motywator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import pl.amarosee.motywator.data.local.dao.ArchivedQuoteDao
import pl.amarosee.motywator.data.local.dao.QuoteDao
import pl.amarosee.motywator.data.model.ArchivedQuote
import pl.amarosee.motywator.data.model.Quote

@Database(entities = [Quote::class, ArchivedQuote::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quoteDao(): QuoteDao
    abstract fun archivedQuoteDao(): ArchivedQuoteDao
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE quotes ADD COLUMN wasDisplayed INTEGER NOT NULL DEFAULT 0")
    }
}
