/*
 *     Dooz
 *     SimpleGameLogic.kt Created/Updated by Yamin Siahmargooei at 2022/9/5
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

package io.github.yamin8000.dooz.game.logic

import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.Player

class SimpleGameLogic(
    private val gameCells: List<List<DoozCell>>,
    private val gameSize: Int
) : GameLogic() {

    override var winnerCells = listOf<DoozCell>()

    override var winner: Player? = null

    override fun findWinner(): Player? {
        winner = findHorizontalWinner()
        if (winner != null) return winner

        winner = findVerticalWinner()
        if (winner != null) return winner

        winner = findDiagonalWinner()
        if (winner != null) return winner

        return winner
    }

    override fun isGameDrew(): Boolean {
        winner = findWinner()
        if (winner == null && gameCells.all { row -> row.all { it.owner != null } })
            return true
        return false
    }

    private fun findDiagonalWinner(): Player? {
        val firstRow = gameCells.first()

        if (firstRow.first().owner == null && firstRow.last().owner == null)
            return null

        /**
         *  a x x
         *  x x x
         *  x x x
         */
        if (firstRow.first().owner != null) {
            val diagonals = mutableListOf<DoozCell>()
            diagonals.add(firstRow.first())
            for (i in 1 until gameSize) {
                val nextCell = gameCells[i][i]
                if (nextCell.owner != null) diagonals.add(nextCell) else break
                if (nextCell != diagonals.last()) break
            }
            if (diagonals.isNotEmpty() && diagonals.size == gameSize && diagonals.all { it.owner == firstRow.first().owner }) {
                winnerCells = diagonals
                return firstRow.first().owner
            }
        }

        /**
         *  x x a
         *  x x x
         *  x x x
         */
        if (firstRow.last().owner != null) {
            val diagonals = mutableListOf<DoozCell>()
            diagonals.add(firstRow.last())
            var i = 1
            var j = gameSize - 2
            while (j > -1) {
                val nextCell = gameCells[i][j]
                if (nextCell.owner != null) diagonals.add(nextCell) else break
                if (nextCell != diagonals.last()) break
                i++
                j--
            }
            if (diagonals.isNotEmpty() && diagonals.size == gameSize && diagonals.all { it.owner == firstRow.last().owner }) {
                winnerCells = diagonals
                return firstRow.last().owner
            }
        }

        return null
    }

    private fun findVerticalWinner(): Player? {
        val firstRow = gameCells.first()

        for (j in gameCells.indices) {
            val column = mutableListOf<DoozCell>()
            column.add(firstRow[j])
            if (firstRow[j].owner != null) {
                for (i in 1 until gameSize) {
                    val nextCell = gameCells[i][j]
                    if (nextCell.owner != null) column.add(nextCell) else break
                    if (nextCell != column.last()) break
                }
            } else continue
            if (column.isNotEmpty() && column.size == gameSize && column.all { it.owner == firstRow[j].owner }) {
                winnerCells = column
                return firstRow[j].owner
            }
        }

        return null
    }

    private fun findHorizontalWinner(): Player? {
        for (i in gameCells.indices) {
            val row = gameCells[i]
            if (row.isNotEmpty() && row.any { it.owner == null })
                continue
            if (row.isNotEmpty() && row.all { it.owner == row.first().owner }) {
                winnerCells = row
                return row.first().owner
            }
        }
        return null
    }
}