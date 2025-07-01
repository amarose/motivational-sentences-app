package com.example.motivationalsentencesapp.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.motivationalsentencesapp.MainActivity
import com.example.motivationalsentencesapp.R
import com.example.motivationalsentencesapp.domain.usecase.GetRandomQuoteUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val getRandomQuoteUseCase: GetRandomQuoteUseCase by inject()

    private val settingsDataStore: com.example.motivationalsentencesapp.data.datastore.SettingsDataStore by inject()

    override suspend fun doWork(): Result {
        return try {
            val quote = getRandomQuoteUseCase()
            settingsDataStore.saveCurrentQuoteId(quote.id)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(MainActivity.EXTRA_QUOTE_ID, quote.id)
                putExtra(MainActivity.EXTRA_QUOTE_TEXT, quote.text)
                putExtra(MainActivity.EXTRA_IS_FAVORITE, quote.isFavorite)
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context, quote.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notification = NotificationCompat.Builder(context, "motivational_quotes_channel")
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("Twoja codzienna dawka motywacji")
                .setContentText("\"${quote.text}\"")
                .setStyle(NotificationCompat.BigTextStyle().bigText("\"${quote.text}\""))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(Random.nextInt(), notification)

            Result.success()
        } catch (_: Exception) {
            Result.failure()
        }
    }
}
