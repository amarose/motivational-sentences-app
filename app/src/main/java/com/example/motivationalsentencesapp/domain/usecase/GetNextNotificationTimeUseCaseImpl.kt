package com.example.motivationalsentencesapp.domain.usecase

import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DateTimeUnit

class GetNextNotificationTimeUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetNextNotificationTimeUseCase {

    override suspend fun invoke(): Long? {
        val preferences = userPreferencesRepository.userPreferences.first()
        if (!preferences.notificationEnabled || preferences.notificationTimes.isEmpty()) {
            return null
        }

        val now = Clock.System.now()
        val systemTZ = TimeZone.currentSystemDefault()
        val nowDateTime = now.toLocalDateTime(systemTZ)

        return preferences.notificationTimes
            .mapNotNull { timeString ->
                try {
                    val (hour, minute) = timeString.split(":").map { it.toInt() }
                    val todayNotificationTime = nowDateTime.date.atTime(hour, minute)

                    val nextNotificationDateTime = if (todayNotificationTime < nowDateTime) {
                        // If the time has already passed today, schedule for tomorrow
                        nowDateTime.date.plus(1, DateTimeUnit.DAY).atTime(hour, minute)
                    } else {
                        // Otherwise, schedule for today
                        todayNotificationTime
                    }
                    nextNotificationDateTime.toInstant(systemTZ).toEpochMilliseconds()
                } catch (e: Exception) {
                    // Handle malformed time string
                    null
                }
            }
            .minOrNull()
    }
}
