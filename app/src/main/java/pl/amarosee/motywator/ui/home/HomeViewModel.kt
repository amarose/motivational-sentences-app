package pl.amarosee.motywator.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.amarosee.motywator.data.datastore.SettingsDataStore
import pl.amarosee.motywator.data.model.Quote
import pl.amarosee.motywator.domain.usecase.ArchiveQuoteUseCase
import pl.amarosee.motywator.domain.usecase.MarkQuoteAsSeenUseCase
import pl.amarosee.motywator.domain.usecase.GetRandomQuoteUseCase
import pl.amarosee.motywator.domain.usecase.GetQuoteByIdUseCase
import pl.amarosee.motywator.domain.usecase.GetSelectedBackgroundUseCase
import pl.amarosee.motywator.domain.usecase.ShareQuoteUseCase
import pl.amarosee.motywator.domain.usecase.UpdateQuoteUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

sealed class HomeViewEffect {
    data class ShareQuote(val text: String) : HomeViewEffect()
}

data class HomeUiState(
    val quote: Quote? = null,
    val backgroundResId: Int? = null,
    val textColor: Int = android.graphics.Color.WHITE,

    val isLoading: Boolean = true
)


class HomeViewModel(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val updateQuoteUseCase: UpdateQuoteUseCase,
    private val getQuoteByIdUseCase: GetQuoteByIdUseCase,
    private val archiveQuoteUseCase: ArchiveQuoteUseCase,
    private val markQuoteAsSeenUseCase: MarkQuoteAsSeenUseCase,
    private val getSelectedBackgroundUseCase: GetSelectedBackgroundUseCase,
    private val shareQuoteUseCase: ShareQuoteUseCase,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeViewEffect>()
    val effect = _effect.asSharedFlow()

    private var initialized = false

    init {
        observeSelectedBackground()
        observeTextColor()
    }

        fun initialize() {
            if (initialized) return
            initialized = true
            observeCurrentQuote()
        }

        private fun observeSelectedBackground() {
            getSelectedBackgroundUseCase()
                .onEach { resId ->
                    _uiState.value = _uiState.value.copy(backgroundResId = resId)
                }
                .launchIn(viewModelScope)
        }

        fun onToggleFavorite(quote: Quote) {
            viewModelScope.launch {
                val updatedQuote = quote.copy(isFavorite = !quote.isFavorite)
                updateQuoteUseCase(updatedQuote)
                _uiState.value = _uiState.value.copy(quote = updatedQuote)
            }
        }

        fun onShareClicked(quoteText: String) {
            viewModelScope.launch {
                val shareText = shareQuoteUseCase(quoteText)
                _effect.emit(HomeViewEffect.ShareQuote(shareText))
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private fun observeCurrentQuote() {
            settingsDataStore.currentQuoteIdFlow
                .distinctUntilChanged()
                .flatMapLatest { quoteId ->
                    if (quoteId != null) {
                        getQuoteByIdUseCase(quoteId)
                    } else {
                        flow { emit(getRandomQuoteUseCase()) }
                    }
                }
                .onEach { quote ->
                    _uiState.value = _uiState.value.copy(quote = quote, isLoading = false)
                    quote?.let {
                        if (settingsDataStore.currentQuoteIdFlow.first() == null) {
                            settingsDataStore.saveCurrentQuoteId(it.id)
                        }
                        markQuoteAsSeenUseCase(it.id)
                        archiveQuoteUseCase(it)
                    }
                }
                .launchIn(viewModelScope)
        }

        private fun observeTextColor() {
            settingsDataStore.textColorFlow
                .onEach { color ->
                    _uiState.value = _uiState.value.copy(textColor = color)
                }
                .launchIn(viewModelScope)
        }

    }

