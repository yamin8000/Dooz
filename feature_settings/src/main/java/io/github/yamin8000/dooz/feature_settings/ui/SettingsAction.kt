package io.github.yamin8000.dooz.feature_settings.ui

import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting
import io.github.yamin8000.dooz.feature_settings.domain.model.SettingsTab

sealed interface SettingsAction {
    data class OnTabChanged(val tab: SettingsTab) : SettingsAction
    data class OnUpdateGamePlayerType(val type: GamePlayersType) : SettingsAction
    data class OnUpdateGameSize(val size: Int) : SettingsAction
    data class OnUpdateFirstPlayerName(val name: String) : SettingsAction
    data class OnUpdateSecondPlayerName(val name: String) : SettingsAction
    data class OnUpdateFirstPlayerShape(val shape: String) : SettingsAction
    data class OnUpdateSecondPlayerShape(val shape: String) : SettingsAction
    data class OnUpdateAiDifficulty(val difficulty: AiDifficulty) : SettingsAction
    data class OnUpdateTheme(val theme: ThemeSetting) : SettingsAction
    data class OnUpdateFirstPlayerPolicy(val policy: FirstPlayerPolicy) : SettingsAction
    data class OnUpdateSoundState(val state: Boolean) : SettingsAction
    data class OnUpdateVibrationState(val state: Boolean) : SettingsAction
}