package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote
import kotlinx.coroutines.flow.Flow

interface GetQuoteByIdUseCase {
    operator fun invoke(id: Int): Flow<Quote?>
}
