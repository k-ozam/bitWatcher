package jp.maskedronin.bitwatcher.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "properties",
    primaryKeys = [
        "currency",
        "exchange"
    ]
)
data class Property(
    @ColumnInfo(name = "currency")
    val currency: Currency,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "exchange")
    val exchange: Exchange,
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime
)