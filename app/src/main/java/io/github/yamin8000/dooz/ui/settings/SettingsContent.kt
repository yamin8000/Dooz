/*
 *     Dooz
 *     SettingsContent.kt Created/Updated by Yamin Siahmargooei at 2022/9/6
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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.game.GameConstants
import io.github.yamin8000.dooz.game.GamePlayersType
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.DoozTheme

@Preview(showBackground = true)
@Composable
fun SettingsContent(
    navController: NavController? = null
) {
    val settingsState = rememberSettingsState()

    DoozTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                PersianText(
                    text = stringResource(R.string.settings),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                GamePlayersTypeSwitch(settingsState)
                GameSizeChanger(
                    gameSize = settingsState.gameSize.value,
                    onGameSizeIncrease = { settingsState.increaseGameSize() },
                    onGameSizeDecrease = { settingsState.decreaseGameSize() }
                )
                PlayerNameInputs(settingsState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNameInputs(
    settingsState: SettingsState
) {
    Card {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersianText(
                text = stringResource(R.string.player_names),
                fontSize = 16.sp
            )
            TextField(
                value = settingsState.firstPlayerName.value,
                onValueChange = { settingsState.firstPlayerName.value = it })
            TextField(
                value = settingsState.secondPlayerName.value,
                onValueChange = { settingsState.secondPlayerName.value = it })
            Button(
                onClick = {
                    settingsState.savePlayerNames()
                }) {
                PersianText(text = stringResource(R.string.save))
            }
        }
    }
}

@Composable
private fun GameSizeChanger(
    gameSize: Int = GameConstants.gameDefaultSize,
    onGameSizeIncrease: () -> Unit = {},
    onGameSizeDecrease: () -> Unit = {}
) {
    Card {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersianText(
                text = stringResource(R.string.game_board_size),
                fontSize = 18.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = onGameSizeIncrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(R.string.increase)
                    )
                }
                Text("$gameSize*$gameSize")
                IconButton(onClick = onGameSizeDecrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove),
                        contentDescription = stringResource(R.string.decrease)
                    )
                }
            }
        }
    }
}

@Composable
private fun GamePlayersTypeSwitch(
    settingsState: SettingsState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PersianText(switchText(settingsState.gamePlayersType.value))
        Switch(
            checked = settingsState.gamePlayersType.value == GamePlayersType.PvP,
            onCheckedChange = { isChecked -> onSwitchCheckedChanged(isChecked, settingsState) }
        )
    }
}

private fun onSwitchCheckedChanged(
    isChecked: Boolean,
    settingsState: SettingsState
) {
    if (isChecked) settingsState.gamePlayersType.value = GamePlayersType.PvP
    else settingsState.gamePlayersType.value = GamePlayersType.PvC
    settingsState.setPlayersType()
}

@Composable
private fun switchText(
    gamePlayersType: GamePlayersType
): String {
    return if (gamePlayersType == GamePlayersType.PvP) stringResource(R.string.play_with_human)
    else stringResource(R.string.play_with_computer)
}
