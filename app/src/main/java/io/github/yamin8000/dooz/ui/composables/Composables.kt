/*
 *     Dooz
 *     Composables.kt Created by Yamin Siahmargooei at 2022/8/25
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

package io.github.yamin8000.dooz.ui.composables

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.model.GamePlayersType

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    contentPadding: Dp = 16.dp,
    elementVerticalSpacing: Dp = 8.dp,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit = {},
    footer: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = columnModifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(elementVerticalSpacing)
        ) {
            header()
            content()
            footer()
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun getGamePlayersTypeCaption(
    gamePlayersType: GamePlayersType
): String {
    return if (gamePlayersType == GamePlayersType.PvP) stringResource(R.string.play_with_human)
    else stringResource(R.string.play_with_computer)
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}