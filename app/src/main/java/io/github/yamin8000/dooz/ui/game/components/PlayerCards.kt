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

package io.github.yamin8000.dooz.ui.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.domain.FirstPlayerPolicy
import io.github.yamin8000.dooz.domain.model.Player

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
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        content = {
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
    )
}