package pl.amarosee.motywator.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.amarosee.motywator.domain.usecase.GetOnboardingStatusUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    getOnboardingStatusUseCase: GetOnboardingStatusUseCase
) : ViewModel() {

    val isOnboardingCompleted: StateFlow<Boolean?> = getOnboardingStatusUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

}
