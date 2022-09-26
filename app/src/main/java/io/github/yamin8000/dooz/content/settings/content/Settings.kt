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

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings.ThemeSetting
import io.github.yamin8000.dooz.content.settings.rememberSettingsState
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.PreviewTheme

@Composable
fun Settings(
    onThemeChanged: (ThemeSetting) -> Unit
) {
    val settingsState = rememberSettingsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val tabTitles = listOf(
                stringResource(R.string.general),
                stringResource(R.string.game),
                stringResource(R.string.players)
            )

            val tabIndex = rememberSaveable { mutableStateOf(1) }
            ScrollableTabRow(
                selectedTabIndex = tabIndex.value,
                tabs = {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = tabIndex.value == index,
                            onClick = { tabIndex.value = index },
                            text = { PersianText(title) })
                    }
                }
            )
            when (tabTitles[tabIndex.value]) {
                stringResource(R.string.general) -> {
                    ThemeChanger(settingsState.themeSetting.value) { newTheme ->
                        settingsState.setThemeSetting(newTheme)
                        onThemeChanged(newTheme)
                    }
                }
                stringResource(R.string.game) -> {
                    GeneralGameSettings(
                        gamePlayersType = settingsState.gamePlayersType.value,
                        onPlayerTypeChange = { settingsState.setPlayersType(it) },
                        firstPlayerPolicy = settingsState.firstPlayerPolicy.value,
                        onFirstPlayerPolicyChange = { settingsState.setFirstPlayerPolicy(it) }
                    )
                    GameSizeChanger(
                        gameSize = settingsState.gameSize.value,
                        onGameSizeIncrease = { settingsState.increaseGameSize() },
                        onGameSizeDecrease = { settingsState.decreaseGameSize() }
                    )
                    AiDifficultyCard(
                        aiDifficulty = settingsState.aiDifficulty.value,
                        onDifficultyChanged = { settingsState.setAiDifficulty(it) }
                    )
                }
                stringResource(R.string.players) -> {
                    PlayerCustomization(
                        firstPlayerName = settingsState.firstPlayerName,
                        secondPlayerName = settingsState.secondPlayerName,
                        firstPlayerShape = settingsState.firstPlayerShape,
                        secondPlayerShape = settingsState.secondPlayerShape,
                        errorText = settingsState.errorText,
                        onSave = { settingsState.savePlayerInfo() }
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme { Settings {} }
}