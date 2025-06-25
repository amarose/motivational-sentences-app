package com.example.motivationalsentencesapp.ui.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motivationalsentencesapp.data.model.ArchivedQuote
import com.example.motivationalsentencesapp.domain.usecase.CleanUpArchiveUseCase
import com.example.motivationalsentencesapp.domain.usecase.GetArchivedQuotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ArchiveUiState(
    val quotes: List<ArchivedQuote> = emptyList(),
    val isLoading: Boolean = true
)

class ArchiveViewModel(
    private val getArchivedQuotesUseCase: GetArchivedQuotesUseCase,
    private val cleanUpArchiveUseCase: CleanUpArchiveUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

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

    private fun cleanUpArchive() {
        viewModelScope.launch {
            cleanUpArchiveUseCase()
        }
    }
}
