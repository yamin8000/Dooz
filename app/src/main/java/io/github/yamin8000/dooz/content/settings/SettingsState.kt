/*
 *     Dooz
 *     SettingsState.kt Created/Updated by Yamin Siahmargooei at 2022/9/6
 *     This file is part of Dooz.
 *     Copyright (C) 2022  Yamin Siahmargooei
 *
 *     Dooz is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Dooz is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Dooz.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.yamin8000.dooz.content.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings
import io.github.yamin8000.dooz.game.GameConstants
import io.github.yamin8000.dooz.model.AiDifficulty
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.util.Constants
import io.github.yamin8000.dooz.util.DataStoreHelper
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsState(
    private val context: Context,
    val coroutineScope: LifecycleCoroutineScope,
    var gamePlayersType: MutableState<GamePlayersType>,
    var gameSize: MutableState<Int>,
    var firstPlayerName: MutableState<String>,
    var secondPlayerName: MutableState<String>,
    var firstPlayerShape: MutableState<String>,
    var secondPlayerShape: MutableState<String>,
    val errorText: MutableState<String?>,
    var aiDifficulty: MutableState<AiDifficulty>,
    val themeSetting: MutableState<ThemeSetting>
) {

    private val dataStore = DataStoreHelper(context.settings)

    init {
        coroutineScope.launch {
            themeSetting.value = ThemeSetting.valueOf(
                dataStore.getString(Constants.theme) ?: ThemeSetting.System.name
            )
            gamePlayersType.value =
                GamePlayersType.valueOf(getPlayersType() ?: GamePlayersType.PvC.name)
            gameSize.value = getGameSize() ?: GameConstants.gameDefaultSize
            firstPlayerName.value = dataStore.getString(Constants.firstPlayerName) ?: "Player 1"
            secondPlayerName.value = dataStore.getString(Constants.secondPlayerName) ?: "Player 2"
            firstPlayerShape.value =
                dataStore.getString(Constants.firstPlayerShape) ?: Constants.Shapes.xShape
            secondPlayerShape.value =
                dataStore.getString(Constants.secondPlayerShape) ?: Constants.Shapes.ringShape
            aiDifficulty.value = AiDifficulty.valueOf(getAiDifficulty() ?: AiDifficulty.Easy.name)
        }
    }

    suspend fun updateThemeSetting(
        newTheme: ThemeSetting
    ) {
        themeSetting.value = newTheme
        coroutineScope.launch {
            dataStore.setString(Constants.theme, newTheme.name)
        }
    }

    fun setPlayersType() {
        coroutineScope.launch {
            dataStore.setString(Constants.gamePlayersType, gamePlayersType.value.name)
        }
    }

    fun increaseGameSize() {
        if (gameSize.value < GameConstants.gameMaxSize) {
            gameSize.value = gameSize.value + 1
            setGameSize()
        }
    }

    fun decreaseGameSize() {
        if (gameSize.value > GameConstants.gameDefaultSize) {
            gameSize.value = gameSize.value - 1
            setGameSize()
        }
    }

    fun savePlayerInfo() {
        if (firstPlayerShape.value == secondPlayerShape.value) {
            errorText.value = context.getString(R.string.player_shapes_equal)
        } else if (firstPlayerName.value == secondPlayerName.value) {
            errorText.value = context.getString(R.string.player_names_equal)
        } else {
            errorText.value = null
            coroutineScope.launch {
                context.settings.edit {
                    it[stringPreferencesKey(Constants.firstPlayerName)] = firstPlayerName.value
                    it[stringPreferencesKey(Constants.secondPlayerName)] = secondPlayerName.value
                    it[stringPreferencesKey(Constants.firstPlayerShape)] = firstPlayerShape.value
                    it[stringPreferencesKey(Constants.secondPlayerShape)] = secondPlayerShape.value
                }
            }
        }
    }

    private fun setGameSize() {
        coroutineScope.launch {
            dataStore.setInt(Constants.gameSize, gameSize.value)
        }
    }

    private suspend fun getGameSize() = withContext(coroutineScope.coroutineContext) {
        dataStore.getInt(Constants.gameSize)
    }

    private suspend fun getPlayersType() = withContext(coroutineScope.coroutineContext) {
        dataStore.getString(Constants.gamePlayersType)
    }

    fun setAiDifficulty() {
        coroutineScope.launch {
            dataStore.setString(Constants.aiDifficulty, aiDifficulty.value.name)
        }
    }

    private suspend fun getAiDifficulty() = withContext(coroutineScope.coroutineContext) {
        dataStore.getString(Constants.aiDifficulty)
    }
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    gamePlayersType: MutableState<GamePlayersType> = rememberSaveable {
        mutableStateOf(
            GamePlayersType.PvC
        )
    },
    gameSize: MutableState<Int> = rememberSaveable { mutableStateOf(GameConstants.gameDefaultSize) },
    firstPlayerName: MutableState<String> = rememberSaveable { mutableStateOf("Player 1") },
    secondPlayerName: MutableState<String> = rememberSaveable { mutableStateOf("Player 2") },
    firstPlayerShape: MutableState<String> = rememberSaveable { mutableStateOf(Constants.Shapes.xShape) },
    secondPlayerShape: MutableState<String> = rememberSaveable { mutableStateOf(Constants.Shapes.ringShape) },
    errorText: MutableState<String?> = rememberSaveable { mutableStateOf(null) },
    aiDifficulty: MutableState<AiDifficulty> = rememberSaveable { mutableStateOf(AiDifficulty.Easy) },
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) }
) = remember(
    context,
    coroutineScope,
    gamePlayersType,
    gameSize,
    firstPlayerName,
    secondPlayerName,
    firstPlayerShape,
    secondPlayerShape,
    errorText,
    aiDifficulty,
    themeSetting
) {
    SettingsState(
        context,
        coroutineScope,
        gamePlayersType,
        gameSize,
        firstPlayerName,
        secondPlayerName,
        firstPlayerShape,
        secondPlayerShape,
        errorText,
        aiDifficulty,
        themeSetting
    )
}