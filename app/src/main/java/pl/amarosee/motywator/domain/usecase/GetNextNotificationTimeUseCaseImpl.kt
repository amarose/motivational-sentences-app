package pl.amarosee.motywator.domain.usecase

import pl.amarosee.motywator.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DateTimeUnit
import kotlin.time.ExperimentalTime

class GetNextNotificationTimeUseCaseImpl(
    private val userPreferencesRepository: UserPreferencesRepository
) : GetNextNotificationTimeUseCase {

    @OptIn(ExperimentalTime::class)
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
                } catch (_: Exception) {
                    // Handle malformed time string
                    null
                }
            }
            .minOrNull()
    }
}
