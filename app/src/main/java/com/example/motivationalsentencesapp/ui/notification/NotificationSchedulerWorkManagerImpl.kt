package com.example.motivationalsentencesapp.ui.notification

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import com.example.motivationalsentencesapp.worker.NotificationWorker
import kotlinx.coroutines.flow.first
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationSchedulerWorkManagerImpl(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) : NotificationScheduler {

    private val workManager = WorkManager.getInstance(context)

    companion object {
        private const val TAG = "NotificationSchedulerWM"
        const val NOTIFICATION_WORK_TAG_PREFIX = "notification_work_"
        private const val MAX_NOTIFICATIONS = 3 // Align with settings
    }

    override suspend fun reschedule() {
        val preferences = userPreferencesRepository.userPreferences.first()
        Log.d(TAG, "Rescheduling work with preferences: $preferences")

        // Always unschedule first to clear existing work.
        unschedule()

        if (!preferences.notificationEnabled) {
            Log.d(TAG, "Notifications are disabled. No work will be scheduled.")
            return
        }

        preferences.notificationTimes.forEachIndexed { index, time ->
            try {
                val (hour, minute) = time.split(":").map { it.toInt() }
                val now = Calendar.getInstance()
                val scheduledTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // If the scheduled time is in the past, schedule it for the next day.
                if (scheduledTime.before(now)) {
                    scheduledTime.add(Calendar.DATE, 1)
                }

                val initialDelay = scheduledTime.timeInMillis - now.timeInMillis

                val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                    24, TimeUnit.HOURS
                )
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .build()

                val uniqueWorkName = "${NOTIFICATION_WORK_TAG_PREFIX}$index"
                workManager.enqueueUniquePeriodicWork(
                    uniqueWorkName,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )

                Log.d(TAG, "Scheduled work '$uniqueWorkName' with initial delay of $initialDelay ms.")

            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse time string or schedule work for time: $time", e)
            }
        }
    }

    override suspend fun unschedule() {
        Log.d(TAG, "Unscheduling all notification work.")
        for (i in 0 until MAX_NOTIFICATIONS) {
            val uniqueWorkName = "${NOTIFICATION_WORK_TAG_PREFIX}$i"
            workManager.cancelUniqueWork(uniqueWorkName)
            Log.d(TAG, "Cancelled work: $uniqueWorkName")
        }
    }
}
