/*
 *     Dooz
 *     MainActivity.kt Created by Yamin Siahmargooei at 2022/3/31
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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import io.github.yamin8000.dooz.content.game.GameContent
import io.github.yamin8000.dooz.ui.navigation.Nav
import io.github.yamin8000.dooz.content.settings.SettingsContent
import io.github.yamin8000.dooz.ui.theme.DoozTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DoozTheme { MainContent() } }

        prepareLogger()
    }

    @Composable
    private fun MainContent() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Nav.Routes.game
        ) {
            composable(Nav.Routes.game) {
                GameContent(navController)
            }

            composable(Nav.Routes.settings) {
                SettingsContent(navController)
            }
        }
    }

    private fun prepareLogger() {
        Logger.addLogAdapter(
            AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder().tag("<==>").build()
            )
        )
        Logger.d("Application is Started!")
    }
}