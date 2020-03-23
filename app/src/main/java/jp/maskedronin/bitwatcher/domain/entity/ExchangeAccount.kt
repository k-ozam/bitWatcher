package jp.maskedronin.bitwatcher.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "exchange_accounts",
    primaryKeys = ["exchange"]
)
data class ExchangeAccount(
    val exchange: Exchange,
    @ColumnInfo(name = "api_key")
    val apiKey: String,
    @ColumnInfo(name = "api_secret")
    val apiSecret: String,
    @ColumnInfo(name = "is_valid")
    val isValid: Boolean?,
    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime?
)



