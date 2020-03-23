package jp.maskedronin.bitwatcher.domain.repository

import jp.maskedronin.bitwatcher.domain.entity.ExchangeRate
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    suspend fun refreshRate(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    )

    suspend fun getRate(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    ): ExchangeRate?

    fun getAllRates(): Flow<List<ExchangeRate>>

    suspend fun getCryptowatchPair(): String
}