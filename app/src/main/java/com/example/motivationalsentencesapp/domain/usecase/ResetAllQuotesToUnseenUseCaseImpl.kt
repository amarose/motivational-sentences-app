package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.QuoteRepository

class ResetAllQuotesToUnseenUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : ResetAllQuotesToUnseenUseCase {
    override suspend fun invoke() {
        quoteRepository.resetAllQuotesToUnseen()
    }
}
