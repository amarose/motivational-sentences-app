package pl.amarosee.motywator.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetOnboardingStatusUseCase {
    operator fun invoke(): Flow<Boolean>
}
