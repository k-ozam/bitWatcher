package jp.maskedronin.bitwatcher.data.db

import androidx.room.TypeConverter
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class RoomTypeConverter {
    @TypeConverter
    fun String.convertToCurrency(): Currency = Currency.valueOf(this)

    @TypeConverter
    fun Currency.convertToString(): String = this.name

    @TypeConverter
    fun Exchange.convertToString(): String = this.name

    @TypeConverter
    fun String.convertToExchange(): Exchange = Exchange.valueOf(this)

    @TypeConverter
    fun LocalDateTime?.convertToString(): String? = this?.run {
        this.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun String?.convertToLocalDateTime(): LocalDateTime? = this?.run {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
