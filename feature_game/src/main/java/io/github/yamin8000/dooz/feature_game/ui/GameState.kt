package io.github.yamin8000.dooz.feature_game.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.DoozCell
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.GameType
import io.github.yamin8000.dooz.common.domain.model.Player

data class GameState(
    val isGameDraw: Boolean = false,
    val isGameStarted: Boolean = false,
    val isGameFinished: Boolean = false,
    val isRollingDices: Boolean = false,
    val gameSize: Int = 3,
    val firstPlayer: Player = Player(),
    val secondPlayer: Player = Player(),
    val currentPlayer: Player? = null,
    val gamePlayersType: GamePlayersType = GamePlayersType.PvC,
    val winner: Player? = null,
    val gameType: GameType = GameType.Simple,
    val winnerCells: List<DoozCell> = emptyList(),
    val aiDifficulty: AiDifficulty = AiDifficulty.Easy,
    val firstPlayerPolicy: FirstPlayerPolicy = FirstPlayerPolicy.DiceRolling,
    val lastPlayedCells: SnapshotStateList<DoozCell> = mutableStateListOf(),
    val gameCells: SnapshotStateList<DoozCell> = mutableStateListOf()
)