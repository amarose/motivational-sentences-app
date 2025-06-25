package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface QuoteRepository {
    val quotes: StateFlow<List<Quote>>
    suspend fun getRandomQuote(): Quote
    suspend fun updateQuote(quote: Quote)
    fun getFavoriteQuotes(): Flow<List<Quote>>
}
