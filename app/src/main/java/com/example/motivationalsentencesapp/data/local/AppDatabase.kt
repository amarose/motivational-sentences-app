package com.example.motivationalsentencesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.motivationalsentencesapp.data.model.ArchivedQuote

@Database(entities = [ArchivedQuote::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun archivedQuoteDao(): ArchivedQuoteDao
}
