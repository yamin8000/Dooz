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

import io.github.yamin8000.dooz.game.ai.GameAi
import io.github.yamin8000.dooz.game.ai.SimpleGameAi
import io.github.yamin8000.dooz.model.AiDifficulty
import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.util.Utility.rotated

class SimpleGameLogic(
    private val gameCells: List<List<DoozCell>>,
    private val gameSize: Int,
    aiDifficulty: AiDifficulty
) : GameLogic() {

    override var winnerCells = listOf<DoozCell>()

    override var ai: GameAi = SimpleGameAi(gameCells, aiDifficulty)

    override var winner: Player? = null

    override fun findWinner(): Player? {
        winner = findRowOrColumnWinner(gameCells)
        if (winner != null) return winner

        winner = findRowOrColumnWinner(gameCells.rotated())
        if (winner != null) return winner

        winner = findDiagonalWinner()
        if (winner != null) return winner

        return null
    }

    override fun isGameDrew(): Boolean {
        winner = findWinner()
        if (winner == null && gameCells.all { row -> row.all { it.owner != null } })
            return true
        return false
    }

    private fun findRowOrColumnWinner(
        gameCells: List<List<DoozCell>>
    ): Player? {
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

    private fun findDiagonalWinner(): Player? {
        val firstRow = gameCells.first()

        if (firstRow.first().owner == null && firstRow.last().owner == null)
            return null

        /**
         *  a x x
         *  x a x
         *  x x a
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
         *  x a x
         *  a x x
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
}