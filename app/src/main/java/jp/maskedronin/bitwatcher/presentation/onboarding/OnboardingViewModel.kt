package jp.maskedronin.bitwatcher.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.usecase.GetSettlementCurrencyUseCase
import jp.maskedronin.bitwatcher.domain.usecase.SetOnboardingCompletedUseCase
import jp.maskedronin.bitwatcher.domain.usecase.SetSettlementCurrencyUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnboardingViewModel(
    private val getSettlementCurrency: GetSettlementCurrencyUseCase,
    private val setSettlementCurrency: SetSettlementCurrencyUseCase,
    private val setOnboardingCompleted: SetOnboardingCompletedUseCase
) : ViewModel() {
    private val throwableHandler = ThrowableHandler(
        onHandle = { message, type ->
            when (type) {
                ThrowableHandler.MessageType.SHORT_SENTENCE -> _snackbarEvent.postValue(
                    SnackbarConfig(
                        message,
                        SnackbarConfig.Duration.INDEFINITE
                    )
                )
                ThrowableHandler.MessageType.LONG_SENTENCE -> _messageDialogEvent.postValue(message)
            }
        }
    )

    private val _toastEvent = LiveEvent<ToastConfig>()
    val toastEvent: LiveData<ToastConfig> = _toastEvent

    private val _snackbarEvent = LiveEvent<SnackbarConfig>()
    val snackbarEvent: LiveData<SnackbarConfig> = _snackbarEvent

    private val _messageDialogEvent = LiveEvent<StringResource>()
    val messageDialogEvent: LiveData<StringResource> = _messageDialogEvent

    private val _currencySelectDialogEvent = LiveEvent<Unit>()
    val currencySelectDialogEvent: LiveData<Unit> = _currencySelectDialogEvent

    private val _portfolioEvent = LiveEvent<Unit>()
    val portfolioEvent: LiveData<Unit> = _portfolioEvent

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            if (getSettlementCurrency().first() == null) {
                _currencySelectDialogEvent.postValue(Unit)
            }
        }
    }

    fun onCurrencySelected(Currency: Currency) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setSettlementCurrency(Currency)
            }.onSuccess {
                _toastEvent.postValue(
                    ToastConfig(
                        StringResource.from(R.string.settlement_currency_setting_completed_message),
                        duration = ToastConfig.Duration.SHORT
                    )
                )

                onOnboardingCompleted()
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    private fun onOnboardingCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setOnboardingCompleted(true)
            }.onSuccess {
                _portfolioEvent.postValue(Unit)
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    class Factory @Inject constructor(
        private val getSettlementCurrency: GetSettlementCurrencyUseCase,
        private val setSettlementCurrency: SetSettlementCurrencyUseCase,
        private val setOnboardingCompleted: SetOnboardingCompletedUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            OnboardingViewModel(
                getSettlementCurrency,
                setSettlementCurrency,
                setOnboardingCompleted
            ) as T
    }
}