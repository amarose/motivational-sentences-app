package com.example.motivationalsentencesapp.domain.usecase

/**
 * Use case for formatting quote text for sharing.
 * Follows Single Responsibility principle by encapsulating share text formatting logic.
 */
interface ShareQuoteUseCase {
    /**
     * Formats a quote for sharing with proper formatting and promotional text.
     * @param quoteText The original quote text to share
     * @return Properly formatted text for sharing
     */
    operator fun invoke(quoteText: String): String
}
