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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Undo
import androidx.compose.material.icons.twotone.Games
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.content.MainTopAppBar
import io.github.yamin8000.dooz.model.AiDifficulty
import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.model.PlayerType
import io.github.yamin8000.dooz.ui.composables.LockScreenOrientation
import io.github.yamin8000.dooz.ui.composables.PersianText
import io.github.yamin8000.dooz.ui.composables.SingleLinePersianText
import io.github.yamin8000.dooz.ui.composables.isFontScaleNormal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameContent(
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val gameState = rememberHomeState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val undoRepeats = remember {
        if (gameState.gamePlayersType.value == GamePlayersType.PvC) 2 else 1
    }

    Scaffold(
        topBar = { MainTopAppBar(scrollBehavior, onNavigateToSettings, onNavigateToAbout) },
        bottomBar = {
            BottomAppBar(
                actions = {
                    FilledIconButton(
                        onClick = { repeat(undoRepeats) { gameState.undo() } },
                        enabled = gameState.isGameStarted.value && gameState.lastPlayedCells.value.isNotEmpty(),
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.TwoTone.Undo,
                                stringResource(R.string.undo)
                            )
                        }
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { SingleLinePersianText(stringResource(R.string.new_game)) },
                        onClick = { gameState.newGame() },
                        icon = {
                            Icon(
                                imageVector = Icons.TwoTone.Games,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = 16.dp),
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        content = {
                            AnimatedVisibility(
                                visible = gameState.isGameStarted.value,
                                enter = slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = tween(300)
                                ),
                                content = {
                                    GameInfoCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        playersType = gameState.gamePlayersType.value,
                                        aiDifficulty = gameState.aiDifficulty.value,
                                        winnerName = gameState.winner.value?.name,
                                        isGameDrew = gameState.isGameDrew.value
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = gameState.isGameStarted.value,
                                enter = slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(300)
                                ),
                                content = {
                                    PlayerCards(
                                        firstPlayerPolicy = gameState.firstPlayerPolicy.value,
                                        players = gameState.players.value,
                                        currentPlayer = gameState.currentPlayer.value
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = gameState.isGameStarted.value && !gameState.isRollingDices.value,
                                enter = scaleIn(),
                                exit = scaleOut(),
                                content = {
                                    GameBoard(
                                        gameSize = gameState.gameSize.intValue,
                                        gameCells = gameState.gameCells.value,
                                        winnerCells = gameState.winnerCells.value,
                                        isGameFinished = gameState.isGameFinished.value,
                                        currentPlayerType = gameState.currentPlayer.value?.type,
                                        shapeProvider = gameState::getOwnerShape,
                                        onItemClick = gameState::playCell
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(64.dp))
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun GameResult(
    winnerName: String?,
    isGameDrew: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (isFontScaleNormal()) {
            PersianText(
                text = stringResource(R.string.game_result),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (isGameDrew)
            SingleLinePersianText(stringResource(R.string.game_is_drew))
        if (winnerName != null)
            SingleLinePersianText(stringResource(R.string.x_is_winner, winnerName))
        if (!isGameDrew && winnerName == null)
            SingleLinePersianText(stringResource(R.string.undefined))
    }
}

@Composable
private fun GameInfoCard(
    modifier: Modifier = Modifier,
    playersType: GamePlayersType = GamePlayersType.PvP,
    aiDifficulty: AiDifficulty = AiDifficulty.Easy,
    winnerName: String?,
    isGameDrew: Boolean
) {
    Card(
        modifier = modifier,
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                content = {
                    PersianText(
                        text = stringResource(R.string.game_info),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    SingleLinePersianText(stringResource(playersType.persianNameStringResource))

                    if (playersType == GamePlayersType.PvC) {
                        SingleLinePersianText(
                            stringResource(
                                R.string.ai_difficulty_var,
                                stringResource(aiDifficulty.persianNameStringResource)
                            )
                        )
                    }
                    GameResult(
                        winnerName = winnerName,
                        isGameDrew = isGameDrew
                    )
                }
            )
        }
    )
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
        userScrollEnabled = false,
        content = {
            gameCells.forEachIndexed { _, row ->
                itemsIndexed(row) { _, cell ->
                    val colors = if (cell in winnerCells)
                        MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
                    else MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                    DoozItem(
                        clickable = !isGameFinished && currentPlayerType == PlayerType.Human && cell.owner == null,
                        shape = shapeProvider(cell.owner),
                        size = boxItemSize,
                        hasOwner = cell.owner != null,
                        onClick = { onItemClick(cell) },
                        backgroundColor = colors.first,
                        contentColor = colors.second
                    )
                }
            }
        }
    )
}

@Composable
private fun DoozItem(
    shape: Shape,
    clickable: Boolean,
    size: Dp,
    hasOwner: Boolean,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                enabled = clickable,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
        content = {
            AnimatedVisibility(
                visible = hasOwner,
                enter = scaleIn(animationSpec = tween(150)),
                content = {
                    if (hasOwner) {
                        Box(
                            modifier = Modifier
                                .size((size.value / 2).dp)
                                .clip(shape)
                                .background(contentColor),
                        )
                    }
                }
            )
        }
    )
}