package jp.maskedronin.bitwatcher.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.maskedronin.bitwatcher.domain.entity.ExchangeRate
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(exchangeRate: ExchangeRate)

    @Query("select * from exchange_rates where base = :base AND quote = :quote AND exchange = :exchange")
    fun getExchangeRate(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    ): ExchangeRate?

    @Query("select * from exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRate>>
}