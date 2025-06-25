package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.ArchiveRepository

class CleanUpArchiveUseCaseImpl(
    private val archiveRepository: ArchiveRepository
) : CleanUpArchiveUseCase {
    override suspend fun invoke() {
        archiveRepository.cleanUpArchive()
    }
}
