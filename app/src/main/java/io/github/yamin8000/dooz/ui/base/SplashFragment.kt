/*
 *     Dooz
 *     SplashFragment.kt Created by Yamin Siahmargooei at 2022/3/2
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

package io.github.yamin8000.dooz.ui.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import androidx.navigation.fragment.findNavController
import com.orhanobut.logger.Logger
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.databinding.FragmentSplashBinding
import io.github.yamin8000.dooz.ui.util.BaseFragment
import io.github.yamin8000.dooz.util.Constants

class SplashFragment : BaseFragment<FragmentSplashBinding>({ FragmentSplashBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            Handler(Looper.getMainLooper()).postDelayed(Constants.SPLASH_DELAY) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        } catch (exception: Exception) {
            Logger.d(exception.stackTraceToString())
        }
    }
}