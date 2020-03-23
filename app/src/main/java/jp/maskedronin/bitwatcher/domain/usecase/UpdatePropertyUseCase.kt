package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        target: Pair<Currency, Exchange>,
        amount: Double
    ) {
        propertyRepository.findProperty(target.first, target.second)!!
            .copy(amount = amount)
            .also { modified ->
                propertyRepository.updateProperty(modified)
            }
    }
}