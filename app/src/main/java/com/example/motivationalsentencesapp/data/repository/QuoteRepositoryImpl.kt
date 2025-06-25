package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class QuoteRepositoryImpl : QuoteRepository {

    private val initialQuotes = listOf(
        Quote(id = 1, text = "The only way to do great work is to love what you do.", author = "Steve Jobs"),
        Quote(id = 2, text = "Believe you can and you're halfway there.", author = "Theodore Roosevelt"),
        Quote(id = 3, text = "The future belongs to those who believe in the beauty of their dreams.", author = "Eleanor Roosevelt"),
        Quote(id = 4, text = "It does not matter how slowly you go as long as you do not stop.", author = "Confucius"),
        Quote(id = 5, text = "Success is not final, failure is not fatal: it is the courage to continue that counts.", author = "Winston Churchill"),
        Quote(id = 6, text = "Strive not to be a success, but rather to be of value.", author = "Albert Einstein"),
        Quote(id = 7, text = "The mind is everything. What you think you become.", author = "Buddha"),
        Quote(id = 8, text = "Your time is limited, don't waste it living someone else's life.", author = "Steve Jobs"),
        Quote(id = 9, text = "The best way to predict the future is to create it.", author = "Peter Drucker"),
        Quote(id = 10, text = "You miss 100% of the shots you don't take.", author = "Wayne Gretzky")
    )

    private val _quotes = MutableStateFlow(initialQuotes)

    override val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    override suspend fun getRandomQuote(): Quote {
        return _quotes.value.random()
    }

    override suspend fun updateQuote(quote: Quote) {
        _quotes.update { currentQuotes ->
            currentQuotes.map {
                if (it.id == quote.id) quote else it
            }
        }
    }

    override fun getFavoriteQuotes(): Flow<List<Quote>> {
        return _quotes.map { quotes ->
            quotes.filter { it.isFavorite }
        }
    }
}
