/*
 *     Dooz
 *     Game.kt Created/Updated by Yamin Siahmargooei at 2022/9/4
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

package io.github.yamin8000.dooz.game

import android.os.Parcelable
import io.github.yamin8000.dooz.model.GamePlayersType
import io.github.yamin8000.dooz.model.Player
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val gamePlayersType: GamePlayersType,
    var isGameStarted: Boolean = false,
    var isGameFinished: Boolean = false,
    var winner: Player? = null
) : Parcelable
