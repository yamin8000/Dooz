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

import io.github.yamin8000.dooz.model.AiDifficulty
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

    private var totalCells = listOf<List<DoozCell>>()

    init {
        totalCells = buildList {
            addAll(gameCells)
            addAll(rotatedGameCells)
            addAll(diagonals.toList())
        }
    }

    override fun play(): DoozCell {
        val cell = when (difficulty) {
            AiDifficulty.Easy -> easyPlay()
            AiDifficulty.Medium -> mediumPlay()
            AiDifficulty.Hard -> hardPlay()
        }
        if (cell.owner != null) throw IllegalArgumentException("Cell already has a owner!")
        else return cell
    }

    private fun DoozCell?.getReadyToPlayCell(): DoozCell? {
        return if (this == null || this.owner != null) null else this
    }

    private fun mediumPlay(): DoozCell {
        return when (random.nextInt(0..1)) {
            0 -> easyPlay()
            else -> hardPlay()
        }
    }

    //mitigate possible stack overflow
    private fun easyPlay(): DoozCell {
        val cells = gameCells.flatten()
        return cells.getOrNull(random.nextInt(cells.indices)).getReadyToPlayCell() ?: easyPlay()
    }

    private fun hardPlay(): DoozCell {
        return ((winOrWinBlockPlay() ?: forkOrForkBlockPlayHandler() ?: centerPlay()
        ?: cornerPlay()).getReadyToPlayCell() ?: easyPlay())
    }

    private fun cornerPlay(): DoozCell? {
        val corners = playableCorners()
        if (corners.isEmpty()) return null
        val cell = when {
            gameCells.firstOrNull()
                ?.firstOrNull()?.owner?.type == PlayerType.Human -> gameCells.lastOrNull()
                ?.lastOrNull()

            gameCells.firstOrNull()
                ?.lastOrNull()?.owner?.type == PlayerType.Human -> gameCells.lastOrNull()
                ?.firstOrNull()

            gameCells.lastOrNull()
                ?.lastOrNull()?.owner?.type == PlayerType.Human -> gameCells.firstOrNull()
                ?.firstOrNull()

            gameCells.lastOrNull()
                ?.firstOrNull()?.owner?.type == PlayerType.Human -> gameCells.firstOrNull()
                ?.lastOrNull()

            else -> corners.getOrNull(random.nextInt(corners.indices))
        } ?: return null
        return if (cell.owner == null) cell
        else corners.getOrNull(random.nextInt(corners.indices))
    }

    private fun playableCorners() = buildList {
        add(gameCells.firstOrNull()?.firstOrNull())
        add(gameCells.firstOrNull()?.lastOrNull())
        add(gameCells.lastOrNull()?.lastOrNull())
        add(gameCells.lastOrNull()?.firstOrNull())
    }.filter { it?.owner == null }

    private fun centerPlay(): DoozCell? {
        val center = gameSize / 2
        return if (gameSize % 2 != 0) {
            val cell = gameCells.getOrNull(center)?.getOrNull(center) ?: return null
            if (cell.owner == null) cell
            else null
        } else null
    }

    private fun winOrWinBlockPlay(): DoozCell? {
        for (i in totalCells.indices) {
            val cells = totalCells.getOrNull(i) ?: return null
            val cell = winOrWinBlockSingleLineScanner(cells)
            if (cell != null) return cell
        }
        return null
    }

    private fun forkOrForkBlockPlayHandler(): DoozCell? {
        var cell = forkOrForkBlockPlay(gameCells, rotatedGameCells)
        if (cell != null) return cell
        cell = forkOrForkBlockPlay(gameCells, diagonals.toList())
        if (cell != null) return cell
        cell = forkOrForkBlockPlay(rotatedGameCells, diagonals.toList())
        if (cell != null) return cell
        return null
    }

    private fun forkOrForkBlockPlay(
        gameCells: List<List<DoozCell>>,
        compareGameCells: List<List<DoozCell>>
    ): DoozCell? {
        return forkPlay(gameCells, compareGameCells) ?: forkBlockPlay(gameCells, compareGameCells)
    }

    private fun forkPlay(
        gameCells: List<List<DoozCell>>,
        compareGameCells: List<List<DoozCell>>,
    ) = forkOrForkBlockPlayScanner(gameCells, compareGameCells, false)

    private fun forkBlockPlay(
        gameCells: List<List<DoozCell>>,
        compareGameCells: List<List<DoozCell>>
    ) = forkOrForkBlockPlayScanner(gameCells, compareGameCells, true)

    private fun forkOrForkBlockPlayScanner(
        gameCells: List<List<DoozCell>>,
        compareGameCells: List<List<DoozCell>>,
        isBlocking: Boolean
    ): DoozCell? {
        val playerType = if (isBlocking) PlayerType.Human else PlayerType.Computer

        for (i in gameCells.indices) {
            val cells = gameCells.getOrNull(i) ?: continue
            if (!cells.isSuspectToFork(playerType)) continue

            for (j in compareGameCells.indices) {
                val intersectingCells = compareGameCells.getOrNull(j) ?: continue
                if (!intersectingCells.isSuspectToFork(playerType)) continue

                if (cells.find { it.owner != null }?.owner == intersectingCells.find { it.owner != null }?.owner) {
                    if (isBlocking) {
                        val forceBlockCell = findCellForOpponentForceBlock()
                        if (forceBlockCell != null)
                            return forceBlockCell
                    }

                    val cell = cells.filter { it.owner == null }
                        .intersect(intersectingCells.filter { it.owner == null }.toSet())
                        .firstOrNull()
                    if (cell != null && cell.owner == null)
                        return cell
                }
            }
        }
        return null
    }

    private fun findCellForOpponentForceBlock(): DoozCell? {
        val cells = buildList {
            addAll(gameCells)
            addAll(rotatedGameCells)
            addAll(diagonals.toList())
        }
        for (i in cells.indices) {
            val line = gameCells.getOrNull(i) ?: return null
            val cell = findCellForWinOrWinBlock(
                line,
                PlayerType.Computer,
                SimpleGameBlankCell.Fork.count
            )
            if (cell != null) return cell
        }
        return null
    }

    private fun List<DoozCell>.isSuspectToFork(
        relevantOwnerType: PlayerType
    ): Boolean {
        if (isEmpty()) return false
        val owned = filter { it.owner != null }
        return owned.size == 1 && owned.all { it.owner?.type == relevantOwnerType }
    }

    private fun winOrWinBlockSingleLineScanner(
        cells: List<DoozCell>
    ): DoozCell? {
        var cell = findCellForWin(cells)
        if (cell != null) return cell
        cell = findCellForWinBlock(cells)
        if (cell != null) return cell
        return null
    }

    private fun findCellForWin(
        cells: List<DoozCell>
    ) = findCellForWinOrWinBlock(cells, PlayerType.Computer)

    private fun findCellForWinBlock(
        cells: List<DoozCell>
    ) = findCellForWinOrWinBlock(cells, PlayerType.Human)

    private fun findCellForWinOrWinBlock(
        cells: List<DoozCell>,
        playerType: PlayerType,
        blankCells: Int = SimpleGameBlankCell.Win.count
    ): DoozCell? {
        val (vacant, owned) = cells.partition { it.owner == null }
        if (vacant.size > blankCells) return null
        if (owned.any { it.owner?.type != playerType }) return null
        return vacant.firstOrNull()
    }
}