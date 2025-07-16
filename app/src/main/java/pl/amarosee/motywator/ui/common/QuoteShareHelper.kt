package pl.amarosee.motywator.ui.common

import android.content.Context
import android.content.Intent

/**
 * Helper class for sharing quotes.
 * Centralizes the share functionality to avoid duplication across screens.
 * Follows Single Responsibility principle.
 */
object QuoteShareHelper {
    private const val EMAIL_SUBJECT = "Motywator - Twoja codzienna dawka motywacji"
    
    /**
     * Shares the given text using Android's share intent.
     *
     * @param context Context to use for starting the activity
     * @param text Text content to share
     */
    fun shareQuote(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT)
            type = "text/plain"
        }
        
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}
