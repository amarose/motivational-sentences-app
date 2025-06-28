package com.example.motivationalsentencesapp.domain.usecase

interface GetNextNotificationTimeUseCase {
    suspend operator fun invoke(): Long?
}
