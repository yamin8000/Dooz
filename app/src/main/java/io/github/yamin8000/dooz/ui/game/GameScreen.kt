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

package io.github.yamin8000.dooz.ui.game

import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Undo
import androidx.compose.material.icons.twotone.Games
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.MainTopAppBar
import io.github.yamin8000.dooz.ui.game.components.GameBoard
import io.github.yamin8000.dooz.ui.game.components.GameInfoCard
import io.github.yamin8000.dooz.ui.game.components.PlayerCards
import io.github.yamin8000.dooz.domain.model.GamePlayersType
import io.github.yamin8000.dooz.ui.components.SingleLinePersianText
import io.github.yamin8000.dooz.util.Utility.LockScreenOrientation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
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