package com.example.motivationalsentencesapp.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.ui.navigation.Routes
import com.example.motivationalsentencesapp.domain.usecase.GetRandomQuoteUseCase
import com.example.motivationalsentencesapp.ui.notification.NotificationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val quote: Quote? = null
)

class HomeViewModel(
    private val getRandomQuoteUseCase: GetRandomQuoteUseCase,
    private val notificationProvider: NotificationProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        val quoteText = savedStateHandle.get<String>(Routes.Home.ARG_QUOTE_TEXT)
        val quoteAuthor = savedStateHandle.get<String>(Routes.Home.ARG_QUOTE_AUTHOR)

        if (quoteText != null && quoteAuthor != null) {
            _uiState.value = HomeUiState(quote = Quote(text = quoteText, author = quoteAuthor))
        } else {
            loadRandomQuote()
        }
    }

    fun loadRandomQuote() {
        viewModelScope.launch {
            val quote = getRandomQuoteUseCase()
            _uiState.value = HomeUiState(quote = quote)
        }
    }

    fun onSendTestNotificationClicked() {
        viewModelScope.launch {
            val quote = getRandomQuoteUseCase()
            notificationProvider.showNotification(quote)
        }
    }
}
