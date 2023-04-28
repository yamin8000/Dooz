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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.settings.SettingsItem
import io.github.yamin8000.dooz.content.settings.SettingsItemCard
import io.github.yamin8000.dooz.content.settings.SettingsSelectorDialog
import io.github.yamin8000.dooz.game.FirstPlayerPolicy
import io.github.yamin8000.dooz.model.AiDifficulty
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.ui.composables.SingleLinePersianText
import io.github.yamin8000.dooz.util.Constants

@Composable
internal fun AiDifficultyCard(
    aiDifficulty: AiDifficulty,
    onDifficultyChanged: (AiDifficulty) -> Unit
) {
    val title = stringResource(R.string.ai_difficulty)
    var isDialogVisible by remember { mutableStateOf(false) }
    SettingsItemCard(
        title = title,
        content = {
            if (isDialogVisible) {
                SettingsSelectorDialog(
                    title = title,
                    options = Constants.difficulties,
                    currentItem = aiDifficulty,
                    onDismiss = { isDialogVisible = false },
                    onOptionChanged = onDifficultyChanged,
                    displayProvider = { item, context -> context.getString(item.persianNameStringResource) }
                )
            }
            SettingsItem(
                onClick = { isDialogVisible = true },
                content = { SingleLinePersianText(stringResource(aiDifficulty.persianNameStringResource)) }
            )
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
    var playerTypeDialogVisibility by remember { mutableStateOf(false) }
    var firstPlayerPolicyVisibility by remember { mutableStateOf(false) }
    val title = stringResource(R.string.game_general_settings)

    SettingsItemCard(
        title = title,
        content = {
            if (playerTypeDialogVisibility) {
                SettingsSelectorDialog(
                    title = title,
                    options = GamePlayersType.values().toList(),
                    currentItem = gamePlayersType,
                    onDismiss = { playerTypeDialogVisibility = false },
                    onOptionChanged = onPlayerTypeChange,
                    displayProvider = { item, context -> context.getString(item.persianNameStringResource) }
                )
            }

            if (firstPlayerPolicyVisibility) {
                SettingsSelectorDialog(
                    title = title,
                    options = FirstPlayerPolicy.values().toList(),
                    currentItem = firstPlayerPolicy,
                    onDismiss = { firstPlayerPolicyVisibility = false },
                    onOptionChanged = onFirstPlayerPolicyChange,
                    displayProvider = { item, context -> context.getString(item.persianNameStringResource) }
                )
            }

            SettingsItem(
                onClick = { playerTypeDialogVisibility = true },
                content = { SingleLinePersianText(stringResource(gamePlayersType.persianNameStringResource)) }
            )

            SettingsItem(
                onClick = { firstPlayerPolicyVisibility = true },
                content = { SingleLinePersianText(stringResource(firstPlayerPolicy.persianNameStringResource)) }
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
    SettingsItemCard(
        title = stringResource(R.string.game_board_size),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
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