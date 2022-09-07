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

package io.github.yamin8000.dooz.content.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.game.GameConstants
import io.github.yamin8000.dooz.game.GamePlayersType
import io.github.yamin8000.dooz.ui.ClickableShapes
import io.github.yamin8000.dooz.ui.RingShape
import io.github.yamin8000.dooz.ui.TriangleShape
import io.github.yamin8000.dooz.ui.XShape
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.DoozTheme
import io.github.yamin8000.dooz.ui.theme.Samim

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
                PlayerCustomization(
                    firstPlayerName = settingsState.firstPlayerName,
                    secondPlayerName = settingsState.secondPlayerName,
                    onSave = { settingsState.savePlayerNames() }
                )
            }
        }
    }
}

@Composable
fun PlayerCustomization(
    firstPlayerName: MutableState<String>,
    secondPlayerName: MutableState<String>,
    onSave: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersianText(
                text = stringResource(R.string.player_names),
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NameField(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.second_player_name),
                    placeholder = stringResource(R.string.enter_name),
                    value = secondPlayerName.value,
                    onValueChange = { secondPlayerName.value = it }
                )
                NameField(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.first_player_name),
                    placeholder = stringResource(R.string.enter_name),
                    value = firstPlayerName.value,
                    onValueChange = { firstPlayerName.value = it }
                )
            }
            PersianText(
                text = stringResource(R.string.player_shapes),
                fontSize = 16.sp
            )
            ClickableShapes(
                shapes = listOf(
                    CircleShape, RectangleShape, TriangleShape, RingShape,
                    XShape
                )
            ) { index, shape ->

            }
            Button(
                onClick = { onSave() }) {
                PersianText(text = stringResource(R.string.save))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { PersianText(label, modifier = Modifier.fillMaxWidth()) },
        placeholder = { PersianText(placeholder, modifier = Modifier.fillMaxWidth()) },
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = Samim,
            textAlign = TextAlign.Right,
            textDirection = TextDirection.Rtl
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
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
                IconButton(onClick = onGameSizeDecrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove),
                        contentDescription = stringResource(R.string.decrease)
                    )
                }
                Text("$gameSize*$gameSize")
                IconButton(onClick = onGameSizeIncrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(R.string.increase)
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
