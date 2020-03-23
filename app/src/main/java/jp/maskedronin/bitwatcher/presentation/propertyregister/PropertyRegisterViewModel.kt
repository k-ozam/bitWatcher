package jp.maskedronin.bitwatcher.presentation.propertyregister

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.usecase.AddPropertyUseCase
import jp.maskedronin.bitwatcher.domain.usecase.GetAllExchangeAccountsUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler

import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class PropertyRegisterViewModel(
    private val addProperty: AddPropertyUseCase,
    allExchangeAccounts: GetAllExchangeAccountsUseCase
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

    private val _currency = BroadcastChannel<Currency?>(Channel.CONFLATED)
        .apply { offer(null) }
    val currency: LiveData<Currency?> = _currency.asFlow().asLiveData()

    private val _exchange = BroadcastChannel<Exchange?>(Channel.CONFLATED)
        .apply { offer(null) }
    val exchange: LiveData<Exchange?> = _exchange.asFlow().asLiveData()

    private val cryptos: Flow<List<Currency>> = _exchange.asFlow()
        .map { exchange -> exchange?.cryptos ?: Currency.cryptos }

    fun getCryptos(): List<Currency> = runBlocking { cryptos.first() }

    private val exchanges: Flow<List<Exchange>> = combine(
        _currency.asFlow(),
        allExchangeAccounts()
            .map { accounts -> accounts.map(ExchangeAccount::exchange) }
            .map { apiLinkedExchanges -> Exchange.values().toList() - apiLinkedExchanges }
    ) { selectedCurrency, apiUnlinkedExchanges ->
        selectedCurrency ?: return@combine apiUnlinkedExchanges
        val dealers = selectedCurrency.dealers
        apiUnlinkedExchanges.filter { dealers.contains(it) }
    }

    fun getExchanges(): List<Exchange> = runBlocking { exchanges.first() }

    val rawAmountText = MutableLiveData("0")
    private val amount: LiveData<Double> = rawAmountText.asFlow()
        .map { rawText ->
            if (rawText.isNullOrEmpty()) {
                0.0
            } else {
                rawText.toDouble()
            }
        }
        .asLiveData()

    private val _openSelectCryptoDialogEvent = LiveEvent<Unit>()
    val cryptoSelectDialogEvent: LiveData<Unit> = _openSelectCryptoDialogEvent

    private val _openSelectExchangeDialogEvent = LiveEvent<Unit>()
    val exchangeSelectDialogEvent: LiveData<Unit> = _openSelectExchangeDialogEvent

    private val _finishEvent = LiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    @ExperimentalCoroutinesApi
    val enableRegisterButton: LiveData<Boolean> = combine(
        currency.asFlow(),
        exchange.asFlow(),
        amount.asFlow()
    ) { currency, exchange, amount ->
        when {
            currency == null -> false
            exchange == null -> false
            amount == 0.0 -> false
            else -> true
        }
    }.asLiveData()

    fun onClickCurrency() {
        _openSelectCryptoDialogEvent.value = Unit
    }

    fun onClickExchange() {
        _openSelectExchangeDialogEvent.value = Unit
    }

    fun onClickCryptoListItem(crypto: Currency?) {
        _currency.offer(crypto)
    }

    fun onExchangeListItemClick(exchange: Exchange?) {
        _exchange.offer(exchange)
    }

    fun onClickRegisterButton() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                addProperty(
                    currency.value!!,
                    amount.value!!,
                    exchange.value!!
                )
            }.onSuccess {
                _finishEvent.postValue(Unit)
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    class Factory @Inject constructor(
        private val addProperty: AddPropertyUseCase,
        private val allExchangeAccounts: GetAllExchangeAccountsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PropertyRegisterViewModel(
                addProperty,
                allExchangeAccounts
            ) as T
    }
}