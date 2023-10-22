/*
 *     Dooz
 *     Settings.kt Created/Updated by Yamin Siahmargooei at 2022/9/26
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

package io.github.yamin8000.dooz.content.settings.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings.ThemeSetting
import io.github.yamin8000.dooz.content.settings.rememberSettingsState
import io.github.yamin8000.dooz.ui.composables.MySnackbar
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.ScaffoldWithTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onThemeChanged: (ThemeSetting) -> Unit,
    onBackClick: () -> Unit
) {
    val state = rememberSettingsState()
    ScaffoldWithTitle(
        title = stringResource(R.string.settings),
        onBackClick = onBackClick,
        snackbarHost = {
            SnackbarHost(state.snackbarHostState) { data ->
                MySnackbar {
                    PersianText(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = data.visuals.message
                    )
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    val tabTitles = listOf(
                        stringResource(R.string.general),
                        stringResource(R.string.game),
                        stringResource(R.string.players)
                    )

                    val tabIndex = rememberSaveable { mutableIntStateOf(1) }
                    ScrollableTabRow(
                        selectedTabIndex = tabIndex.intValue,
                        tabs = {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = tabIndex.intValue == index,
                                    onClick = { tabIndex.intValue = index },
                                    text = { PersianText(title) })
                            }
                        }
                    )
                    when (tabTitles[tabIndex.intValue]) {
                        stringResource(R.string.general) -> {
                            ThemeChangerCard(state.themeSetting) { newTheme ->
                                state.themeSetting = newTheme
                                onThemeChanged(newTheme)
                            }
                            EffectsCard(
                                isSoundOn = state.isSoundOn,
                                isSoundOnChange = { state.isSoundOn = it },
                                isVibrationOn = state.isVibrationOn,
                                isVibrationOnChange = { state.isVibrationOn = it }
                            )
                        }

                        stringResource(R.string.game) -> {
                            GeneralGameSettings(
                                gamePlayersType = state.gamePlayersType,
                                onPlayerTypeChange = { state.gamePlayersType = it },
                                firstPlayerPolicy = state.firstPlayerPolicy,
                                onFirstPlayerPolicyChange = { state.firstPlayerPolicy = it }
                            )
                            AiDifficultyCard(
                                aiDifficulty = state.aiDifficulty,
                                onDifficultyChanged = { state.aiDifficulty = it }
                            )
                            GameSizeChanger(
                                gameSize = state.gameSize,
                                onGameSizeIncrease = { state.gameSize++ },
                                onGameSizeDecrease = { state.gameSize-- }
                            )
                        }

                        stringResource(R.string.players) -> {
                            PlayerCustomization(
                                onSave = { state.saveNames() },
                                firstPlayerName = state.firstPlayerName,
                                onFirstPlayerNameChange = { state.firstPlayerName = it },
                                secondPlayerName = state.secondPlayerName,
                                onSecondPlayerNameChange = { state.secondPlayerName = it },
                                firstPlayerShape = state.firstPlayerShape,
                                onFirstPlayerShapeChange = { state.firstPlayerShape = it },
                                secondPlayerShape = state.secondPlayerShape,
                                onSecondPlayerShapeChange = { state.secondPlayerShape = it }
                            )
                        }
                    }
                }
            }
        }
    )
}