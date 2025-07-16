package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.QuoteRepository

class ResetAllQuotesToUnseenUseCaseImpl(
    private val quoteRepository: QuoteRepository
) : ResetAllQuotesToUnseenUseCase {
    override suspend fun invoke() {
        quoteRepository.resetAllQuotesToUnseen()
    }
}
