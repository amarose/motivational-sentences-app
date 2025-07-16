package pl.amarosee.motywator.domain.usecase

interface UpdateSelectedBackgroundUseCase {
    suspend operator fun invoke(backgroundResId: Int)
}
