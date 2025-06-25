package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.model.Quote

interface ArchiveQuoteUseCase {
    suspend operator fun invoke(quote: Quote)
}
