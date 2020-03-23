package jp.maskedronin.bitwatcher.data.repository

import jp.maskedronin.bitwatcher.data.api.cryptowatch.CryptowatchApi
import jp.maskedronin.bitwatcher.data.api.cryptowatch.CryptowatchPairWrapper
import jp.maskedronin.bitwatcher.data.db.dao.ExchangeRateDao
import jp.maskedronin.bitwatcher.data.source.ExchangeRateRemoteDataSource
import jp.maskedronin.bitwatcher.domain.entity.ExchangeRate
import jp.maskedronin.bitwatcher.domain.repository.ExchangeRateRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExchangeRateRemoteDataSource,
    private val cryptowatchApi: CryptowatchApi,
    private val exchangeRateDao: ExchangeRateDao
) : ExchangeRateRepository {
    override suspend fun refreshRate(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    ) {
        val now = LocalDateTime.now()

        if (base == quote) {
            exchangeRateDao.upsert(
                ExchangeRate(
                    base,
                    quote,
                    exchange,
                    value = 1.0,
                    updatedAt = now,
                    isLatest = true
                )
            )
            return
        }

        runCatching {
            remoteDataSource.getRate(base, quote, exchange)
        }.onSuccess { latestRate ->
            exchangeRateDao.upsert(
                ExchangeRate(
                    base,
                    quote,
                    exchange,
                    value = latestRate,
                    updatedAt = now,
                    isLatest = true
                )
            )
        }.onFailure { throwable ->
            // キャッシュ
            exchangeRateDao.getExchangeRate(base, quote, exchange)
                ?.copy(isLatest = false)
                ?.also { previousRate ->
                    exchangeRateDao.upsert(previousRate)
                }

            throw throwable
        }
    }

    override suspend fun getRate(
        base: Currency,
        quote: Currency,
        exchange: Exchange
    ): ExchangeRate? = exchangeRateDao.getExchangeRate(
        base, quote, exchange
    )

    override fun getAllRates(): Flow<List<ExchangeRate>> = exchangeRateDao.getAllRates()

    @kotlinx.serialization.UnstableDefault
    override suspend fun getCryptowatchPair(): String =
        Json.stringify(CryptowatchPairWrapper.serializer(), cryptowatchApi.getPair())
}