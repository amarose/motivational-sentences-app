package com.example.motivationalsentencesapp.data.repository

import com.example.motivationalsentencesapp.data.model.Quote

class QuoteRepositoryImpl : QuoteRepository {

    private val quotes = listOf(
        Quote("The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote("Believe you can and you're halfway there.", "Theodore Roosevelt"),
        Quote("The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt"),
        Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius"),
        Quote("Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill"),
        Quote("Strive not to be a success, but rather to be of value.", "Albert Einstein"),
        Quote("The mind is everything. What you think you become.", "Buddha"),
        Quote("Your time is limited, don't waste it living someone else's life.", "Steve Jobs"),
        Quote("The best way to predict the future is to create it.", "Peter Drucker"),
        Quote("You miss 100% of the shots you don't take.", "Wayne Gretzky")
    )

    override suspend fun getRandomQuote(): Quote {
        return quotes.random()
    }
}
