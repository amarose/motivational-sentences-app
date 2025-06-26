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
        Quote(id = 1, text = "The only way to do great work is to love what you do."),
        Quote(id = 2, text = "Believe you can and you're halfway there."),
        Quote(id = 3, text = "The future belongs to those who believe in the beauty of their dreams."),
        Quote(id = 4, text = "Strive not to be a success, but rather to be of value."),
        Quote(id = 5, text = "The best way to predict the future is to create it."),
        Quote(id = 6, text = "Your time is limited, don't waste it living someone else's life."),
        Quote(id = 7, text = "The journey of a thousand miles begins with a single step."),
        Quote(id = 8, text = "It does not matter how slowly you go as long as you do not stop."),
        Quote(id = 9, text = "Everything you’ve ever wanted is on the other side of fear."),
        Quote(id = 10, text = "Success is not final, failure is not fatal: it is the courage to continue that counts.")
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
