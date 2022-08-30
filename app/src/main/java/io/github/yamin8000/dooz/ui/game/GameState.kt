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

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.model.Player

class GameState(
    var gameCells: MutableState<List<List<DoozCell>>>,
    val gameSize: MutableState<Int>,
    var currentPlayer: MutableState<Player?>,
    var players: MutableState<List<Player>>,
    var gamePlayersType: MutableState<GamePlayersType>,
    var isGameStarted: MutableState<Boolean>,
    var isGameFinished: MutableState<Boolean>,
    var winner: MutableState<Player?>,
    var gameType: MutableState<GameType>
) {

    private var filledCells = 0

    init {
        newGame()
    }

    private fun newGame() {
        resetGame()
        preparePlayers()
    }

    private fun resetGame() {
        winner.value = null
        filledCells = 0
        isGameFinished.value = false
        isGameStarted.value = false
        gameCells.value = getEmptyBoard()
    }

    private fun getEmptyBoard(): List<List<DoozCell>> {
        val list = mutableListOf<List<DoozCell>>()
        for (x in 0 until gameSize.value) {
            val row: MutableList<DoozCell> = mutableListOf()
            for (y in 0 until gameSize.value)
                row.add(DoozCell(x, y))
            list.add(row)
        }
        return list
    }

    fun startGame() {
        newGame()
        isGameStarted.value = true
    }

    private fun preparePlayers() {
        players.value = listOf(Player("P1"), Player("P2"))
        currentPlayer.value = players.value.first()
    }

    fun getOwnerShape(owner: Player): Shape {
        return if (owner == players.value.first()) CircleShape else RectangleShape
    }

    private fun changePlayer() {
        if (currentPlayer.value == players.value.first()) currentPlayer.value = players.value.last()
        else currentPlayer.value = players.value.first()
    }

    fun playItemByUser(
        cell: DoozCell
    ) {
        checkIfGameIsFinished()
        changeCellOwner(cell)
        checkIfGameIsFinished()
    }

    private fun changeCellOwner(
        cell: DoozCell
    ) {
        if (cell.owner == null && isGameStarted.value) {
            cell.owner = currentPlayer.value
            changePlayer()
        }
    }

    private fun finishGame() {
        isGameStarted.value = false
    }

    private fun checkIfGameIsFinished() {
        winner.value = findWinner()
        isGameFinished.value = winner.value != null
        if (isGameFinished.value)
            finishGame()
    }

    private fun findWinner(): Player? {
        return when (gameType.value) {
            GameType.Simple -> findSimpleGameWinner()
        }
    }

    /**
     * Probably a naive approach for:
     * finding the winner in the most simple type of tic-tac-tao
     */
    private fun findSimpleGameWinner(): Player? {
        var winner: Player? = null
        var p1Score = 0
        var p2Score = 0
        for (i in 0 until gameCells.value.size) {
            for (j in 0 until gameCells.value[i].size) {
                if (gameCells.value[i][j].owner == players.value.first())
                    p1Score++
                if (gameCells.value[i][j].owner == players.value.last())
                    p2Score++
                if (p1Score == 3) {
                    winner = players.value.first()
                    break
                }
                if (p2Score == 3) {
                    winner = players.value.last()
                    break
                }
            }
            p1Score = 0
            p2Score = 0
        }
        return winner
    }
}

@Composable
fun rememberHomeState(
    doozCells: MutableState<List<List<DoozCell>>> = rememberSaveable { mutableStateOf(emptyList()) },
    gameSize: MutableState<Int> = rememberSaveable { mutableStateOf(3) },
    currentPlayer: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    playerPair: MutableState<List<Player>> = rememberSaveable { mutableStateOf(listOf()) },
    gamePlayersType: MutableState<GamePlayersType> = rememberSaveable {
        mutableStateOf(
            GamePlayersType.PvP
        )
    },
    isGameStarted: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    isGameFinished: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    winner: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    gameType: MutableState<GameType> = rememberSaveable { mutableStateOf(GameType.Simple) }
) = remember(
    doozCells,
    gameSize,
    currentPlayer,
    playerPair,
    gamePlayersType,
    isGameStarted,
    isGameFinished,
    winner,
    gameType
) {
    GameState(
        doozCells,
        gameSize,
        currentPlayer,
        playerPair,
        gamePlayersType,
        isGameStarted,
        isGameFinished,
        winner,
        gameType
    )
}