package jp.maskedronin.bitwatcher.data.db.dao

import androidx.room.*
import jp.maskedronin.bitwatcher.domain.entity.Property
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(property: Property)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(properties: List<Property>)

    @Query("select * from properties where exchange = :exchange AND currency = :currency")
    fun findProperty(
        currency: Currency,
        exchange: Exchange
    ): Property?

    @Query("select * from properties")
    fun getAllProperties(): Flow<List<Property>>

    @Delete
    fun delete(property: Property)

    @Query("delete from properties where exchange = :exchange")
    fun deleteByExchange(exchange: Exchange)
}