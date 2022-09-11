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

package io.github.yamin8000.dooz.content.game

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import io.github.yamin8000.dooz.ui.toName
import io.github.yamin8000.dooz.ui.toShape
import io.github.yamin8000.dooz.util.Constants
import io.github.yamin8000.dooz.util.DataStoreHelper
import io.github.yamin8000.dooz.util.settings
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameState(
    private val context: Context,
    private val coroutineScope: LifecycleCoroutineScope,
    var gameCells: MutableState<List<List<DoozCell>>>,
    val gameSize: MutableState<Int>,
    var currentPlayer: MutableState<Player?>,
    var players: MutableState<List<Player>>,
    var gamePlayersType: MutableState<GamePlayersType>,
    var isGameStarted: MutableState<Boolean>,
    var isGameFinished: MutableState<Boolean>,
    var winner: MutableState<Player?>,
    private var gameType: MutableState<GameType>,
    var isGameDrew: MutableState<Boolean>,
    var winnerCells: MutableState<List<DoozCell>>
) {
    private var gameLogic: GameLogic? = null
    private val datastore = DataStoreHelper(context.settings)

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
        gameSize.value = datastore.getInt(Constants.gameSize) ?: gameDefaultSize
        gamePlayersType.value = GamePlayersType.valueOf(
            datastore.getString(Constants.gamePlayersType) ?: GamePlayersType.PvP.name
        )
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
        winnerCells.value = listOf()
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
        val firstPlayerName = withContext(coroutineScope.coroutineContext) {
            getFirstPlayerName()
        } ?: "Player 1"
        val secondPlayerName = withContext(coroutineScope.coroutineContext) {
            getSecondPlayerName()
        } ?: "Player 2"

        val firstPlayerShape = withContext(coroutineScope.coroutineContext) {
            getFirstPlayerShape()?.toShape()
        } ?: RingShape

        val secondPlayerShape = withContext(coroutineScope.coroutineContext) {
            getSecondPlayerShape()?.toShape()
        } ?: XShape

        players.value = listOf(
            Player(firstPlayerName, firstPlayerShape.toName()),
            Player(secondPlayerName, secondPlayerShape.toName())
        )
        currentPlayer.value = players.value.first()
    }

    fun getOwnerShape(
        owner: Player?
    ): Shape {
        return if (owner == players.value.first()) owner.shape?.toShape() ?: RingShape
        else owner?.shape?.toShape() ?: XShape
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
        winnerCells.value = gameLogic?.winnerCells ?: listOf()
    }

    private fun findWinner(): Player? {
        return when (gameType.value) {
            GameType.Simple -> gameLogic?.findWinner()
        }
    }

    private suspend fun getFirstPlayerShape() = getData(Constants.firstPlayerShape)

    private suspend fun getSecondPlayerShape() = getData(Constants.secondPlayerShape)

    private suspend fun getSecondPlayerName() = getData(Constants.secondPlayerName)

    private suspend fun getFirstPlayerName() = getData(Constants.firstPlayerName)

    private suspend fun getData(
        key: String
    ): String? {
        return context.settings.data.map {
            it[stringPreferencesKey(key)]
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
    players: MutableState<List<Player>> = rememberSaveable { mutableStateOf(listOf()) },
    gamePlayersType: MutableState<GamePlayersType> = rememberSaveable {
        mutableStateOf(
            GamePlayersType.PvC
        )
    },
    isGameStarted: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isGameFinished: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    winner: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    gameType: MutableState<GameType> = rememberSaveable { mutableStateOf(GameType.Simple) },
    isGameDrew: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    winnerCells: MutableState<List<DoozCell>> = rememberSaveable { mutableStateOf(emptyList()) }
) = remember(
    context,
    coroutineScope,
    doozCells,
    gameSize,
    currentPlayer,
    players,
    gamePlayersType,
    isGameStarted,
    isGameFinished,
    winner,
    gameType,
    isGameDrew,
    winnerCells
) {
    GameState(
        context,
        coroutineScope,
        doozCells,
        gameSize,
        currentPlayer,
        players,
        gamePlayersType,
        isGameStarted,
        isGameFinished,
        winner,
        gameType,
        isGameDrew,
        winnerCells
    )
}