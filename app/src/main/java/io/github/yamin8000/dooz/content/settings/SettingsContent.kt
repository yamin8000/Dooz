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

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.game.GamePlayersType
import io.github.yamin8000.dooz.game.ai.AiDifficulty
import io.github.yamin8000.dooz.ui.ClickableShapes
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.getGamePlayersTypeCaption
import io.github.yamin8000.dooz.ui.shapes
import io.github.yamin8000.dooz.ui.theme.DoozTheme
import io.github.yamin8000.dooz.ui.theme.Samim
import io.github.yamin8000.dooz.ui.toName
import io.github.yamin8000.dooz.ui.toShape
import io.github.yamin8000.dooz.util.Constants

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
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
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                PersianText(
                    text = stringResource(R.string.settings),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                )
                GamePlayersTypeSwitch(
                    gamePlayersType = settingsState.gamePlayersType,
                    onCheckedChanged = { settingsState.setPlayersType() }
                )
                GameSizeChanger(
                    gameSize = settingsState.gameSize.value,
                    onGameSizeIncrease = { settingsState.increaseGameSize() },
                    onGameSizeDecrease = { settingsState.decreaseGameSize() }
                )
                AiDifficultyCard(
                    aiDifficulty = settingsState.aiDifficulty,
                    onDifficultyChanged = { settingsState.setAiDifficulty() }
                )
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

@Composable
fun AiDifficultyCard(
    aiDifficulty: MutableState<AiDifficulty>,
    onDifficultyChanged: () -> Unit
) {
    val radioOptions = listOf(AiDifficulty.Hard, AiDifficulty.Medium, AiDifficulty.Easy)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(aiDifficulty.value) }
    Card {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            PersianText(
                text = stringResource(R.string.ai_difficulty),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                radioOptions.forEach { difficulty ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .selectable(
                                selected = (difficulty == selectedOption),
                                onClick = {
                                    onOptionSelected(difficulty)
                                    aiDifficulty.value = difficulty
                                    onDifficultyChanged()
                                },
                                role = Role.RadioButton
                            )
                    ) {
                        PersianText(
                            text = difficulty.persianName,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                        )
                        RadioButton(
                            selected = (difficulty == selectedOption),
                            onClick = null,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun PlayerCustomization(
    firstPlayerName: MutableState<String>,
    secondPlayerName: MutableState<String>,
    firstPlayerShape: MutableState<String>,
    secondPlayerShape: MutableState<String>,
    errorText: MutableState<String?>,
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
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            PlayerNamesCustomizer(firstPlayerName, secondPlayerName)
            PersianText(
                text = stringResource(R.string.player_shapes),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )
            PlayerShapesCustomizer(firstPlayerShape, secondPlayerShape)
            Button(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .padding(top = 16.dp),
                onClick = { onSave() }) {
                PersianText(text = stringResource(R.string.save))
            }
            errorText.value?.let { it ->
                PersianText(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun PlayerShapesCustomizer(
    firstPlayerShape: MutableState<String>,
    secondPlayerShape: MutableState<String>
) {
    ClickableShapes(
        shapes = shapes,
        lastSelectedShape = firstPlayerShape.value.toShape(),
        header = { PersianText(stringResource(R.string.first_player_shape)) }
    ) { shape -> firstPlayerShape.value = shape.toName() ?: Constants.Shapes.ringShape }
    ClickableShapes(
        shapes = shapes,
        lastSelectedShape = secondPlayerShape.value.toShape(),
        header = { PersianText(stringResource(R.string.second_player_shape)) }
    ) { shape -> secondPlayerShape.value = shape.toName() ?: Constants.Shapes.xShape }
}

@Composable
private fun PlayerNamesCustomizer(
    firstPlayerName: MutableState<String>,
    secondPlayerName: MutableState<String>
) {
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
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameField(
    modifier: Modifier = Modifier,
    label: String = "label",
    placeholder: String = "placeholder",
    value: String = "value",
    onValueChange: (String) -> Unit = {}
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { NameFieldChildArranger { PersianText(label) } },
        placeholder = { NameFieldChildArranger { PersianText(placeholder) } },
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
private fun NameFieldChildArranger(
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) { content() }
}

@Composable
private fun GameSizeChanger(
    gameSize: Int,
    onGameSizeIncrease: () -> Unit,
    onGameSizeDecrease: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PersianText(
                text = stringResource(R.string.game_board_size),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = onGameSizeDecrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove),
                        contentDescription = stringResource(R.string.decrease),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Text("$gameSize*$gameSize")
                IconButton(onClick = onGameSizeIncrease) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = stringResource(R.string.increase),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun GamePlayersTypeSwitch(
    gamePlayersType: MutableState<GamePlayersType>,
    onCheckedChanged: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PersianText(getGamePlayersTypeCaption(gamePlayersType.value))
        Switch(
            checked = gamePlayersType.value == GamePlayersType.PvP,
            onCheckedChange = { isChecked ->
                onSwitchCheckedChanged(isChecked, gamePlayersType)
                onCheckedChanged()
            }
        )
    }
}

private fun onSwitchCheckedChanged(
    isChecked: Boolean,
    gamePlayersType: MutableState<GamePlayersType>,
) {
    if (isChecked) gamePlayersType.value = GamePlayersType.PvP
    else gamePlayersType.value = GamePlayersType.PvC
}
