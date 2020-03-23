package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.entity.Property
import jp.maskedronin.bitwatcher.domain.repository.PropertyRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Currency
import jp.maskedronin.bitwatcher.domain.valueobject.Exchange
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend operator fun invoke(
        currency: Currency,
        amount: Double,
        exchange: Exchange
    ) {
        propertyRepository.addProperty(
            Property(
                currency,
                amount,
                exchange,
                updatedAt = LocalDateTime.now()
            )
        )
    }
}