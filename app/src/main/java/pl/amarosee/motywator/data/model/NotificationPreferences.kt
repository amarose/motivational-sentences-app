package pl.amarosee.motywator.data.model

data class NotificationPreferences(
    val notificationEnabled: Boolean,
    val notificationTimes: List<String>,
    val notificationQuantity: Int
)
