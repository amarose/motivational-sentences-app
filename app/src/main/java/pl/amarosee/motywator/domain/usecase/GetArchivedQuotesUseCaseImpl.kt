package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.ArchivedQuote
import pl.amarosee.motywator.data.repository.ArchiveRepository
import kotlinx.coroutines.flow.Flow

class GetArchivedQuotesUseCaseImpl(
    private val archiveRepository: ArchiveRepository
) : GetArchivedQuotesUseCase {
    override fun invoke(): Flow<List<ArchivedQuote>> {
        return archiveRepository.getArchivedQuotes()
    }
}
