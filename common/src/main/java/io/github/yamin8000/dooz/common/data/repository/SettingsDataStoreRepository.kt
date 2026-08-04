package io.github.yamin8000.dooz.common.data.repository

import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.ThemeSetting
import io.github.yamin8000.dooz.common.domain.repository.SettingsRepository
import io.github.yamin8000.dooz.common.util.Constants
import io.github.yamin8000.dooz.common.util.datastore.DataStoreHelper
import io.github.yamin8000.dooz.common.util.datastore.SettingsKeys

class SettingsDataStoreRepository(
    private val helper: DataStoreHelper
) : SettingsRepository {

    override suspend fun getFirstPlayerPolicy(): FirstPlayerPolicy {
        return FirstPlayerPolicy.valueOf(
            helper.getString(SettingsKeys.FIRST_PLAYER_POLICY) ?: FirstPlayerPolicy.DiceRolling.name
        )
    }

    override suspend fun setFirstPlayerPolicy(policy: FirstPlayerPolicy) {
        helper.setString(SettingsKeys.FIRST_PLAYER_POLICY, policy.name)
    }

    override suspend fun getGamePlayersType(): GamePlayersType {
        return GamePlayersType.valueOf(
            helper.getString(SettingsKeys.GAME_PLAYERS_TYPE) ?: GamePlayersType.PvC.name
        )
    }

    override suspend fun setGamePlayerType(type: GamePlayersType) {
        helper.setString(SettingsKeys.GAME_PLAYERS_TYPE, type.name)
    }

    override suspend fun getTheme(): ThemeSetting {
        return ThemeSetting.valueOf(
            helper.getString(SettingsKeys.THEME) ?: ThemeSetting.System.name
        )
    }

    override suspend fun setTheme(theme: ThemeSetting) {
        helper.setString(SettingsKeys.THEME, theme.name)
    }

    override suspend fun getGameSize(): Int {
        return helper.getInt(SettingsKeys.GAME_SIZE) ?: Constants.GAME_DEFAULT_SIZE
    }

    override suspend fun setGameSize(size: Int) {
        helper.setInt(SettingsKeys.GAME_SIZE, size)
    }

    override suspend fun getFirstPlayerName(): String {
        return helper.getString(SettingsKeys.FIRST_PLAYER_NAME) ?: ""
    }

    override suspend fun setFirstPlayerName(name: String) {
        helper.setString(SettingsKeys.FIRST_PLAYER_NAME, name)
    }

    override suspend fun getFirstPlayerShape(): String {
        return helper.getString(SettingsKeys.FIRST_PLAYER_SHAPE) ?: Constants.Shapes.X_SHAPE
    }

    override suspend fun setFirstPlayerShape(shape: String) {
        helper.setString(SettingsKeys.FIRST_PLAYER_SHAPE, shape)
    }

    override suspend fun getSecondPlayerName(): String {
        return helper.getString(SettingsKeys.SECOND_PLAYER_NAME) ?: ""
    }

    override suspend fun setSecondPlayerName(name: String) {
        helper.setString(SettingsKeys.SECOND_PLAYER_NAME, name)
    }

    override suspend fun getSecondPlayerShape(): String {
        return helper.getString(SettingsKeys.SECOND_PLAYER_SHAPE) ?: Constants.Shapes.RING_SHAPE
    }

    override suspend fun setSecondPlayerShape(shape: String) {
        helper.setString(SettingsKeys.SECOND_PLAYER_SHAPE, shape)
    }

    override suspend fun getAiDifficulty(): AiDifficulty {
        return AiDifficulty.valueOf(
            helper.getString(SettingsKeys.AI_DIFFICULTY) ?: AiDifficulty.Easy.name
        )
    }

    override suspend fun setAiDifficulty(difficulty: AiDifficulty) {
        helper.setString(SettingsKeys.AI_DIFFICULTY, difficulty.name)
    }

    override suspend fun getVibrationState(): Boolean {
        return helper.getBoolean(SettingsKeys.IS_VIBRATION_ON) ?: true
    }

    override suspend fun setVibrationState(state: Boolean) {
        helper.setBoolean(SettingsKeys.IS_VIBRATION_ON, state)
    }

    override suspend fun getSoundState(): Boolean {
        return helper.getBoolean(SettingsKeys.IS_SOUND_ON) ?: true
    }

    override suspend fun setSoundState(state: Boolean) {
        helper.setBoolean(SettingsKeys.IS_SOUND_ON, state)
    }
}