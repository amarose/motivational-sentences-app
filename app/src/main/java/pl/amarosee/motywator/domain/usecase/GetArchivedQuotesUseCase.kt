package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.ArchivedQuote
import kotlinx.coroutines.flow.Flow

interface GetArchivedQuotesUseCase {
    operator fun invoke(): Flow<List<ArchivedQuote>>
}
