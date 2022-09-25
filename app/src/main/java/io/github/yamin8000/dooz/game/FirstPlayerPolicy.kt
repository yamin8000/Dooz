/*
 *     Dooz
 *     FirstPlayerPolicy.kt Created/Updated by Yamin Siahmargooei at 2022/9/25
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
import androidx.annotation.StringRes
import io.github.yamin8000.dooz.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FirstPlayerPolicy(
    @StringRes val persianNameStringResource: Int
) : Parcelable {
    DiceRolling(R.string.dice_rolling_start), HumanFirst(R.string.human_first_start)
}