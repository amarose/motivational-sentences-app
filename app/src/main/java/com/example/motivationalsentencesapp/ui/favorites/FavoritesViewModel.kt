package com.example.motivationalsentencesapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.data.model.Quote
import com.example.motivationalsentencesapp.domain.usecase.GetFavoriteQuotesUseCase
import com.example.motivationalsentencesapp.domain.usecase.UpdateQuoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class FavoritesViewEffect {
    data class ShareQuote(val text: String) : FavoritesViewEffect()
}

data class FavoritesUiState(
    val quotes: List<Quote> = emptyList(),
    val isLoading: Boolean = true
)

class FavoritesViewModel(
    private val getFavoriteQuotesUseCase: GetFavoriteQuotesUseCase,
    private val updateQuoteUseCase: UpdateQuoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<FavoritesViewEffect>()
    val effect: SharedFlow<FavoritesViewEffect> = _effect.asSharedFlow()

    init {
        loadFavoriteQuotes()
    }

    private fun loadFavoriteQuotes() {
        getFavoriteQuotesUseCase()
            .onEach { favoriteQuotes ->
                _uiState.value = FavoritesUiState(quotes = favoriteQuotes, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    fun onShareClicked(quoteText: String) {
        viewModelScope.launch {
            _effect.emit(FavoritesViewEffect.ShareQuote(quoteText))
        }
    }

    fun onToggleFavorite(quote: Quote) {
        viewModelScope.launch {
            updateQuoteUseCase(quote.copy(isFavorite = !quote.isFavorite))
        }
    }
}
