/*
 *     Dooz
 *     SimpleGameAi.kt Created/Updated by Yamin Siahmargooei at 2022/9/11
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

package io.github.yamin8000.dooz.game.ai

import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.PlayerType
import kotlin.random.Random
import kotlin.random.nextInt

class SimpleGameAi(
    override var gameCells: List<List<DoozCell>>
) : GameAi() {

    override var difficulty = AiDifficulty.Hard

    private val gameSize = gameCells.first().size

    override fun play(): DoozCell {
        return when (difficulty) {
            AiDifficulty.Easy -> easyPlay()
            AiDifficulty.Hard -> hardPlay()
            else -> easyPlay()
        }
    }

    //mitigate possible stack overflow
    private fun easyPlay(): DoozCell {
        val row = gameCells[Random.nextInt(gameCells.indices)].filter { it.owner == null }
        return if (row.isEmpty()) easyPlay()
        else row[Random.nextInt(row.indices)]
    }

    private fun hardPlay(): DoozCell {
        for (i in gameCells.indices) {
            val row = gameCells[i]
            //win
            val winCell = findCellForWinMove(row)
            if (winCell != null) return winCell
            //win block
            val winBlockCell = findCellForWinBlockMove(row)
            if (winBlockCell != null) return winBlockCell
        }
        return easyPlay()
    }


    private fun findCellForWinMove(
        row: List<DoozCell>
    ) = findCellForWinOrWinBlockMove(row, PlayerType.Computer)

    private fun findCellForWinBlockMove(
        row: List<DoozCell>
    ) = findCellForWinOrWinBlockMove(row, PlayerType.Human)

    private fun findCellForWinOrWinBlockMove(
        row: List<DoozCell>,
        playerType: PlayerType
    ): DoozCell? {
        var cell: DoozCell? = null
        if (row.filter { it.owner != null }.size < gameSize - 1) return null
        if (row.isNotEmpty() && row.all { it.owner != null }) return null
        val leftPart = row.takeWhile { it.owner?.type == playerType }
        val rightPart = row.takeLastWhile { it.owner?.type == playerType }
        if (leftPart.size + rightPart.size == gameSize - 1)
            cell = row.minus(leftPart.toSet()).minus(rightPart.toSet()).first()
        return if (cell?.owner?.type == playerType) null else cell
    }
}