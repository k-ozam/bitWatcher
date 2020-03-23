package jp.maskedronin.bitwatcher.presentation.exchangeaccountedit

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.usecase.GetExchangeAccountUseCase
import jp.maskedronin.bitwatcher.domain.usecase.SetExchangeAccountUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ExchangeAccountEditViewModel(
    private val exchange: Exchange,
    private val setExchangeAccount: SetExchangeAccountUseCase,
    getExchangeAccount: GetExchangeAccountUseCase
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

    private val _snackbarEvent = LiveEvent<SnackbarConfig>()
    val snackbarEvent: LiveData<SnackbarConfig> = _snackbarEvent

    private val _messageDialogEvent = LiveEvent<StringResource>()
    val messageDialogEvent: LiveData<StringResource> = _messageDialogEvent

    private val _appRestartEvent = LiveEvent<Unit>()
    val appRestartEvent: LiveData<Unit> = _appRestartEvent

    private val _toastEvent = LiveEvent<ToastConfig>()
    val toastEvent: LiveData<ToastConfig> = _toastEvent

    private val account: ExchangeAccount? = runBlocking { getExchangeAccount(exchange) }
    val apiKey: MutableLiveData<String> = MutableLiveData(account?.apiKey ?: "")
    val apiSecret: MutableLiveData<String> = MutableLiveData(account?.apiSecret ?: "")

    val enableAuthorizeButton: LiveData<Boolean> = combine(
        apiKey.asFlow(),
        apiSecret.asFlow()
    ) { apiKey, apiSecret ->
        when {
            apiKey.isNullOrEmpty() -> false
            apiSecret.isNullOrEmpty() -> false
            else -> true
        }
    }.asLiveData()

    private val _authorizeConfirmDialogEvent = LiveEvent<Unit>()
    val authorizeConfirmDialogEvent: LiveData<Unit> = _authorizeConfirmDialogEvent

    fun onAuthorizeButtonClick() {
        _authorizeConfirmDialogEvent.value = Unit
    }

    fun onAuthorizeConfirmButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setExchangeAccount(
                    ExchangeAccount(
                        exchange = exchange,
                        apiKey = apiKey.value!!,
                        apiSecret = apiSecret.value!!,
                        isValid = null,
                        updatedAt = null
                    )
                )
            }.onSuccess {
                // 認証情報をHTTPクライアントに持たせるために再起動が必要
                _appRestartEvent.postValue(Unit)
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    class Factory @Inject constructor(
        private val setExchangeAccount: SetExchangeAccountUseCase,
        private val getExchangeAccount: GetExchangeAccountUseCase
    ) : ViewModelProvider.Factory {
        lateinit var exchange: Exchange

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ExchangeAccountEditViewModel(
                exchange,
                setExchangeAccount,
                getExchangeAccount
            ) as T
    }
}