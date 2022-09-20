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
import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.MainTopAppBar
import io.github.yamin8000.dooz.model.*
import io.github.yamin8000.dooz.ui.ShapePreview
import io.github.yamin8000.dooz.ui.composables.*
import io.github.yamin8000.dooz.ui.toShape

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GameContent(
    onNavigateToSettings: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val gameState = rememberHomeState()

    Scaffold(
        topBar = { MainTopAppBar(onSettingsIconClick = onNavigateToSettings) }
    ) { contentPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Button(
                    onClick = { gameState.newGame() },
                    content = { PersianText(stringResource(R.string.start_game)) }
                )

                AnimatedVisibility(
                    visible = gameState.isGameStarted.value,
                    enter = slideInHorizontally { -it }
                ) {
                    GameInfoCard(
                        gameState.winner.value,
                        gameState.isGameDrew.value,
                        gameState.gamePlayersType.value,
                        gameState.aiDifficulty.value,
                    )
                }

                AnimatedVisibility(
                    visible = gameState.isGameStarted.value,
                    enter = slideInHorizontally { it }
                ) {
                    PlayerCards(
                        gameState.players.value,
                        gameState.currentPlayer.value,
                        gameState.isRollingDices.value
                    )
                }

                AnimatedVisibility(
                    visible = gameState.isGameStarted.value && !gameState.isRollingDices.value,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    GameBoard(
                        gameSize = gameState.gameSize.value,
                        gameCells = gameState.gameCells.value,
                        winnerCells = gameState.winnerCells.value,
                        isGameFinished = gameState.isGameFinished.value,
                        currentPlayer = gameState.currentPlayer.value,
                        shapeProvider = { gameState.getOwnerShape(it) },
                        onItemClick = { gameState.playCell(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerCards(
    players: List<Player>,
    currentPlayer: Player?,
    isRollingDices: Boolean
) {
    val firstPlayer = players[0]
    val secondPlayer = players[1]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PlayerCard(firstPlayer, firstPlayer == currentPlayer, isRollingDices)
        PlayerCard(secondPlayer, secondPlayer == currentPlayer, isRollingDices)
    }
}

@Preview
@Composable
fun PlayerCard(
    @PreviewParameter(PlayerProvider::class)
    player: Player,
    isCurrentPlayer: Boolean = true,
    isRollingDices: Boolean = true
) {
    val iconTint =
        if (isCurrentPlayer) MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)

    val borderColor =
        if (isCurrentPlayer) MaterialTheme.colorScheme.outline
        else MaterialTheme.colorScheme.outlineVariant

    OutlinedCard(
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlayerDice(iconTint = iconTint, diceIndex = player.diceIndex)
            player.shape?.toShape()?.let { shape -> ShapePreview(shape, 30.dp, iconTint) }
            PersianText(player.name)
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

@Preview
@Composable
fun GameInfoCard(
    @PreviewParameter(PlayerProvider::class)
    winner: Player?,
    isGameDrew: Boolean = true,
    playersType: GamePlayersType = GamePlayersType.PvP,
    aiDifficulty: AiDifficulty = AiDifficulty.Easy,
) {
    InfoCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        columnModifier = Modifier.fillMaxWidth(),
        header = {
            PersianText(
                text = stringResource(R.string.game_info),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        content = {
            PersianText(getGamePlayersTypeCaption(playersType))

            if (playersType == GamePlayersType.PvC)
                PersianText(stringResource(R.string.ai_difficulty_var, aiDifficulty.persianName))
        },
        footer = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isGameDrew)
                    PersianText(stringResource(R.string.game_is_drew))
                winner?.let {
                    PersianText(stringResource(R.string.x_is_winner, it.name))
                }
                if (!isGameDrew && winner == null)
                    PersianText(stringResource(R.string.undefined))
                PersianText(
                    text = stringResource(R.string.game_result),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        })
}

@Composable
private fun GameBoard(
    gameSize: Int,
    gameCells: List<List<DoozCell>>,
    winnerCells: List<DoozCell>,
    isGameFinished: Boolean,
    currentPlayer: Player?,
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
                val colors = if (cell in winnerCells) listOf(
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.onSecondary
                ) else listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary
                )
                DoozItem(
                    clickable = !isGameFinished && currentPlayer?.type == PlayerType.Human,
                    shape = shapeProvider(cell.owner),
                    itemSize = boxItemSize,
                    doozCell = cell,
                    onClick = { onItemClick(cell) },
                    itemBackgroundColor = colors.first(),
                    itemContentColor = colors.component2()
                )
            }
        }
    }
}

@Composable
fun DoozItem(
    shape: Shape,
    clickable: Boolean,
    itemSize: Dp,
    doozCell: DoozCell,
    itemBackgroundColor: Color,
    itemContentColor: Color,
    onClick: () -> Unit
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
                .background(itemBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            doozCell.owner?.let {
                Box(
                    modifier = Modifier
                        .size((itemSize.value / 2).dp)
                        .clip(shape)
                        .background(itemContentColor),
                )
            }
        }
    }
}