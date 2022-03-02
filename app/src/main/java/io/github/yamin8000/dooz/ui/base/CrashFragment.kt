/*
 *     Dooz
 *     CrashFragment.kt Created by Yamin Siahmargooei at 2022/3/2
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
import android.view.View
import io.github.yamin8000.dooz.databinding.FragmentCrashBinding
import io.github.yamin8000.dooz.ui.util.BaseFragment
import io.github.yamin8000.dooz.util.Constants

class CrashFragment : BaseFragment<FragmentCrashBinding>({ FragmentCrashBinding.inflate(it) }) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.crashImage.setOnClickListener { activity?.finish() }
        binding.crashText.setOnClickListener { activity?.finish() }

        arguments?.let {
            val stacktrace = it.getString(Constants.STACKTRACE) ?: ""
        }
    }
}