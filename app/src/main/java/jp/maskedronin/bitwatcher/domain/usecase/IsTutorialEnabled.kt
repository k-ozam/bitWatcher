package jp.maskedronin.bitwatcher.domain.usecase

import jp.maskedronin.bitwatcher.domain.repository.SettingsRepository
import jp.maskedronin.bitwatcher.domain.valueobject.Tutorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IsTutorialEnabled @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(tutorial: Tutorial): Flow<Boolean> = when (tutorial) {
        Tutorial.SWIPE_REFRESH -> settingsRepository.isSwipeRefreshTutorialEnabled()
            .map { enabled -> enabled ?: true }
    }
}