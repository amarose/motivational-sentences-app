package com.example.motivationalsentencesapp.domain.usecase

interface MarkQuoteAsSeenUseCase {
    suspend operator fun invoke(quoteId: Int)
}
