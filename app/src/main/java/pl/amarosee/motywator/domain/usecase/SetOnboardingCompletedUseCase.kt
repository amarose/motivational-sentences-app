package pl.amarosee.motywator.domain.usecase

interface SetOnboardingCompletedUseCase {
    suspend operator fun invoke(completed: Boolean)
}
