package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote

interface GetRandomQuoteUseCase {
    suspend operator fun invoke(): Quote
}
