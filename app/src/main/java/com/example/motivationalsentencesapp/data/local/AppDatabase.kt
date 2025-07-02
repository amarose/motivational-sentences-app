package com.example.motivationalsentencesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.motivationalsentencesapp.data.local.dao.ArchivedQuoteDao
import com.example.motivationalsentencesapp.data.local.dao.QuoteDao
import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import com.example.motivationalsentencesapp.data.model.Quote

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
