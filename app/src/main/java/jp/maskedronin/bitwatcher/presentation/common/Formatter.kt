package jp.maskedronin.bitwatcher.presentation.common

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.math.RoundingMode
import java.text.NumberFormat

object Formatter {
    enum class DateTimeFormatType(
        val formatter: DateTimeFormatter
    ) {
        PATTERN1(DateTimeFormatter.ofPattern("yyyy/MM/dd/ HH:mm:ss"))
        ;
    }

    @JvmOverloads
    @JvmStatic
    fun formatDateTime(
        dateTime: LocalDateTime,
        type: DateTimeFormatType = DateTimeFormatType.PATTERN1
    ): String = dateTime.format(type.formatter)

    /**
     * e.g.
     * @receiver 123456.789000000
     * @return "123,456.79"
     */
    @JvmStatic
    @JvmOverloads
    fun formatPropertyAmount(
        value: Double,
        scale: Int? = null
    ): String = if (scale == null) {
        format(value)
    } else {
        format(
            value,
            scale
        )
    }

    /**
     * e.g.
     * @receiver 123456.789000000
     * @return "123,456.79"
     */
    @JvmStatic
    @JvmOverloads
    fun formatValue(
        value: Double,
        scale: Int? = null
    ): String = if (scale == null) {
        format(value)
    } else {
        format(
            value,
            scale
        )
    }

    /**
     * e.g.
     * @receiver 123456.789000000
     * @return "123,456.79"
     */
    private fun format(
        value: Double,
        scale: Int = 2 // 0.01 までわかれば十分なはず
    ): String = value
        .toBigDecimal()
        .setScale(scale, RoundingMode.HALF_EVEN)
        .toDouble()
        .let { NumberFormat.getInstance().format(it) }
}