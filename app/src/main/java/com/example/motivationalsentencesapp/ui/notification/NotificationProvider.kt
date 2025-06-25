package com.example.motivationalsentencesapp.ui.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.motivationalsentencesapp.MainActivity
import com.example.motivationalsentencesapp.R
import com.example.motivationalsentencesapp.data.model.Quote

class NotificationProvider(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(quote: Quote) {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(MainActivity.EXTRA_QUOTE_ID, quote.id)
            putExtra(MainActivity.EXTRA_QUOTE_TEXT, quote.text)
            putExtra(MainActivity.EXTRA_QUOTE_AUTHOR, quote.author)
            putExtra(MainActivity.EXTRA_IS_FAVORITE, quote.isFavorite)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(quote.author)
            .setContentText(quote.text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    companion object {
        const val CHANNEL_ID = "motivational_quotes_channel"
    }
}
