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
import kotlin.random.Random
import kotlin.random.nextInt

class SimpleGameAi(
    override var gameCells: List<List<DoozCell>>
) : GameAi() {

    override var difficulty = AiDifficulty.Easy

    override fun play(): DoozCell {
        return when (difficulty) {
            AiDifficulty.Easy -> easyPlay()
            else -> easyPlay()
        }
    }

    private fun easyPlay(): DoozCell {
        val row = gameCells[Random.nextInt(gameCells.indices)]
        val cell = row[Random.nextInt(row.indices)]
        return if (cell.owner == null) cell
        else easyPlay()
    }
}