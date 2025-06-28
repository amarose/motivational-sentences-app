package com.example.motivationalsentencesapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.data.datastore.SettingsDataStore
import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.domain.usecase.ArchiveQuoteUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetQuoteByIdUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetRandomQuoteUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetNextNotificationTimeUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetNotificationPreferencesUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetSelectedBackgroundUseCase
import com.example.motivationalsentencesapp.domain.usecase.UpdateQuoteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class HomeViewEffect {
    data class ShareQuote(val text: String) : HomeViewEffect()
}

data class HomeUiState(
    val quote: Quote? = null,
    val backgroundResId: Int? = null,
    val textColor: Int = android.graphics.Color.WHITE,
    val nextNotificationTime: Long? = null,
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val updateQuoteUseCase: UpdateQuoteUseCase,
    private val getQuoteByIdUseCase: GetQuoteByIdUseCase,
    private val archiveQuoteUseCase: ArchiveQuoteUseCase,
    private val getSelectedBackgroundUseCase: GetSelectedBackgroundUseCase,
    private val settingsDataStore: SettingsDataStore,
    private val getNextNotificationTimeUseCase: GetNextNotificationTimeUseCase,
    private val getNotificationPreferencesUseCase: GetNotificationPreferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeViewEffect>()
    val effect = _effect.asSharedFlow()

    private var quoteJob: Job? = null
    private var initialized = false

    init {
        observeSelectedBackground()
        observeTextColor()
        observeNotificationPreferences()
    }

    fun initialize(quoteId: Int, quoteText: String?, isFavorite: Boolean) {
        if (initialized) return
        initialized = true

        if (quoteId != -1 && quoteText != null) {
            val quote = Quote(id = quoteId, text = quoteText, isFavorite = isFavorite)
            _uiState.value = uiState.value.copy(quote = quote, isLoading = false)
            observeQuote(quoteId)
        }
    }

    fun loadRandomQuote() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, quote = null)
            val randomQuote = getRandomQuoteUseCase()
            observeQuote(randomQuote.id)
        }
    }

    private fun observeQuote(id: Int) {
        quoteJob?.cancel()
        quoteJob = getQuoteByIdUseCase(id)
            .onEach { quote ->
                quote?.let {
                    _uiState.value = uiState.value.copy(quote = it, isLoading = false)
                    archiveQuoteUseCase(it)
                }
            }
            .launchIn(viewModelScope)
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

    fun onShareClicked() {
        viewModelScope.launch {
            _uiState.value.quote?.let {
                val quoteText = "\"${it.text}\""
                val promoText =
                    "Pobierz tą aplikację zupełnie za darmo i czerp z niej motywację do działania - 'TODO wkleić link do aplikacji jak juz bedzie na play store'"
                val shareText = "$quoteText\n\n$promoText"
                _effect.emit(HomeViewEffect.ShareQuote(shareText))
            }
        }
    }

    private fun observeNotificationPreferences() {
        getNotificationPreferencesUseCase()
            .onEach {
                loadNextNotificationTime()
            }
            .launchIn(viewModelScope)
    }

    private fun loadNextNotificationTime() {
        viewModelScope.launch {
            val nextTime = getNextNotificationTimeUseCase()
            _uiState.value = _uiState.value.copy(nextNotificationTime = nextTime, isLoading = false)
        }
    }

    private fun observeTextColor() {
        settingsDataStore.textColorFlow
            .onEach { color ->
                _uiState.value = _uiState.value.copy(textColor = color)
            }
            .launchIn(viewModelScope)
    }

}
