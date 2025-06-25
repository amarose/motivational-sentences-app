package com.example.motivationalsentencesapp.ui.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.example.motivationalsentencesapp.domain.usecase.GetRandomQuoteUseCase
import kotlinx.coroutines.CoroutineScope
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationReceiver : BroadcastReceiver(), KoinComponent {

    companion object {
        private const val TAG = "NotificationReceiver"

        const val EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE"
        const val ACTION_RECEIVE_NOTIFICATION = "com.example.motivationalsentencesapp.ACTION_RECEIVE_NOTIFICATION"
    }

    private val getRandomQuoteUseCase: GetRandomQuoteUseCase by inject()
    private val notificationProvider: NotificationProvider by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult: PendingResult = goAsync()
        Log.d(TAG, "Notification received on thread: ${Thread.currentThread().name}")

        val requestCode = intent.getIntExtra(EXTRA_REQUEST_CODE, 0)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val quote = getRandomQuoteUseCase()
                Log.d(TAG, "Quote fetched: ${quote.text}")
                notificationProvider.showNotification(quote)

                // Reschedule all alarms for the next cycle
                Log.d(TAG, "Rescheduling alarms for the next cycle.")
                notificationScheduler.reschedule()
            } catch (e: Exception) {
                Log.e(TAG, "Error during notification processing", e)
            } finally {
                // Always call finish() when done
                pendingResult.finish()
            }
        }
    }
}
