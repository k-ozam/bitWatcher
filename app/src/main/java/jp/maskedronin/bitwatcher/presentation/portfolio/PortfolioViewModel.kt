package jp.maskedronin.bitwatcher.presentation.portfolio

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.usecase.*
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Tutorial
import jp.maskedronin.bitwatcher.presentation.common.Formatter
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_DEBOUNCE_MILLIS = 1000L

class PortfolioViewModel(
    getSettlementCurrency: GetSettlementCurrencyUseCase,
    getPortfolioItemList: GetPortfolioItemListUseCase,
    getTotalValuation: GetTotalValuationUseCase,
    private val deleteProperty: DeletePropertyUseCase,
    private val updateProperty: UpdatePropertyUseCase,
    private val refreshPropertyAmount: RefreshPropertyAmountUseCase,
    private val refreshExchangeRate: RefreshExchangeRateUseCase,
    isTutorialEnabled: IsTutorialEnabled,
    private val setTutorialEnabled: SetTutorialEnabled,
    getAllExchangeAccounts: GetAllExchangeAccountsUseCase
) : ViewModel(), PortfolioRecyclerAdapter.ViewHolder.ViewModel {
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

    private val _toastEvent = LiveEvent<ToastConfig>()
    val toastEvent: LiveData<ToastConfig> = _toastEvent

    private val _messageDialogEvent = LiveEvent<StringResource>()
    val messageDialogEvent: LiveData<StringResource> = _messageDialogEvent

    private val _portfolioItemList: Flow<List<PortfolioItem>> = getPortfolioItemList()
    val portfolioItemList: LiveData<List<PortfolioItem>> = _portfolioItemList
        .debounce(REFRESH_DEBOUNCE_MILLIS)
        .catch { throwable -> throwableHandler.handle(throwable) }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val totalValuationText: LiveData<String> = combine(
        getTotalValuation(),
        getSettlementCurrency().filterNotNull()
    ) { valuation, currency -> "${Formatter.formatValue(valuation)} ${currency.getSymbol()}" }
        .debounce(REFRESH_DEBOUNCE_MILLIS)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    override val settlement: LiveData<Currency> = getSettlementCurrency()
        .filterNotNull()
        .asLiveData()

    private val _propertyDetailDialogEvent = LiveEvent<PortfolioItem>()
    val propertyDetailDialogEvent: LiveData<PortfolioItem> = _propertyDetailDialogEvent

    private val _openPropertyActionSelectDialogEvent = LiveEvent<PortfolioItem>()
    val propertyActionSelectDialogEvent: LiveData<PortfolioItem> =
        _openPropertyActionSelectDialogEvent

    private val _openAmountModifyDialogEvent = LiveEvent<PortfolioItem>()
    val amountModifyDialogEvent: LiveData<PortfolioItem> = _openAmountModifyDialogEvent

    private val _propertyRegisterEvent = LiveEvent<Unit>()
    val propertyRegisterEvent: LiveData<Unit> = _propertyRegisterEvent

    private val _exchangeAccountRegisterEvent = LiveEvent<Unit>()
    val exchangeAccountRegisterEvent: LiveData<Unit> = _exchangeAccountRegisterEvent

    private val _portfolioRegisterActionSelectDialogEvent = LiveEvent<Unit>()
    val portfolioRegisterActionSelectDialogEvent: LiveData<Unit> =
        _portfolioRegisterActionSelectDialogEvent

    val refreshing: LiveData<Boolean> = combine(
        refreshExchangeRate.isProcessing,
        refreshPropertyAmount.isProcessing
    ) { isRefreshingRate, isRefreshingAmount ->
        isRefreshingRate || isRefreshingAmount
    }.asLiveData(viewModelScope.coroutineContext)

    private val _enableRefresh: Flow<Boolean> = combine(
        refreshExchangeRate.isProcessing,
        refreshPropertyAmount.isProcessing,
        _portfolioItemList.map { it.isNotEmpty() },
        getAllExchangeAccounts().map { it.isNotEmpty() }
    ) { isRefreshingRate, isRefreshingAmount, isPortfolioNotEmpty, exchangeAccountExists ->
        when {
            isRefreshingRate -> false
            isRefreshingAmount -> false
            isPortfolioNotEmpty -> true
            exchangeAccountExists -> true
            else -> false
        }
    }
    val enableRefresh: LiveData<Boolean> = _enableRefresh
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val swipeRefreshTutorialVisible: LiveData<Boolean> = combine(
        isTutorialEnabled(Tutorial.SWIPE_REFRESH),
        _enableRefresh
    ) { isTutorialEnabled, enableRefresh ->
        when {
            isTutorialEnabled.not() -> false
            enableRefresh -> true
            else -> false
        }
    }.distinctUntilChanged()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                refreshPropertyAmount()
            }.onFailure {
                throwableHandler.handle(it)
            }.onSuccess {
                // nothing to do
            }
        }
    }

    fun onSwipeRefresh() {
        _toastEvent.value = ToastConfig(
            StringResource.from(R.string.property_amount_and_rate_update_started_message),
            duration = ToastConfig.Duration.SHORT
        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setTutorialEnabled(Tutorial.SWIPE_REFRESH, enabled = false)
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                refreshPropertyAmount()
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }

            runCatching {
                refreshExchangeRate()
            }.onSuccess {
                _toastEvent.postValue(
                    ToastConfig(
                        StringResource.from(R.string.property_amount_and_rate_update_completed_message),
                        duration = ToastConfig.Duration.SHORT
                    )
                )
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    override fun onPortfolioItemClick(portfolioItem: PortfolioItem) {
        _propertyDetailDialogEvent.value = portfolioItem
    }

    override fun onPortfolioItemLongClick(portfolioItem: PortfolioItem) {
        _openPropertyActionSelectDialogEvent.value = portfolioItem
    }

    fun onAmountModifySelected(portfolioItem: PortfolioItem) {
        _openAmountModifyDialogEvent.value = portfolioItem
    }

    fun onAmountModifyButtonClicked(portfolioItem: PortfolioItem, newAmount: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                updateProperty(
                    target = with(portfolioItem) { currency to exchange },
                    amount = newAmount
                )
            }.onSuccess {
                _toastEvent.postValue(
                    ToastConfig(
                        StringResource.from(R.string.property_amount_modify_completed_message),
                        ToastConfig.Duration.SHORT
                    )
                )
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    fun onPropertyDeleteSelected(portfolioItem: PortfolioItem) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                deleteProperty(
                    currency = portfolioItem.currency,
                    exchange = portfolioItem.exchange
                )
            }.onSuccess {
                ToastConfig(
                    StringResource.from(R.string.property_delete_completed_message),
                    duration = ToastConfig.Duration.SHORT
                ).also { config ->
                    _toastEvent.postValue(config)
                }
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    fun onFloatingPlusButtonClick() {
        _portfolioRegisterActionSelectDialogEvent.value = Unit
    }

    fun onRegisterPropertySelected() {
        _propertyRegisterEvent.value = Unit
    }

    fun onRegisterExchangeAccountSelected() {
        _exchangeAccountRegisterEvent.value = Unit
    }

    fun onSwipeRefreshTutorialCloseClick() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                setTutorialEnabled(Tutorial.SWIPE_REFRESH, enabled = false)
            }.onFailure {
                throwableHandler.handle(it)
            }
        }
    }

    class Factory @Inject constructor(
        private val getPortfolioItemList: GetPortfolioItemListUseCase,
        private val getSettlementCurrency: GetSettlementCurrencyUseCase,
        private val getTotalValuation: GetTotalValuationUseCase,
        private val deleteProperty: DeletePropertyUseCase,
        private val updateProperty: UpdatePropertyUseCase,
        private val refreshPropertyAmount: RefreshPropertyAmountUseCase,
        private val refreshExchangeRate: RefreshExchangeRateUseCase,
        private val isTutorialEnabled: IsTutorialEnabled,
        private val setTutorialEnabled: SetTutorialEnabled,
        private val getAllExchangeAccounts: GetAllExchangeAccountsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PortfolioViewModel(
                getSettlementCurrency,
                getPortfolioItemList,
                getTotalValuation,
                deleteProperty,
                updateProperty,
                refreshPropertyAmount,
                refreshExchangeRate,
                isTutorialEnabled,
                setTutorialEnabled,
                getAllExchangeAccounts
            ) as T
    }
}