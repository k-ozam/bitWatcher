package jp.maskedronin.bitwatcher.presentation.settings

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.usecase.DeleteExchangeAccountUseCase
import jp.maskedronin.bitwatcher.domain.usecase.GetAllExchangeAccountsUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExchangeAccountSettingsViewModel(
    getAllExchangeAccounts: GetAllExchangeAccountsUseCase,
    private val deleteExchangeAccount: DeleteExchangeAccountUseCase
) : ViewModel(),
    ExchangeAccountRecyclerAdapter.ViewHolder.AccountValid.ViewModel,
    ExchangeAccountRecyclerAdapter.ViewHolder.AccountUnconfirmed.ViewModel,
    ExchangeAccountRecyclerAdapter.ViewHolder.AccountInvalid.ViewModel,
    ExchangeAccountRecyclerAdapter.ViewHolder.AccountUnregistered.ViewModel {
    private val throwableHandler = ThrowableHandler(
        onHandle = { message ->
            _snackbarEvent.postValue(
                SnackbarConfig(
                    message,
                    SnackbarConfig.Duration.INDEFINITE
                )
            )
        }
    )

    private val _snackbarEvent = LiveEvent<SnackbarConfig>()
    val snackbarEvent: LiveData<SnackbarConfig> = _snackbarEvent

    private val _toastEvent = LiveEvent<ToastConfig>()
    val toastEvent: LiveData<ToastConfig> = _toastEvent

    val exchangeToAccountList: LiveData<List<Pair<Exchange, ExchangeAccount?>>> =
        getAllExchangeAccounts().map { exchangeAccountList ->
            Exchange.values().filter(Exchange::isBalanceApiAvailable)
                .map { exchange ->
                    exchange to exchangeAccountList.find { account ->
                        account.exchange == exchange
                    }
                }.sortedBy {
                    val account: ExchangeAccount? = it.second
                    if (account == null) 0 else 1
                }
        }.asLiveData()

    private val _editConfirmDialogEvent = LiveEvent<Exchange>()
    val editEvent: LiveData<Exchange> = _editConfirmDialogEvent

    private val _deleteConfirmDialogEvent = LiveEvent<Exchange>()
    val deleteConfirmDialogEvent: LiveData<Exchange> = _deleteConfirmDialogEvent

    private val _registerAccountEvent = LiveEvent<Exchange>()
    val registerAccountEvent: LiveData<Exchange> = _registerAccountEvent

    override fun onEditIconClick(exchange: Exchange) {
        _editConfirmDialogEvent.value = exchange
    }

    override fun onDeleteIconClick(exchange: Exchange) {
        _deleteConfirmDialogEvent.value = exchange
    }

    fun onDeleteSelected(exchange: Exchange) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                deleteExchangeAccount(exchange)
            }.onSuccess {
                _toastEvent.postValue(
                    ToastConfig(
                        StringResource.from(
                            R.string.exchange_api_unlink_completed_message,
                            exchange.canonicalName
                        ),
                        duration = ToastConfig.Duration.SHORT
                    )
                )
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    override fun onAccountUnregisteredClick(exchange: Exchange) {
        _registerAccountEvent.value = exchange
    }

    class Factory @Inject constructor(
        private val getAllExchangeAccounts: GetAllExchangeAccountsUseCase,
        private val deleteExchangeAccount: DeleteExchangeAccountUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ExchangeAccountSettingsViewModel(
                getAllExchangeAccounts,
                deleteExchangeAccount
            ) as T
    }
}