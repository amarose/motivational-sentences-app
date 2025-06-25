package com.example.motivationalsentencesapp.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.motivationalsentencesapp.domain.usecase.GetNotificationPreferencesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootCompletedReceiver : BroadcastReceiver(), KoinComponent {

    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val preferences = getNotificationPreferencesUseCase().first()
                    if (preferences.notificationEnabled) {
                        notificationScheduler.reschedule()
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
