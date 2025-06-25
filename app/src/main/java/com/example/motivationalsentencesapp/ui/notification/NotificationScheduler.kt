package com.example.motivationalsentencesapp.ui.notification

interface NotificationScheduler {
    suspend fun reschedule()
    suspend fun unschedule()
}
