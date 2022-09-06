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

package io.github.yamin8000.dooz.ui.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.dooz.game.GameConstants
import io.github.yamin8000.dooz.game.GamePlayersType
import io.github.yamin8000.dooz.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.settings: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsState(
    private val context: Context,
    private val coroutineScope: LifecycleCoroutineScope,
    var gamePlayersType: MutableState<GamePlayersType>,
    var gameSize: MutableState<Int>,
    var firstPlayerName: MutableState<String>,
    var secondPlayerName: MutableState<String>
) {
    init {
        coroutineScope.launch {
            gamePlayersType.value = GamePlayersType.valueOf(getPlayersType() ?: "PvC")
            gameSize.value = getGameSize() ?: GameConstants.gameDefaultSize
            firstPlayerName.value = getFirstPlayerName() ?: "Player 1"
            secondPlayerName.value = getSecondPlayerName() ?: "Player 2"
        }
    }


    private suspend fun getSecondPlayerName(): String? {
        return getPlayerName(Constants.secondPlayerName)
    }

    private suspend fun getFirstPlayerName(): String? {
        return getPlayerName(Constants.firstPlayerName)
    }

    private suspend fun getPlayerName(
        player: String
    ): String? {
        return context.settings.data.map {
            it[stringPreferencesKey(player)]
        }.first()
    }

    fun setPlayersType() {
        coroutineScope.launch {
            context.settings.edit {
                it[stringPreferencesKey(Constants.gamePlayersType)] = gamePlayersType.value.name
            }
        }
    }

    fun increaseGameSize() {
        gameSize.value = gameSize.value + 1
        setGameSize()
    }

    fun decreaseGameSize() {
        if (gameSize.value > GameConstants.gameDefaultSize) {
            gameSize.value = gameSize.value - 1
            setGameSize()
        }
    }

    fun savePlayerNames() {
        coroutineScope.launch {
            context.settings.edit {
                it[stringPreferencesKey(Constants.firstPlayerName)] = firstPlayerName.value
                it[stringPreferencesKey(Constants.secondPlayerName)] = secondPlayerName.value
            }
        }
    }

    private fun setGameSize() {
        coroutineScope.launch {
            context.settings.edit {
                it[intPreferencesKey(Constants.gameSize)] = gameSize.value
            }
        }
    }

    private suspend fun getGameSize(): Int? {
        return context.settings.data.map {
            it[intPreferencesKey(Constants.gameSize)]
        }.first()
    }

    private suspend fun getPlayersType() = context.settings.data.map {
        it[stringPreferencesKey(Constants.gamePlayersType)]
    }.first()
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
    secondPlayerName: MutableState<String> = rememberSaveable { mutableStateOf("Player 2") }
) = remember(
    context,
    coroutineScope,
    gamePlayersType,
    gameSize,
    firstPlayerName,
    secondPlayerName
) {
    SettingsState(
        context,
        coroutineScope,
        gamePlayersType,
        gameSize,
        firstPlayerName,
        secondPlayerName
    )
}