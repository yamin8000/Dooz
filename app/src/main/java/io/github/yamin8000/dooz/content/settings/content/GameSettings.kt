/*
 *     Dooz
 *     GameSettings.kt Created/Updated by Yamin Siahmargooei at 2022/9/26
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.game.FirstPlayerPolicy
import io.github.yamin8000.dooz.model.AiDifficulty
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.ui.composables.InfoCard
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.RadioGroup
import io.github.yamin8000.dooz.util.Constants

@Composable
internal fun AiDifficultyCard(
    aiDifficulty: AiDifficulty,
    onDifficultyChanged: (AiDifficulty) -> Unit
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        columnModifier = Modifier.fillMaxWidth(),
        header = {
            PersianText(
                text = stringResource(R.string.ai_difficulty),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        content = {
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Constants.difficulties.forEach { difficulty ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .selectable(
                                selected = (difficulty == aiDifficulty),
                                onClick = { onDifficultyChanged(difficulty) },
                                role = Role.RadioButton
                            )
                    ) {
                        PersianText(
                            text = stringResource(difficulty.persianNameStringResource),
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                        )
                        RadioButton(
                            selected = (difficulty == aiDifficulty),
                            onClick = null,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
internal fun GeneralGameSettings(
    gamePlayersType: GamePlayersType,
    onPlayerTypeChange: (GamePlayersType) -> Unit,
    firstPlayerPolicy: FirstPlayerPolicy,
    onFirstPlayerPolicyChange: (FirstPlayerPolicy) -> Unit
) {
    val resources = LocalContext.current.resources
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        columnModifier = Modifier.fillMaxWidth(),
        header = {
            PersianText(
                text = stringResource(R.string.game_general_settings),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        content = {
            RadioGroup(
                options = GamePlayersType.values().toList(),
                currentOption = gamePlayersType,
                onOptionChange = onPlayerTypeChange,
                optionStringProvider = { resources.getString(it.persianNameStringResource) }
            )
            RadioGroup(
                options = FirstPlayerPolicy.values().toList(),
                currentOption = firstPlayerPolicy,
                onOptionChange = onFirstPlayerPolicyChange,
                optionStringProvider = { resources.getString(it.persianNameStringResource) }
            )
        }
    )
}

@Composable
internal fun GameSizeChanger(
    gameSize: Int,
    onGameSizeIncrease: () -> Unit,
    onGameSizeDecrease: () -> Unit
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        columnModifier = Modifier.fillMaxWidth(),
        header = {
            PersianText(
                text = stringResource(R.string.game_board_size),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = onGameSizeDecrease) {
                    Icon(
                        imageVector = Icons.TwoTone.Remove,
                        contentDescription = stringResource(R.string.decrease),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Text("$gameSize*$gameSize")
                IconButton(onClick = onGameSizeIncrease) {
                    Icon(
                        imageVector = Icons.TwoTone.Add,
                        contentDescription = stringResource(R.string.increase),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    )
}