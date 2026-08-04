package io.github.yamin8000.dooz.feature_game.ui

import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.DoozCell
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.domain.model.GameType
import io.github.yamin8000.dooz.common.domain.model.Player

data class GameState(
    val isGameDraw: Boolean = false,
    val gameSize: Int = 3,
    val currentPlayer: Player? = null,
    val players: List<Player> = emptyList(),
    val gamePlayersType: GamePlayersType = GamePlayersType.PvC,
    val isGameStarted: Boolean = false,
    val isGameFinished: Boolean = false,
    val winner: Player? = null,
    val gameType: GameType = GameType.Simple,
    val winnerCells: List<DoozCell> = emptyList(),
    val aiDifficulty: AiDifficulty = AiDifficulty.Easy,
    val isRollingDices: Boolean = false,
    val firstPlayerPolicy: FirstPlayerPolicy = FirstPlayerPolicy.DiceRolling,
    val lastPlayedCells: List<DoozCell> = emptyList(),
    val gameCells: List<List<DoozCell>> = emptyList()
)