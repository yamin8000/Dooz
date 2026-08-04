package io.github.yamin8000.dooz.feature_settings.ui

import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting
import io.github.yamin8000.dooz.feature_settings.domain.model.SettingsTab

data class SettingsState(
    val currentTab: SettingsTab = SettingsTab.Game,
    val firstPlayerPolicy: FirstPlayerPolicy = FirstPlayerPolicy.DiceRolling,
    val gamePlayersType: GamePlayersType = GamePlayersType.PvC,
    val gameSize: Int = 0,
    val firstPlayerName: String = "",
    val secondPlayerName: String = "",
    val firstPlayerShape: String = "",
    val secondPlayerShape: String = "",
    val aiDifficulty: AiDifficulty = AiDifficulty.Easy,
    val theme: ThemeSetting = ThemeSetting.System,
    val isSoundOn: Boolean = true,
    val isVibrationOn: Boolean = true,
)
