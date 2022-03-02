/*
 *     Dooz
 *     Utility.kt Created by Yamin Siahmargooei at 2022/3/2
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

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.orhanobut.logger.Logger
import io.github.yamin8000.dooz.R

object Utility {

    /**
     * Handle soft crashes, that are suppressed using try-catch
     *
     * @param exception exception that caught
     */
    fun Fragment.handleCrash(exception: Exception) {
        val stackTraceToString = exception.stackTraceToString()
        //log it to logcat
        Logger.d(stackTraceToString)
        //navigate user to a special crash screen
        val bundle = bundleOf(Constants.STACKTRACE to stackTraceToString)
        this.findNavController().navigate(R.id.crashFragment, bundle)
    }
}