package com.example.motivationalsentencesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.motivationalsentencesapp.data.local.dao.QuoteDao
import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import com.example.motivationalsentencesapp.data.model.Quote

@Database(entities = [Quote::class, ArchivedQuote::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
        abstract fun quoteDao(): QuoteDao
    abstract fun archivedQuoteDao(): ArchivedQuoteDao
}
