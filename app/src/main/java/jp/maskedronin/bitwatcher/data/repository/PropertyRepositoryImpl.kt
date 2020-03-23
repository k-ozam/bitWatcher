package jp.maskedronin.bitwatcher.data.repository

import jp.maskedronin.bitwatcher.data.db.dao.PropertyDao
import jp.maskedronin.bitwatcher.data.source.PropertyRemoteDataSource
import jp.maskedronin.bitwatcher.domain.entity.Property
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val propertyRemoteDataSource: PropertyRemoteDataSource,
    private val propertyDao: PropertyDao
) : PropertyRepository {
    override fun getAllProperties(): Flow<List<Property>> = propertyDao.getAllProperties()

    override suspend fun findProperty(
        currency: Currency,
        exchange: Exchange
    ): Property? = propertyDao.findProperty(currency, exchange)

    override suspend fun updateProperty(property: Property) {
        propertyDao.upsert(property)
    }

    override suspend fun deleteProperty(property: Property) {
        propertyDao.delete(property)
    }

    override suspend fun addProperty(property: Property) {
        propertyDao.upsert(property)
    }

    override suspend fun refreshProperties(exchange: Exchange) {
        val newProperties = propertyRemoteDataSource.getProperties(exchange)
        propertyDao.deleteByExchange(exchange)
        propertyDao.upsert(newProperties)
    }
}