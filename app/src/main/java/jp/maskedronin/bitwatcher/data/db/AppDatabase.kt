package jp.maskedronin.bitwatcher.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.maskedronin.bitwatcher.data.db.dao.ExchangeAccountDao
import jp.maskedronin.bitwatcher.data.db.dao.ExchangeRateDao
import jp.maskedronin.bitwatcher.data.db.dao.PropertyDao
import jp.maskedronin.bitwatcher.domain.entity.ExchangeAccount
import jp.maskedronin.bitwatcher.domain.entity.ExchangeRate
import jp.maskedronin.bitwatcher.domain.entity.Property

@Database(
    entities = [
        Property::class,
        ExchangeRate::class,
        ExchangeAccount::class
    ],
    version = 1
)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun exchangeAccountDao(): ExchangeAccountDao
}