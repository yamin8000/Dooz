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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings
import io.github.yamin8000.dooz.game.FirstPlayerPolicy
import io.github.yamin8000.dooz.game.GameConstants
import io.github.yamin8000.dooz.model.AiDifficulty
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.util.Constants
import io.github.yamin8000.dooz.util.DataStoreHelper
import kotlinx.coroutines.launch

class SettingsState(
    private val context: Context,
    private val scope: LifecycleCoroutineScope,
    private val _gamePlayersType: MutableState<GamePlayersType>,
    private val _gameSize: MutableIntState,
    private val _firstPlayerName: MutableState<String>,
    private val _secondPlayerName: MutableState<String>,
    private val _firstPlayerShape: MutableState<String>,
    private val _secondPlayerShape: MutableState<String>,
    private val _aiDifficulty: MutableState<AiDifficulty>,
    private val _themeSetting: MutableState<ThemeSetting>,
    private val _firstPlayerPolicy: MutableState<FirstPlayerPolicy>,
    private var _isSoundOn: MutableState<Boolean>,
    private val _isVibrationOn: MutableState<Boolean>
) {
    var gamePlayersType: GamePlayersType
        get() = _gamePlayersType.value
        set(value) {
            _gamePlayersType.value = value
            scope.launch { dataStore.setString(Constants.gamePlayersType, value.name) }
        }

    var gameSize: Int
        get() = _gameSize.intValue
        set(value) {
            if (value in GameConstants.gameSizeRange) {
                _gameSize.intValue = value
                scope.launch { dataStore.setInt(Constants.gameSize, value) }
            }
        }

    var firstPlayerName: String
        get() = _firstPlayerName.value
        set(value) {
            _firstPlayerName.value = value
        }

    var secondPlayerName: String
        get() = _secondPlayerName.value
        set(value) {
            _secondPlayerName.value = value
        }

    var firstPlayerShape: String
        get() = _firstPlayerShape.value
        set(value) {
            _firstPlayerShape.value = value
            if (isPairValid(value, secondPlayerShape)) {
                _firstPlayerShape.value = value
                scope.launch { dataStore.setString(Constants.firstPlayerShape, value) }
            } else scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.player_shapes_equal)) }
        }

    var secondPlayerShape: String
        get() = _secondPlayerShape.value
        set(value) {
            if (isPairValid(firstPlayerShape, value)) {
                _secondPlayerShape.value = value
                scope.launch { dataStore.setString(Constants.secondPlayerShape, value) }
            } else scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.player_shapes_equal)) }
        }

    var aiDifficulty: AiDifficulty
        get() = _aiDifficulty.value
        set(value) {
            _aiDifficulty.value = value
            scope.launch { dataStore.setString(Constants.aiDifficulty, value.name) }
        }

    var themeSetting: ThemeSetting
        get() = _themeSetting.value
        set(value) {
            _themeSetting.value = value
            scope.launch { dataStore.setString(Constants.theme, value.name) }
        }

    var firstPlayerPolicy: FirstPlayerPolicy
        get() = _firstPlayerPolicy.value
        set(value) {
            _firstPlayerPolicy.value = value
            scope.launch { dataStore.setString(Constants.firstPlayerPolicy, value.name) }
        }

    var isSoundOn: Boolean
        get() = _isSoundOn.value
        set(value) {
            _isSoundOn.value = value
            scope.launch { dataStore.setBoolean(Constants.isSoundOn, value) }
        }

    var isVibrationOn: Boolean
        get() = _isVibrationOn.value
        set(value) {
            _isVibrationOn.value = value
            scope.launch { dataStore.setBoolean(Constants.isVibrationOn, value) }
        }

    fun saveNames() {
        if (isPairValid(_firstPlayerName.value, _secondPlayerName.value)) {
            scope.launch {
                dataStore.setString(Constants.firstPlayerName, _firstPlayerName.value.trim())
                dataStore.setString(Constants.secondPlayerName, _secondPlayerName.value.trim())
                snackbarHostState.showSnackbar(context.getString(R.string.data_saved))
            }
        } else scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.player_names_equal_or_empty)) }
    }

    private fun isPairValid(
        first: String,
        second: String
    ) = if (first.isEmpty() || second.isEmpty()) false else first.trim() != second.trim()

    val snackbarHostState: SnackbarHostState = SnackbarHostState()

    private val dataStore = DataStoreHelper(context.settings)

    init {
        val defaultFirstPlayerName = context.getString(R.string.first_player_default_name)
        val defaultSecondPlayerName = context.getString(R.string.second_player_default_name)

        scope.launch {
            _themeSetting.value = ThemeSetting.valueOf(
                dataStore.getString(Constants.theme) ?: ThemeSetting.System.name
            )
            _gamePlayersType.value = GamePlayersType.valueOf(
                dataStore.getString(Constants.gamePlayersType) ?: GamePlayersType.PvC.name
            )
            _gameSize.intValue = dataStore.getInt(Constants.gameSize) ?: GameConstants.gameDefaultSize
            _firstPlayerName.value =
                dataStore.getString(Constants.firstPlayerName) ?: defaultFirstPlayerName
            _secondPlayerName.value =
                dataStore.getString(Constants.secondPlayerName) ?: defaultSecondPlayerName
            _firstPlayerShape.value =
                dataStore.getString(Constants.firstPlayerShape) ?: Constants.Shapes.xShape
            _secondPlayerShape.value =
                dataStore.getString(Constants.secondPlayerShape) ?: Constants.Shapes.ringShape
            _aiDifficulty.value = AiDifficulty.valueOf(
                dataStore.getString(Constants.aiDifficulty) ?: AiDifficulty.Easy.name
            )
            _firstPlayerPolicy.value = FirstPlayerPolicy.valueOf(
                dataStore.getString(Constants.firstPlayerPolicy)
                    ?: FirstPlayerPolicy.DiceRolling.name
            )
            _isSoundOn.value = dataStore.getBoolean(Constants.isSoundOn) ?: true
            _isVibrationOn.value = dataStore.getBoolean(Constants.isVibrationOn) ?: true
        }
    }
}

@Composable
fun rememberSettingsState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    gamePlayersType: MutableState<GamePlayersType> = rememberSaveable {
        mutableStateOf(GamePlayersType.PvC)
    },
    gameSize: MutableIntState = rememberSaveable { mutableIntStateOf(GameConstants.gameDefaultSize) },
    firstPlayerName: MutableState<String> = rememberSaveable { mutableStateOf(context.getString(R.string.first_player_default_name)) },
    secondPlayerName: MutableState<String> = rememberSaveable { mutableStateOf(context.getString(R.string.second_player_default_name)) },
    firstPlayerShape: MutableState<String> = rememberSaveable { mutableStateOf(Constants.Shapes.xShape) },
    secondPlayerShape: MutableState<String> = rememberSaveable { mutableStateOf(Constants.Shapes.ringShape) },
    aiDifficulty: MutableState<AiDifficulty> = rememberSaveable { mutableStateOf(AiDifficulty.Easy) },
    themeSetting: MutableState<ThemeSetting> = rememberSaveable { mutableStateOf(ThemeSetting.System) },
    firstPlayerPolicy: MutableState<FirstPlayerPolicy> = rememberSaveable {
        mutableStateOf(FirstPlayerPolicy.DiceRolling)
    },
    isSoundOn: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) },
    isVibrationOn: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) }
) = remember(
    context,
    coroutineScope,
    gamePlayersType,
    gameSize,
    firstPlayerName,
    secondPlayerName,
    firstPlayerShape,
    secondPlayerShape,
    aiDifficulty,
    themeSetting,
    firstPlayerPolicy,
    isSoundOn,
    isVibrationOn
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
        aiDifficulty,
        themeSetting,
        firstPlayerPolicy,
        isSoundOn,
        isVibrationOn
    )
}