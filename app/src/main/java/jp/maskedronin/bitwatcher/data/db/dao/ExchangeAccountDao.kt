package jp.maskedronin.bitwatcher.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(account: ExchangeAccount)

    @Query("select * from exchange_accounts")
    fun getAllAccounts(): Flow<List<ExchangeAccount>>

    @Query("select * from exchange_accounts where exchange = :exchange")
    fun findAccount(exchange: Exchange): Flow<ExchangeAccount?>

    @Query("delete from exchange_accounts where exchange = :exchange")
    suspend fun delete(exchange: Exchange)
}