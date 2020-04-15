package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Tutorial
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetTutorialEnabled @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(tutorial: Tutorial, enabled: Boolean) {
        with(settingsRepository) {
            when (tutorial) {
                Tutorial.SWIPE_REFRESH -> setSwipeRefreshTutorialEnabled(enabled)
            }
        }
    }
}