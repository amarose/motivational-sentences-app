package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote

interface UpdateQuoteUseCase {
    suspend operator fun invoke(quote: Quote)
}
