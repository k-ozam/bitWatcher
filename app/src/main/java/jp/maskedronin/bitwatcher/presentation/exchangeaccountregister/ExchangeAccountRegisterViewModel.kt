package jp.maskedronin.bitwatcher.presentation.exchangeaccountregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.usecase.GetExchangeAccountUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ExchangeAccountRegisterViewModel(
    getExchangeAccount: GetExchangeAccountUseCase,
    private val targetExchange: Exchange?
) : ViewModel() {
    private val _finishEvent = LiveEvent<Unit>()
    val finishEvent: LiveData<Unit> = _finishEvent

    private val _toastEvent = LiveEvent<ToastConfig>()
    val toastEvent: LiveData<ToastConfig> = _toastEvent

    val exchangeEnable: Boolean = targetExchange == null

    private val accountUnregisteredExchanges: Array<Exchange> = Exchange.values()
        .filter(Exchange::isBalanceApiAvailable)
        .filter {
            runBlocking { getExchangeAccount(exchange = it) == null }
        }
        .also { exchanges ->
            if (exchanges.isEmpty()) {
                _toastEvent.value =
                    ToastConfig(
                        StringResource.from(R.string.no_exchange_apis_available_message),
                        duration = ToastConfig.Duration.SHORT
                    )
                _finishEvent.value = Unit
            }
        }
        .toTypedArray()

    val exchanges: Array<Exchange>
        get() = if (targetExchange == null) {
            accountUnregisteredExchanges
        } else throw UnsupportedOperationException()

    private val _exchange = BroadcastChannel<Exchange?>(Channel.CONFLATED)
        .apply { offer(targetExchange) }
    val exchange: LiveData<Exchange?> = _exchange.asFlow().asLiveData()

    private val _openSelectExchangeDialogEvent = LiveEvent<Unit>()
    val exchangeSelectDialogEvent: LiveData<Unit> = _openSelectExchangeDialogEvent

    fun onClickExchange() {
        _openSelectExchangeDialogEvent.value = Unit
    }

    fun onExchangeListItemClick(exchange: Exchange?) {
        _exchange.offer(exchange)
    }

    class Factory @Inject constructor(
        private val getExchangeAccount: GetExchangeAccountUseCase
    ) : ViewModelProvider.Factory {
        var exchange: Exchange? = null

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ExchangeAccountRegisterViewModel(
                getExchangeAccount,
                exchange
            ) as T
    }
}