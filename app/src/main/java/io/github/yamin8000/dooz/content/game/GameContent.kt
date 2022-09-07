/*
 *     Dooz
 *     GameContent.kt Created by Yamin Siahmargooei at 2022/8/26
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

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.ui.LockScreenOrientation
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.navigation.Nav
import io.github.yamin8000.dooz.ui.theme.DoozTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GameContent(
    navController: NavController? = null
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val gameState = rememberHomeState()

    DoozTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { gameState.startGame() }
                    ) { PersianText(stringResource(R.string.start_game)) }
                    Button(
                        onClick = { navController?.navigate(Nav.Routes.settings) }
                    ) { PersianText(stringResource(R.string.settings)) }
                }
                if (gameState.isGameStarted.value)
                    CurrentPlayerCard(gameState.currentPlayer.value)
                gameState.winner.value?.let {
                    Text(text = "Winner is: ${it.name}")
                }
                if (gameState.isGameDrew.value)
                    PersianText(stringResource(R.string.game_is_drew))
                if (gameState.isGameStarted.value) {
                    GameBoard(
                        gameSize = gameState.gameSize.value,
                        gameCells = gameState.gameCells.value,
                        isGameFinished = gameState.isGameFinished.value,
                        shapeProvider = { gameState.getOwnerShape(it) },
                        onItemClick = { gameState.playItemByUser(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentPlayerCard(
    player: Player?
) {
    Card {
        player?.let { PersianText(stringResource(R.string.player_turn, it.name)) }
    }
}

@Composable
private fun GameBoard(
    gameSize: Int,
    gameCells: List<List<DoozCell>>,
    isGameFinished: Boolean,
    shapeProvider: (Player?) -> Shape,
    onItemClick: (DoozCell) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val boxPadding = 16.dp
    val boxSize = screenWidth - (2 * boxPadding.value).dp
    val itemMargin = 8.dp
    val boxItemSize = ((boxSize.value - ((itemMargin.value * (gameSize - 1)))) / gameSize).dp

    LazyVerticalGrid(
        modifier = Modifier
            .padding(boxPadding)
            .size(boxSize),
        columns = GridCells.Fixed(gameSize),
        horizontalArrangement = Arrangement.spacedBy(itemMargin),
        verticalArrangement = Arrangement.spacedBy(itemMargin),
        userScrollEnabled = false
    ) {
        gameCells.forEachIndexed { _, row ->
            itemsIndexed(row) { _, cell ->
                DoozItem(
                    clickable = !isGameFinished,
                    shape = shapeProvider(cell.owner),
                    itemSize = boxItemSize,
                    doozCell = cell,
                    onClick = { onItemClick(cell) }
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DoozItem(
    shape: Shape = RectangleShape,
    clickable: Boolean = true,
    itemSize: Dp = 50.dp,
    doozCell: DoozCell = DoozCell(0, 0),
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick,
                enabled = clickable
            )
    ) {
        Box(
            modifier = Modifier
                .size(itemSize)
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            doozCell.owner?.let {
                Box(
                    modifier = Modifier
                        .size((itemSize.value / 2).dp)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.onSecondary),
                )
            }
        }
    }
}