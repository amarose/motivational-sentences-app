package com.example.motivationalsentencesapp.data.model

data class NotificationPreferences(
    val notificationEnabled: Boolean,
    val notificationTimes: List<String>
)
