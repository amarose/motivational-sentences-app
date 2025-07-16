package pl.amarosee.motywator.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.amarosee.motywator.data.model.ArchivedQuote
import pl.amarosee.motywator.domain.usecase.CleanUpArchiveUseCase
import pl.amarosee.motywator.domain.usecase.GetArchivedQuotesUseCase
import pl.amarosee.motywator.domain.usecase.ShareQuoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class ArchiveViewEffect {
    data class ShareQuote(val text: String) : ArchiveViewEffect()
}

data class ArchiveUiState(
    val quotes: List<ArchivedQuote> = emptyList(),
    val isLoading: Boolean = true
)

class ArchiveViewModel(
    private val getArchivedQuotesUseCase: GetArchivedQuotesUseCase,
    private val cleanUpArchiveUseCase: CleanUpArchiveUseCase,
    private val shareQuoteUseCase: ShareQuoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ArchiveViewEffect>()
    val effect: SharedFlow<ArchiveViewEffect> = _effect.asSharedFlow()

    init {
        loadArchivedQuotes()
        cleanUpArchive()
    }

    private fun loadArchivedQuotes() {
        getArchivedQuotesUseCase()
            .onEach { archivedQuotes ->
                _uiState.value = ArchiveUiState(quotes = archivedQuotes, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    fun onShareClicked(quoteText: String) {
        viewModelScope.launch {
            val shareText = shareQuoteUseCase(quoteText)
            _effect.emit(ArchiveViewEffect.ShareQuote(shareText))
        }
    }

    private fun cleanUpArchive() {
        viewModelScope.launch {
            cleanUpArchiveUseCase()
        }
    }
}
