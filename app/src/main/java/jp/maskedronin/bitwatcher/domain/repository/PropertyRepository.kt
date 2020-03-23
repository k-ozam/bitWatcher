package jp.maskedronin.bitwatcher.domain.repository

import jp.maskedronin.bitwatcher.domain.entity.Property
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    fun getAllProperties(): Flow<List<Property>>

    suspend fun findProperty(currency: Currency, exchange: Exchange): Property?

    suspend fun updateProperty(property: Property)

    suspend fun addProperty(property: Property)

    suspend fun deleteProperty(property: Property)

    suspend fun refreshProperties(exchange: Exchange)
}