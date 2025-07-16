package pl.amarosee.motywator.ui.notification

interface NotificationScheduler {
    suspend fun reschedule()
    suspend fun unschedule()
}
