package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.Quote

interface QuoteRepository {
    suspend fun getRandomQuote(): Quote
}
