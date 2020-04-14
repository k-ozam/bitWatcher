package jp.maskedronin.bitwatcher.presentation

import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.R
import jp.maskedronin.bitwatcher.domain.usecase.GetNotificationsUseCase
import jp.maskedronin.bitwatcher.domain.usecase.GetPortfolioItemListUseCase
import jp.maskedronin.bitwatcher.domain.usecase.GetSettlementCurrencyUseCase
import jp.maskedronin.bitwatcher.domain.usecase.RefreshExchangeRateUseCase
import jp.maskedronin.bitwatcher.presentation.common.SnackbarConfig
import jp.maskedronin.bitwatcher.presentation.common.ThrowableHandler
import jp.maskedronin.bitwatcher.presentation.common.ToastConfig
import jp.maskedronin.bitwatcher.presentation.common.resource.AnimationResource
import jp.maskedronin.bitwatcher.presentation.common.resource.StringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(
    private val getSettlementCurrency: GetSettlementCurrencyUseCase,
    private val refreshExchangeRate: RefreshExchangeRateUseCase,
    getPortfolioItemList: GetPortfolioItemListUseCase,
    getNotificationsUseCase: GetNotificationsUseCase
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

    val refreshIconVisible: LiveData<Boolean> = getPortfolioItemList()
        .map { it.isNotEmpty() }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    val refreshIconAnimRes: LiveData<AnimationResource?> = refreshExchangeRate
        .isProcessing
        .map { isProcessing ->
            if (isProcessing)
                AnimationResource(R.anim.rotation_forever)
            else null
        }
        .asLiveData()

    val notificationIconVisible: LiveData<Boolean> = getNotificationsUseCase()
        .map { it.isNotEmpty() }
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val _notificationEvent = LiveEvent<Unit>()
    val notificationEvent: LiveData<Unit> = _notificationEvent

    private val _settingsEvent = LiveEvent<Unit>()
    val settingsEvent: LiveData<Unit> = _settingsEvent

    private val isPortfolioShown = MutableLiveData(false)
    val toolbarMenuVisible: LiveData<Boolean> = isPortfolioShown

    init {
        // 基準通貨が変更されたらレートを更新する
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                getSettlementCurrency()
                    .filterNotNull()
                    .collectLatest {
                        refreshExchangeRate()
                    }
            }.onSuccess {
                _toastEvent.postValue(
                    ToastConfig(
                        StringResource.from(R.string.rate_update_completed_message),
                        duration = ToastConfig.Duration.SHORT
                    )
                )
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    fun onRefreshIconClick() {
        _toastEvent.value = ToastConfig(
            StringResource.from(R.string.rate_update_started_message),
            ToastConfig.Duration.SHORT
        )

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                refreshExchangeRate()
            }.onSuccess {
                _toastEvent.postValue(
                    ToastConfig(
                        StringResource.from(R.string.rate_update_completed_message),
                        ToastConfig.Duration.SHORT
                    )
                )
            }.onFailure { throwable ->
                throwableHandler.handle(throwable)
            }
        }
    }

    fun onNotificationIconClick() {
        _notificationEvent.value = Unit
    }

    fun onSettingsIconClick() {
        _settingsEvent.value = Unit
    }

    fun setPortfolioShown(shown: Boolean) {
        isPortfolioShown.value = shown
    }

    class Factory @Inject constructor(
        private val getSettlementCurrency: GetSettlementCurrencyUseCase,
        private val refreshExchangeRate: RefreshExchangeRateUseCase,
        private val getPortfolioItemList: GetPortfolioItemListUseCase,
        private val getNotifications: GetNotificationsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MainViewModel(
                getSettlementCurrency,
                refreshExchangeRate,
                getPortfolioItemList,
                getNotifications
            ) as T
    }
}