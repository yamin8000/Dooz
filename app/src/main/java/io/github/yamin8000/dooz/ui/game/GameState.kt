/*
 *     Dooz
 *     GameState.kt Created by Yamin Siahmargooei at 2022/8/26
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

import android.content.Context
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import io.github.yamin8000.dooz.game.GameConstants.gameDefaultSize
import io.github.yamin8000.dooz.game.GamePlayersType
import io.github.yamin8000.dooz.game.GameType
import io.github.yamin8000.dooz.game.logic.GameLogic
import io.github.yamin8000.dooz.game.logic.SimpleGameLogic
import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.ui.RingShape
import io.github.yamin8000.dooz.ui.XShape
import io.github.yamin8000.dooz.ui.settings.settings
import io.github.yamin8000.dooz.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameState(
    val context: Context,
    val coroutineScope: LifecycleCoroutineScope,
    var gameCells: MutableState<List<List<DoozCell>>>,
    val gameSize: MutableState<Int>,
    var currentPlayer: MutableState<Player?>,
    var players: MutableState<List<Player>>,
    var gamePlayersType: MutableState<GamePlayersType>,
    var isGameStarted: MutableState<Boolean>,
    var isGameFinished: MutableState<Boolean>,
    var winner: MutableState<Player?>,
    var gameType: MutableState<GameType>,
    var isGameDrew: MutableState<Boolean>
) {
    private var gameLogic: GameLogic? = null

    init {
        newGame()
    }

    fun startGame() {
        newGame()
        isGameStarted.value = true
    }

    fun playItemByUser(
        cell: DoozCell
    ) {
        checkIfGameIsFinished()
        changeCellOwner(cell)
        checkIfGameIsFinished()
    }

    private fun newGame() {
        resetGame()
        coroutineScope.launch {
            prepareGameRules()
            prepareGameLogic()
            preparePlayers()
        }
    }

    private suspend fun prepareGameRules() {
        gameSize.value = withContext(coroutineScope.coroutineContext) {
            context.settings.data.map {
                it[intPreferencesKey(Constants.gameSize)]
            }.first()
        } ?: gameDefaultSize
    }

    private fun prepareGameLogic() {
        when (gameType.value) {
            GameType.Simple -> gameLogic = SimpleGameLogic(gameCells.value, gameSize.value)
        }
    }

    private fun resetGame() {
        winner.value = null
        isGameFinished.value = false
        isGameStarted.value = false
        isGameDrew.value = false
        gameCells.value = getEmptyBoard()
    }

    private fun getEmptyBoard(): List<List<DoozCell>> {
        val columns = mutableListOf<List<DoozCell>>()
        for (x in 0 until gameSize.value) {
            val row = mutableListOf<DoozCell>()
            for (y in 0 until gameSize.value)
                row.add(DoozCell(x, y))
            columns.add(row)
        }
        return columns
    }

    private suspend fun preparePlayers() {
        val firstPlayer = withContext(coroutineScope.coroutineContext) {
            getFirstPlayerName()
        } ?: "Player 1"
        val secondPlayer = withContext(coroutineScope.coroutineContext) {
            getSecondPlayerName()
        } ?: "Player 2"
        players.value = listOf(Player(firstPlayer), Player(secondPlayer))
        currentPlayer.value = players.value.first()
    }

    fun getOwnerShape(owner: Player?): Shape {
        return if (owner == players.value.first()) RingShape else XShape
    }

    private fun changePlayer() {
        if (currentPlayer.value == players.value.first()) currentPlayer.value = players.value.last()
        else currentPlayer.value = players.value.first()
    }

    private fun changeCellOwner(
        cell: DoozCell
    ) {
        if (cell.owner == null && isGameStarted.value) {
            cell.owner = currentPlayer.value
            changePlayer()
        }
    }

    private fun checkIfGameIsFinished() {
        winner.value = findWinner()
        if (winner.value != null)
            finishGame()
        if (gameLogic?.isGameDrew() == true)
            handleDrewGame()
    }

    private fun handleDrewGame() {
        finishGame()
        isGameDrew.value = true
    }

    private fun finishGame() {
        isGameFinished.value = true
    }

    private fun findWinner(): Player? {
        return when (gameType.value) {
            GameType.Simple -> gameLogic?.findWinner()
        }
    }

    private suspend fun getSecondPlayerName(): String? {
        return getPlayerName(Constants.secondPlayerName)
    }

    private suspend fun getFirstPlayerName(): String? {
        return getPlayerName(Constants.firstPlayerName)
    }

    private suspend fun getPlayerName(
        player: String
    ): String? {
        return context.settings.data.map {
            it[stringPreferencesKey(player)]
        }.first()
    }
}

@Composable
fun rememberHomeState(
    context: Context = LocalContext.current,
    coroutineScope: LifecycleCoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
    doozCells: MutableState<List<List<DoozCell>>> = rememberSaveable { mutableStateOf(emptyList()) },
    gameSize: MutableState<Int> = rememberSaveable { mutableStateOf(gameDefaultSize) },
    currentPlayer: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    playerPair: MutableState<List<Player>> = rememberSaveable { mutableStateOf(listOf()) },
    gamePlayersType: MutableState<GamePlayersType> = rememberSaveable {
        mutableStateOf(
            GamePlayersType.PvC
        )
    },
    isGameStarted: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isGameFinished: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    winner: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    gameType: MutableState<GameType> = rememberSaveable { mutableStateOf(GameType.Simple) },
    isGameDrew: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
) = remember(
    context,
    coroutineScope,
    doozCells,
    gameSize,
    currentPlayer,
    playerPair,
    gamePlayersType,
    isGameStarted,
    isGameFinished,
    winner,
    gameType,
    isGameDrew
) {
    GameState(
        context,
        coroutineScope,
        doozCells,
        gameSize,
        currentPlayer,
        playerPair,
        gamePlayersType,
        isGameStarted,
        isGameFinished,
        winner,
        gameType,
        isGameDrew
    )
}