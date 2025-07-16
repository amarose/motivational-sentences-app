package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.model.Background

interface GetAvailableBackgroundsUseCase {
    operator fun invoke(): List<Background>
}
