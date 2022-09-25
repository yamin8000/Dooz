/*
 *     Dooz
 *     PlayersInfoContent.kt Created/Updated by Yamin Siahmargooei at 2022/9/25
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

package io.github.yamin8000.dooz.content.game

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.game.FirstPlayerPolicy
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.ui.ShapePreview
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.PreviewTheme
import io.github.yamin8000.dooz.ui.toShape

@Composable
internal fun PlayerCards(
    firstPlayerPolicy: FirstPlayerPolicy,
    players: List<Player>,
    currentPlayer: Player?
) {
    val firstPlayer = players[0]
    val secondPlayer = players[1]

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PlayerCard(
            modifier = Modifier.weight(1f),
            player = firstPlayer,
            firstPlayerPolicy = firstPlayerPolicy,
            isCurrentPlayer = firstPlayer == currentPlayer
        )
        PlayerCard(
            modifier = Modifier.weight(1f),
            player = secondPlayer,
            firstPlayerPolicy = firstPlayerPolicy,
            isCurrentPlayer = secondPlayer == currentPlayer
        )
    }
}

@Composable
internal fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player,
    firstPlayerPolicy: FirstPlayerPolicy,
    isCurrentPlayer: Boolean = true
) {
    val iconTint =
        if (isCurrentPlayer) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)

    val borderColor =
        if (isCurrentPlayer) MaterialTheme.colorScheme.outline
        else MaterialTheme.colorScheme.outlineVariant

    OutlinedCard(
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (firstPlayerPolicy == FirstPlayerPolicy.DiceRolling)
                PlayerDice(iconTint = iconTint, diceIndex = player.diceIndex)
            player.shape?.toShape()?.let { shape -> ShapePreview(shape, 30.dp, iconTint) }
            PersianText(text = player.name, modifier = Modifier.weight(2f))
        }
    }
}

@Composable
private fun PlayerDice(
    iconTint: Color,
    diceIndex: Int = 1
) {
    val icon = when (diceIndex) {
        1 -> R.drawable.ic_dice_1
        2 -> R.drawable.ic_dice_2
        3 -> R.drawable.ic_dice_3
        4 -> R.drawable.ic_dice_4
        5 -> R.drawable.ic_dice_5
        6 -> R.drawable.ic_dice_6
        else -> R.drawable.ic_dice_1
    }
    Icon(
        painter = painterResource(icon),
        contentDescription = stringResource(R.string.player_turn),
        tint = iconTint
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun PlayerCardsPreview() {
    PreviewTheme {
        PlayerCards(
            firstPlayerPolicy = FirstPlayerPolicy.DiceRolling,
            players = listOf(Player("Yamin"), Player("Amir")),
            currentPlayer = null
        )
    }
}