package jp.maskedronin.bitwatcher.presentation.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.hadilq.liveevent.LiveEvent
import jp.maskedronin.bitwatcher.domain.entity.Notification
import jp.maskedronin.bitwatcher.domain.usecase.GetNotificationsUseCase
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class NotificationViewModel(
    getNotifications: GetNotificationsUseCase
) : ViewModel(),
    NotificationRecyclerAdapter.ViewHolder.ViewModel {
    val notifications: LiveData<List<Notification>> = getNotifications().asLiveData(Dispatchers.IO)

    private val _exchangeAccountRegisterEvent = LiveEvent<Exchange>()
    val exchangeAccountRegisterEvent: LiveData<Exchange> = _exchangeAccountRegisterEvent

    class Factory @Inject constructor(
        private val getNotifications: GetNotificationsUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            NotificationViewModel(
                getNotifications
            ) as T
    }

    override fun onNotificationClick(notification: Notification) {
        when (notification) {
            is Notification.Unauthorized ->
                _exchangeAccountRegisterEvent.value = notification.exchange

            else -> TODO()
        }
    }
}