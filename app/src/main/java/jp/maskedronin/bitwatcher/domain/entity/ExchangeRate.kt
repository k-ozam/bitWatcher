package jp.maskedronin.bitwatcher.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "exchange_rates",
    primaryKeys = [
        "base",
        "quote",
        "exchange"
    ]
)
data class ExchangeRate(
    @ColumnInfo(name = "base")
    val base: Currency,
    @ColumnInfo(name = "quote")
    val quote: Currency,
    @ColumnInfo(name = "exchange")
    val exchange: Exchange,
    @ColumnInfo(name = "value")
    val value: Double,
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime,
    @ColumnInfo(name = "is_latest")
    val isLatest: Boolean
)