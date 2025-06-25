package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote

interface GetRandomQuoteUseCase {
    suspend operator fun invoke(): Quote
}
