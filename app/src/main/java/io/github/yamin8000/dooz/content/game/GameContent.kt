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
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Gamepad
import androidx.compose.material.icons.twotone.Undo
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.MainTopAppBar
import io.github.yamin8000.dooz.model.*
import io.github.yamin8000.dooz.ui.XShape
import io.github.yamin8000.dooz.ui.composables.ButtonWithIcon
import io.github.yamin8000.dooz.ui.composables.InfoCard
import io.github.yamin8000.dooz.ui.composables.LockScreenOrientation
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.theme.PreviewTheme

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GameContent(
    onNavigateToSettings: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val gameState = rememberHomeState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { MainTopAppBar(scrollBehavior, onNavigateToSettings) },
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ButtonWithIcon(
                            onClick = { gameState.newGame() },
                            enabled = !gameState.isRollingDices.value,
                            content = { PersianText(stringResource(R.string.start_game)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.TwoTone.Gamepad,
                                    contentDescription = stringResource(R.string.start_game)
                                )
                            }
                        )
                        val undoRepeats =
                            if (gameState.gamePlayersType.value == GamePlayersType.PvC) 2
                            else 1
                        ButtonWithIcon(
                            onClick = { repeat(undoRepeats) { gameState.undo() } },
                            enabled = gameState.isGameStarted.value && gameState.lastPlayedCells.value.isNotEmpty(),
                            content = { PersianText(stringResource(R.string.undo)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.TwoTone.Undo,
                                    stringResource(R.string.undo)
                                )
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = gameState.isGameStarted.value,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    ) {
                        GameInfoCard(
                            gameState.winner.value?.name,
                            gameState.isGameDrew.value,
                            gameState.gamePlayersType.value,
                            gameState.aiDifficulty.value,
                        )
                    }

                    AnimatedVisibility(
                        visible = gameState.isGameStarted.value,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    ) {
                        PlayerCards(
                            gameState.firstPlayerPolicy.value,
                            gameState.players.value,
                            gameState.currentPlayer.value
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
                            currentPlayerType = gameState.currentPlayer.value?.type,
                            shapeProvider = { gameState.getOwnerShape(it) },
                            onItemClick = { gameState.playCell(it) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun GameInfoCard(
    winnerName: String?,
    isGameDrew: Boolean = true,
    playersType: GamePlayersType = GamePlayersType.PvP,
    aiDifficulty: AiDifficulty = AiDifficulty.Easy,
) {
    InfoCard(
        modifier = Modifier.fillMaxWidth(),
        columnModifier = Modifier.fillMaxWidth(),
        header = {
            PersianText(
                text = stringResource(R.string.game_info),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        content = {
            PersianText(stringResource(playersType.persianNameStringResource))

            if (playersType == GamePlayersType.PvC)
                PersianText(stringResource(R.string.ai_difficulty_var, aiDifficulty.persianName))
        },
        footer = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isGameDrew)
                    PersianText(stringResource(R.string.game_is_drew))
                if (winnerName != null)
                    PersianText(stringResource(R.string.x_is_winner, winnerName))
                if (!isGameDrew && winnerName == null)
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
    currentPlayerType: PlayerType?,
    shapeProvider: (Player?) -> Shape,
    onItemClick: (DoozCell) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val boxPadding = 16.dp
    val boxSize = screenWidth - (2 * boxPadding.value).dp
    val itemMargin = 8.dp
    val boxItemSize = ((boxSize.value - ((itemMargin.value * (gameSize - 1)))) / gameSize).dp

    LazyVerticalGrid(
        modifier = Modifier.size(boxSize),
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
                    clickable = !isGameFinished && currentPlayerType == PlayerType.Human && cell.owner == null,
                    shape = shapeProvider(cell.owner),
                    itemSize = boxItemSize,
                    doozCellOwner = cell.owner,
                    onClick = { onItemClick(cell) },
                    itemBackgroundColor = colors.first(),
                    itemContentColor = colors.component2()
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DoozItem(
    shape: Shape,
    clickable: Boolean,
    itemSize: Dp,
    doozCellOwner: Player?,
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
                enabled = clickable,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(itemSize)
                .clip(RoundedCornerShape(5.dp))
                .background(itemBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = doozCellOwner != null,
                enter = scaleIn(animationSpec = tween(150))
            ) {
                if (doozCellOwner != null) {
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
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun Preview() = PreviewTheme { GameContent {} }

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun DoozItemPreview() {
    PreviewTheme {
        DoozItem(
            shape = XShape,
            clickable = true,
            itemSize = 40.dp,
            doozCellOwner = Player("Player"),
            itemBackgroundColor = MaterialTheme.colorScheme.primary,
            itemContentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {}
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun GameBoardPreview() {
    PreviewTheme {
        Surface {
            GameBoard(
                gameSize = 3,
                gameCells = buildList {
                    for (i in 1..3) {
                        val row = mutableListOf<DoozCell>()
                        for (j in 1..3)
                            row.add(DoozCell(i, j))
                        add(row)
                    }
                },
                winnerCells = buildList { },
                isGameFinished = false,
                currentPlayerType = null,
                shapeProvider = { XShape },
                onItemClick = {}
            )
        }
    }
}