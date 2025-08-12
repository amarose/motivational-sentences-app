package pl.amarosee.motywator.ui.background

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.amarosee.motywator.data.model.Background
import pl.amarosee.motywator.domain.usecase.GetAvailableBackgroundsUseCase
import pl.amarosee.motywator.domain.usecase.GetSelectedBackgroundUseCase
import pl.amarosee.motywator.domain.usecase.UpdateSelectedBackgroundUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class BackgroundUiState(
    val backgrounds: List<Background> = emptyList(),
    val selectedBackgroundResId: Int? = null
)

sealed class BackgroundViewEffect {
    object BackgroundChanged : BackgroundViewEffect()
}

class BackgroundViewModel(
    private val getAvailableBackgroundsUseCase: GetAvailableBackgroundsUseCase,
    private val getSelectedBackgroundUseCase: GetSelectedBackgroundUseCase,
    private val updateSelectedBackgroundUseCase: UpdateSelectedBackgroundUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BackgroundUiState())
    val uiState: StateFlow<BackgroundUiState> = _uiState.asStateFlow()

    private val _effect = Channel<BackgroundViewEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadBackgrounds()
        observeSelectedBackground()
    }

    private fun loadBackgrounds() {
        val backgrounds = getAvailableBackgroundsUseCase()
        _uiState.value = _uiState.value.copy(backgrounds = backgrounds)
    }

    private fun observeSelectedBackground() {
        getSelectedBackgroundUseCase()
            .onEach { resId ->
                _uiState.value = _uiState.value.copy(selectedBackgroundResId = resId)
            }
            .launchIn(viewModelScope)
    }

    fun onBackgroundSelected(backgroundResId: Int) {
        viewModelScope.launch {
            updateSelectedBackgroundUseCase(backgroundResId)
            _effect.send(BackgroundViewEffect.BackgroundChanged)
        }
    }
}
