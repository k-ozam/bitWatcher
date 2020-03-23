package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.Notification
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNotificationsUseCase @Inject constructor(
    private val exchangeAccountRepository: ExchangeAccountRepository
) {
    operator fun invoke(): Flow<List<Notification>> = getUnauthorizedNotification()

    private fun getUnauthorizedNotification() =
        exchangeAccountRepository.getAllAccountsAsFlow()
            .map { accounts ->
                accounts.filter { it.isValid == false }
                    .map { account ->
                        Notification.Unauthorized(exchange = account.exchange)
                    }
            }
}