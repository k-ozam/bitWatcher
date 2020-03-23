package jp.maskedronin.bitwatcher.data.repository

import jp.maskedronin.bitwatcher.data.source.ExchangeAccountLocalDataSource
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.repository.ExchangeAccountRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeAccountRepositoryImpl @Inject constructor(
    private val localDataSource: ExchangeAccountLocalDataSource
) : ExchangeAccountRepository {
    override suspend fun getAccount(exchange: Exchange): ExchangeAccount? =
        localDataSource.getAccount(exchange).first()

    override fun getAccountAsFlow(exchange: Exchange): Flow<ExchangeAccount?> =
        localDataSource.getAccount(exchange)

    override fun getAllAccountsAsFlow(): Flow<List<ExchangeAccount>> =
        localDataSource.getAllAccounts()

    override suspend fun setAccount(account: ExchangeAccount) {
        localDataSource.setAccount(account)
    }

    override suspend fun deleteAccount(exchange: Exchange) {
        localDataSource.deleteAccount(exchange)
    }
}