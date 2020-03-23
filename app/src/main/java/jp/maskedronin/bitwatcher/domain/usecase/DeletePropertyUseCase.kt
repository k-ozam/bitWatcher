package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.Property
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeletePropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        currency: Currency,
        exchange: Exchange
    ) {
        val targetProperty: Property = propertyRepository.findProperty(currency, exchange)!!
        propertyRepository.deleteProperty(targetProperty)
    }
}