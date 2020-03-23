package jp.maskedronin.bitwatcher.presentation.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.domain.usecase.IsOnboardingCompletedUseCase
import javax.inject.Inject

class LaunchViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase
) : ViewModel() {
    private val _onboardingEvent = LiveEvent<Unit>()
    val onboardingEvent: LiveData<Unit> = _onboardingEvent

    private val _portfolioEvent = LiveEvent<Unit>()
    val portfolioEvent: LiveData<Unit> = _portfolioEvent

    fun init() {
        if (isOnboardingCompleted()) {
            _portfolioEvent.value = Unit
        } else {
            _onboardingEvent.value = Unit
        }
    }

    class Factory @Inject constructor(
        private val isOnboardingCompleted: IsOnboardingCompletedUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LaunchViewModel(
                isOnboardingCompleted
            ) as T
    }
}