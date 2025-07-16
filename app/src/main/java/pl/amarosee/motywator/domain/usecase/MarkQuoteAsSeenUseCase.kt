package pl.amarosee.motywator.domain.usecase

interface MarkQuoteAsSeenUseCase {
    suspend operator fun invoke(quoteId: Int)
}
