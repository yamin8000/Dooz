/*
 *     Dooz
 *     Utility.kt Created/Updated by Yamin Siahmargooei at 2022/9/14
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

package io.github.yamin8000.dooz.util

import android.content.Context
import java.util.Locale

object Utility {
    private fun getCurrentLocale(context: Context): Locale =
        context.resources.configuration.locales.get(0)

    //simple 90 degrees rotation
    fun <T> List<List<T>>.rotated(): List<List<T>> {
        val rotated = mutableListOf<List<T>>()

        for (j in this.indices) {
            val newRow = mutableListOf<T>()
            for (i in this.indices)
                newRow.add(this[i][j])
            rotated.add(newRow.reversed())
        }

        return rotated
    }

    fun <T> List<List<T>>.diagonals(): Pair<List<T>, List<T>> {
        return this.diagonal() to this.rotated().diagonal()
    }

    private fun <T> List<List<T>>.diagonal(): List<T> {
        val diagonal = mutableListOf<T>()
        for (i in this.indices)
            diagonal.add(this[i][i])
        return diagonal
    }

    fun Context.isLocalePersian(text: String): Boolean {
        val currentLocale = getCurrentLocale(this).language
        return currentLocale == Locale("fa").language || Constants.PERSIAN_REGEX.containsMatchIn(
            text
        )
    }
}