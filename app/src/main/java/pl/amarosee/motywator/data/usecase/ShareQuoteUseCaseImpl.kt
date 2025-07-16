package pl.amarosee.motywator.data.usecase

import pl.amarosee.motywator.domain.usecase.ShareQuoteUseCase

/**
 * Implementation of [ShareQuoteUseCase] that provides formatted share text.
 * Keeps all share text formatting logic in a single place for maintainability.
 */
class ShareQuoteUseCaseImpl : ShareQuoteUseCase {
    
    /**
     * Creates a properly formatted share text from a quote.
     * Adds quotation marks and promotional text.
     */
    override operator fun invoke(quoteText: String): String {
        val quoteTextWithQuotes = "\"$quoteText\""
        val promoText = "Pobierz tą aplikację zupełnie za darmo i czerp z niej motywację do działania"
        return "$quoteTextWithQuotes\n\n$promoText"
    }
}
