package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetOnboardingCompletedUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(completed: Boolean) {
        settingsRepository.setOnboardingCompleted(completed)
    }
}