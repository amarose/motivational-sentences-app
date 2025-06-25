package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote

interface UpdateQuoteUseCase {
    suspend operator fun invoke(quote: Quote)
}
