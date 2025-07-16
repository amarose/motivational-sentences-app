package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.ArchiveRepository

class CleanUpArchiveUseCaseImpl(
    private val archiveRepository: ArchiveRepository
) : CleanUpArchiveUseCase {
    override suspend fun invoke() {
        archiveRepository.cleanUpArchive()
    }
}
