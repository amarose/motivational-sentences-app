package com.example.motivationalsentencesapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.ui.navigation.Routes
import com.example.motivationalsentencesapp.domain.usecase.GetQuoteByIdUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetRandomQuoteUseCase
import com.example.motivationalsentencesapp.domain.usecase.UpdateQuoteUseCase
import com.example.motivationalsentencesapp.ui.notification.NotificationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class HomeUiState(
    val quote: Quote? = null
)

class HomeViewModel(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val updateQuoteUseCase: UpdateQuoteUseCase,
    private val getQuoteByIdUseCase: GetQuoteByIdUseCase,
    private val notificationProvider: NotificationProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var quoteJob: Job? = null

    init {
        val quoteId = savedStateHandle.get<Int>(Routes.Home.ARG_QUOTE_ID) ?: -1

        if (quoteId != -1) {
            observeQuote(quoteId)
        } else {
            loadRandomQuote()
        }
    }

    fun loadRandomQuote() {
        viewModelScope.launch {
            val randomQuote = getRandomQuoteUseCase()
            observeQuote(randomQuote.id)
        }
    }

    private fun observeQuote(id: Int) {
        quoteJob?.cancel()
        quoteJob = getQuoteByIdUseCase(id)
            .onEach { quote ->
                quote?.let { _uiState.value = uiState.value.copy(quote = it) }
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

    fun onSendTestNotificationClicked() {
        viewModelScope.launch {
            val quote = getRandomQuoteUseCase()
            notificationProvider.showNotification(quote)
        }
    }
}
