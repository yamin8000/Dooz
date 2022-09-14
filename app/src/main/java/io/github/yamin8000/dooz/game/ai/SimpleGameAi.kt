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
import io.github.yamin8000.dooz.util.Utility.diagonals
import io.github.yamin8000.dooz.util.Utility.rotated
import kotlin.random.Random
import kotlin.random.nextInt

class SimpleGameAi(
    override var gameCells: List<List<DoozCell>>,
    override var difficulty: AiDifficulty
) : GameAi() {

    private val gameSize = gameCells.first().size

    private val rotatedGameCells = gameCells.rotated()

    private val diagonals = gameCells.diagonals()

    private val random = Random(System.nanoTime())

    override fun play(): DoozCell {
        val cell = when (difficulty) {
            AiDifficulty.Easy -> easyPlay()
            AiDifficulty.Medium -> mediumPlay()
            AiDifficulty.Hard -> hardPlay()
        }
        if (cell.owner != null) throw IllegalArgumentException("Cell already has a owner!")
        else return cell
    }

    private fun mediumPlay(): DoozCell {
        return when (random.nextInt(0..1)) {
            0 -> easyPlay()
            else -> hardPlay()
        }
    }

    //mitigate possible stack overflow
    private fun easyPlay(): DoozCell {
        val row = gameCells[random.nextInt(gameCells.indices)].filter { it.owner == null }
        return if (row.isEmpty()) easyPlay()
        else row[random.nextInt(row.indices)]
    }

    private fun hardPlay(): DoozCell {
        //win or win block scanner
        var cell = winOrWinBlockScanner(gameCells)
        if (cell != null) return cell
        //fork or fork block scanner
        cell = forkOrForkBlockScanner()
        if (cell != null) return cell
        //center play
        cell = centerPlay()
        if (cell != null) return cell
        //corner play
        cell = cornerPlay()
        if (cell != null) return cell
        return easyPlay()
    }

    private fun cornerPlay(): DoozCell? {
        val corners = buildList {
            add(gameCells.first().first())
            add(gameCells.first().last())
            add(gameCells.last().last())
            add(gameCells.last().first())
        }.filter { it.owner == null }
        if (corners.isEmpty()) return null
        val cell = when {
            gameCells.first().first().owner?.type == PlayerType.Human -> gameCells.last()
                .last()
            gameCells.first().last().owner?.type == PlayerType.Human -> gameCells.last()
                .first()
            gameCells.last().last().owner?.type == PlayerType.Human -> gameCells.first()
                .first()
            gameCells.last().first().owner?.type == PlayerType.Human -> gameCells.first()
                .last()
            else -> corners.getOrNull(random.nextInt(corners.indices))
        } ?: return null
        return if (cell.owner == null) cell
        else corners.getOrNull(random.nextInt(corners.indices))
    }

    private fun centerPlay(): DoozCell? {
        val center = gameSize / 2
        return if (gameSize % 2 != 0) {
            val cell = gameCells[center][center]
            if (cell.owner == null) cell
            else null
        } else null
    }

    private fun winOrWinBlockScanner(
        gameCells: List<List<DoozCell>>
    ): DoozCell? {
        //column scan
        var cell = winOrWinBlockLinearScanner(gameCells)
        if (cell != null) return cell
        //row scan
        cell = winOrWinBlockLinearScanner(rotatedGameCells)
        if (cell != null) return cell
        //diagonal scan
        cell = winOrWinBlockDiagonalScanner()
        if (cell != null) return cell
        return null
    }

    private fun forkOrForkBlockScanner(): DoozCell? {
        var cell = forkOrForkBlockGeneralScanner(gameCells, rotatedGameCells)
        if (cell != null) return cell
        cell = forkOrForkBlockGeneralScanner(gameCells, diagonals.toList())
        if (cell != null) return cell
        cell = forkOrForkBlockGeneralScanner(rotatedGameCells, diagonals.toList())
        if (cell != null) return cell
        return null
    }

    private fun forkOrForkBlockGeneralScanner(
        gameCells: List<List<DoozCell>>,
        intersectingRows: List<List<DoozCell>>
    ): DoozCell? {
        for (i in gameCells.indices) {
            val row = gameCells[i]
            if (row.filter { it.owner != null }.size != gameSize - 2) continue
            for (j in intersectingRows.indices) {
                val column = intersectingRows[j]
                if (column.filter { it.owner != null }.size != gameSize - 2) continue
                if (row.find { it.owner != null }?.owner == column.find { it.owner != null }?.owner) {
                    val cell = row.filter { it.owner == null }
                        .intersect(column.filter { it.owner == null }.toSet())
                        .singleOrNull()
                    if (cell != null && cell.owner == null)
                        return cell
                }
            }
        }
        return null
    }

    private fun winOrWinBlockLinearScanner(
        gameCells: List<List<DoozCell>>
    ): DoozCell? {
        for (i in gameCells.indices) {
            val cell = winOrWinBlockRowScanner(gameCells[i])
            if (cell != null) return cell
        }
        return null
    }

    private fun winOrWinBlockRowScanner(
        row: List<DoozCell>
    ): DoozCell? {
        var cell = findCellForWin(row)
        if (cell != null) return cell
        cell = findCellForWinBlockMove(row)
        if (cell != null) return cell
        return cell
    }

    private fun winOrWinBlockDiagonalScanner(): DoozCell? {
        val (firstDiagonal, secondDiagonal) = diagonals
        var cell = winOrWinBlockRowScanner(firstDiagonal)
        if (cell != null) return cell
        cell = winOrWinBlockRowScanner(secondDiagonal)
        if (cell != null) return cell
        return null
    }

    private fun findCellForWin(
        row: List<DoozCell>
    ) = findCellForWinOrWinBlock(row, PlayerType.Computer)

    private fun findCellForWinBlockMove(
        row: List<DoozCell>
    ) = findCellForWinOrWinBlock(row, PlayerType.Human)

    private fun findCellForWinOrWinBlock(
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