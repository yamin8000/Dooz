package io.github.yamin8000.dooz.common.domain.repository

import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting

interface SettingsRepository {

    suspend fun getFirstPlayerPolicy(): FirstPlayerPolicy
    suspend fun setFirstPlayerPolicy(policy: FirstPlayerPolicy)

    suspend fun getGamePlayersType(): GamePlayersType
    suspend fun setGamePlayerType(type: GamePlayersType)

    suspend fun getTheme(): ThemeSetting
    suspend fun setTheme(theme: ThemeSetting)

    suspend fun getGameSize(): Int
    suspend fun setGameSize(size: Int)

    suspend fun getFirstPlayerName(): String
    suspend fun setFirstPlayerName(name: String)

    suspend fun getFirstPlayerShape(): String
    suspend fun setFirstPlayerShape(shape: String)

    suspend fun getSecondPlayerName(): String
    suspend fun setSecondPlayerName(name: String)

    suspend fun getSecondPlayerShape(): String
    suspend fun setSecondPlayerShape(shape: String)

    suspend fun getAiDifficulty(): AiDifficulty
    suspend fun setAiDifficulty(difficulty: AiDifficulty)

    suspend fun getVibrationState(): Boolean
    suspend fun setVibrationState(state: Boolean)

    suspend fun getSoundState(): Boolean
    suspend fun setSoundState(state: Boolean)
}