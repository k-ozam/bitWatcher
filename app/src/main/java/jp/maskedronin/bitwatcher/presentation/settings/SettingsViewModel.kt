package jp.maskedronin.bitwatcher.presentation.settings

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.usecase.GetSettlementCurrencyUseCase
import jp.maskedronin.bitwatcher.domain.usecase.SetSettlementCurrencyUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel(
    getSettlementCurrency: GetSettlementCurrencyUseCase,
    private val setSettlementCurrency: SetSettlementCurrencyUseCase
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

    val settlementCurrency = getSettlementCurrency().asLiveData(Dispatchers.IO)

    private val _settlementCurrencySelectDialogEvent = LiveEvent<Unit>()
    val settlementCurrencySelectDialogEvent: LiveData<Unit> = _settlementCurrencySelectDialogEvent

    private val _exchangeAccountEvent = LiveEvent<Unit>()
    val exchangeAccountEvent: LiveData<Unit> = _exchangeAccountEvent

    private val _ossLisenceEvent = LiveEvent<Unit>()
    val ossLicenseEvent: LiveData<Unit> = _ossLisenceEvent

    fun onSettlementCurrencyClick() {
        _settlementCurrencySelectDialogEvent.value = Unit
    }

    fun onSettlementCurrencyListItemClick(Currency: Currency) {
        if (settlementCurrency.value == Currency) {
            return
        }

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
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    fun onExchangeAccountClick() {
        _exchangeAccountEvent.value = Unit
    }

    fun onOssLicenseClick() {
        _ossLisenceEvent.value = Unit
    }

    class Factory @Inject constructor(
        private val getSettlementCurrency: GetSettlementCurrencyUseCase,
        private val setSettlementCurrency: SetSettlementCurrencyUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            SettingsViewModel(
                getSettlementCurrency,
                setSettlementCurrency
            ) as T
    }
}