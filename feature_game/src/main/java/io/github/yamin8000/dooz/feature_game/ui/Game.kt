package io.github.yamin8000.dooz.feature_game.ui

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yamin8000.dooz.common.R
import io.github.yamin8000.dooz.common.ui.components.SingleLinePersianText
import io.github.yamin8000.dooz.common.ui.theme.AppTheme
import io.github.yamin8000.dooz.common.ui.theme.Sizes
import io.github.yamin8000.dooz.common.util.Utility.ObserverEvent
import io.github.yamin8000.dooz.feature_game.ui.components.GameBoard
import io.github.yamin8000.dooz.feature_game.ui.components.GameInfoCard
import io.github.yamin8000.dooz.feature_game.ui.components.GameTopAppBar
import io.github.yamin8000.dooz.feature_game.ui.components.PlayerCards
import io.github.yamin8000.dooz.feature_game.util.Utility.LockScreenOrientation
import kotlinx.collections.immutable.toImmutableList
import io.github.yamin8000.dooz.feature_game.R as moduleR

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        GameContent(
            state = GameState(),
            onAction = {},
            onNavigateToSettings = {},
            onNavigateToAbout = {}
        )
    }
}

@Composable
fun GameScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
    vm: GameViewModel = hiltViewModel()
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val state = vm.state.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    ObserverEvent(vm.eventFlow) { event ->
        when (event) {
            GameEvent.PlayDiceSound -> {
                MediaPlayer.create(context, moduleR.raw.dice)
                    .start()
            }

            GameEvent.PlayDrawSound -> {
                val player = MediaPlayer.create(context, moduleR.raw.draw)
                player.start()
            }

            GameEvent.PlayLoseSound -> {
                val player = MediaPlayer.create(context, moduleR.raw.lose)
                player.start()
            }

            GameEvent.PlayPencilSound -> {
                MediaPlayer.create(context, moduleR.raw.pencil).start()
            }

            GameEvent.PlayWinSound -> {
                val player = MediaPlayer.create(context, moduleR.raw.win)
                player.start()
            }

            GameEvent.Vibrate -> {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
            }
        }
    }

    GameContent(
        modifier = modifier,
        onNavigateToAbout = onNavigateToAbout,
        onNavigateToSettings = onNavigateToSettings,
        state = state,
        onAction = { vm.onAction(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameContent(
    state: GameState,
    onNavigateToSettings: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onAction: (GameAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GameTopAppBar(
                onSettingsIconClick = onNavigateToSettings,
                onAboutIconClick = onNavigateToAbout,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    FilledIconButton(
                        onClick = { onAction(GameAction.Undo) },
                        enabled = state.isGameStarted && state.lastPlayedCells.isNotEmpty(),
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
                        onClick = { onAction(GameAction.NewGame) },
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
        content = { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(horizontal = Sizes.Large),
                content = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Sizes.Large),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = Sizes.Large)
                            .verticalScroll(rememberScrollState()),
                        content = {
                            AnimatedVisibility(
                                visible = state.isGameStarted,
                                enter = slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = tween(300)
                                ),
                                content = {
                                    GameInfoCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        playersType = state.gamePlayersType,
                                        aiDifficulty = state.aiDifficulty,
                                        winnerName = state.winner?.name,
                                        isGameDrew = state.isGameDraw
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = state.isGameStarted,
                                enter = slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(300)
                                ),
                                content = {
                                    PlayerCards(
                                        firstPlayerPolicy = state.firstPlayerPolicy,
                                        players = listOf(state.firstPlayer, state.secondPlayer),
                                        currentPlayer = state.currentPlayer
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = state.isGameStarted && !state.isRollingDices,
                                enter = scaleIn(),
                                exit = scaleOut(),
                                content = {
                                    GameBoard(
                                        gameSize = state.gameSize,
                                        gameCells = state.gameCells,
                                        winnerCells = state.winnerCells.toImmutableList(),
                                        isGameFinished = state.isGameFinished,
                                        currentPlayerType = state.currentPlayer?.type,
                                        onItemClick = { onAction(GameAction.PlayCell(it)) }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(Sizes.xxLarge))
                        }
                    )
                }
            )
        }
    )
}