package com.example.motivationalsentencesapp.worker

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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

    override suspend fun doWork(): Result {
        return try {
            val quote = getRandomQuoteUseCase()
            quote.let {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val notification = NotificationCompat.Builder(context, "motivational_quotes_channel")
                    .setSmallIcon(R.drawable.background_1)
                    .setContentTitle("Twoja codzienna dawka motywacji")
                    .setContentText("\"${it.text}\"")
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("\"${it.text}\""))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(Random.nextInt(), notification)
            }
            Result.success()
        } catch (_: Exception) {
            Result.failure()
        }
    }
}
