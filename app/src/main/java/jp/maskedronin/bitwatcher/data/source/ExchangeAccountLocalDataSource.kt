package jp.maskedronin.bitwatcher.data.source

import jp.maskedronin.bitwatcher.data.db.dao.ExchangeAccountDao
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeAccountLocalDataSource @Inject constructor(
    private val exchangeAccountDao: ExchangeAccountDao
) {
    fun getAccount(exchange: Exchange): Flow<ExchangeAccount?> =
        exchangeAccountDao.findAccount(exchange)

    fun getAllAccounts(): Flow<List<ExchangeAccount>> = exchangeAccountDao.getAllAccounts()

    suspend fun setAccount(account: ExchangeAccount) {
        exchangeAccountDao.upsert(account)
    }

    suspend fun deleteAccount(exchange: Exchange) {
        exchangeAccountDao.delete(exchange)
    }
}