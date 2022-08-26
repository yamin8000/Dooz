/*
 *     Dooz
 *     GameState.kt Created by Yamin Siahmargooei at 2022/8/25
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

package io.github.yamin8000.dooz.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import io.github.yamin8000.dooz.ui.game.*

class GameState(
    var gameCells: MutableState<List<List<DoozCell>>>,
    val gameSize: MutableState<Int>,
    var currentPlayer: MutableState<Player?>,
    var playerPair: MutableState<Pair<Player, Player>>,
    var game: MutableState<Game>
) {
    init {
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
        //preparePlayers()

        game.value.isGameStarted = true
    }

    private fun preparePlayers() {
        //if (game.value.gamePlayersType.)
    }

    fun getOwnerShape(owner: DoozCellOwner): Shape {
        return if (owner == DoozCellOwner.P1) CircleShape else RectangleShape
    }

    fun changePlayer() {
        //if (currentPlayer.value == DoozCellOwner.P1) currentPlayer.value = DoozCellOwner.P2
        //else currentPlayer.value = DoozCellOwner.P1
    }

    fun playItem(
        cell: DoozCell
    ) {
        if (cell.owner == null) {
            cell.owner = currentPlayer.value
            changePlayer()
        }
    }
}

@Composable
fun rememberHomeState(
    doozCells: MutableState<List<List<DoozCell>>> = rememberSaveable { mutableStateOf(emptyList()) },
    gameSize: MutableState<Int> = rememberSaveable { mutableStateOf(3) },
    currentPlayer: MutableState<Player?> = rememberSaveable { mutableStateOf(null) },
    playerPair: MutableState<Pair<Player, Player>> = rememberSaveable {
        mutableStateOf(Player("P1") to Player("P2"))
    },
    game: MutableState<Game> = rememberSaveable { mutableStateOf(Game(GamePlayersType.PvP)) }
) = remember(doozCells, gameSize, currentPlayer, playerPair, game) {
    GameState(doozCells, gameSize, currentPlayer, playerPair, game)
}