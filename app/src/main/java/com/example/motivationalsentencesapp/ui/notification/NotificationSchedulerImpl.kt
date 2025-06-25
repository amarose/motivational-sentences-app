package com.example.motivationalsentencesapp.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.motivationalsentencesapp.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import java.util.Calendar

class NotificationSchedulerImpl(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) : NotificationScheduler {

    companion object {
        private const val TAG = "NotificationScheduler"
        private const val MAX_NOTIFICATIONS = 3 // Max possible notifications
    }

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override suspend fun reschedule() {
        val preferences = userPreferencesRepository.userPreferences.first()
        Log.d(TAG, "Rescheduling alarms with preferences: $preferences")

        // Always unschedule first to clear existing alarms.
        unschedule()

        if (!preferences.notificationEnabled) {
            Log.d(TAG, "Notifications are disabled. No alarms will be set.")
            return
        }

        preferences.notificationTimes.forEachIndexed { index, time ->
            try {
                val (hour, minute) = time.split(":").map { it.toInt() }

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    // If the time is in the past, schedule it for the next day.
                    if (this.timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.DATE, 1)
                    }
                }

                val intent = Intent(context, NotificationReceiver::class.java).apply {
                    action = NotificationReceiver.ACTION_RECEIVE_NOTIFICATION
                    // Use index as a unique request code for each PendingIntent
                    putExtra(NotificationReceiver.EXTRA_REQUEST_CODE, index)
                }

                Log.d(TAG, "Scheduling alarm for index $index at ${calendar.timeInMillis} ($time)")
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    index,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    Log.w(TAG, "Cannot schedule exact alarms. Permission not granted.")
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                    Log.d(TAG, "Alarm successfully set for index $index.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to parse time string: $time or schedule alarm.", e)
            }
        }
    }

    override suspend fun unschedule() {
        Log.d(TAG, "Unscheduling all alarms (up to max $MAX_NOTIFICATIONS).")
        for (i in 0 until MAX_NOTIFICATIONS) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                action = NotificationReceiver.ACTION_RECEIVE_NOTIFICATION
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                i,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
                Log.d(TAG, "Canceled alarm with request code: $i")
            }
        }
    }
}
