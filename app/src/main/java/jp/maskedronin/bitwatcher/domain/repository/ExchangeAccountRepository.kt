package jp.maskedronin.bitwatcher.domain.repository

import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow

interface ExchangeAccountRepository {
    suspend fun getAccount(exchange: Exchange): ExchangeAccount?
    fun getAccountAsFlow(exchange: Exchange): Flow<ExchangeAccount?>
    fun getAllAccountsAsFlow(): Flow<List<ExchangeAccount>>
    suspend fun setAccount(account: ExchangeAccount)
    suspend fun deleteAccount(exchange: Exchange)
}