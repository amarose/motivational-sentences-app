package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Quote
import kotlinx.coroutines.flow.Flow

interface GetFavoriteQuotesUseCase {
    operator fun invoke(): Flow<List<Quote>>
}
