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
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.ui.theme.PreviewTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun RadioGroupPreview() {
    PreviewTheme {
        Card {
            RadioGroup(
                options = listOf("On", "Off", "Automatic"),
                currentOption = "On",
                onOptionChange = {},
                optionStringProvider = { it }
            )
        }
    }
}

@Composable
fun <T> RadioGroup(
    options: List<T>,
    currentOption: T,
    onOptionChange: (T) -> Unit,
    optionStringProvider: (T) -> String
) {
    Row(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .selectable(
                        selected = (option == currentOption),
                        onClick = { onOptionChange(option) },
                        role = Role.RadioButton
                    )
            ) {
                PersianText(
                    text = optionStringProvider(option),
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                RadioButton(
                    selected = (option == currentOption),
                    onClick = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    iconPainter: Painter,
    contentDescription: String,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    IconButton(
        modifier = modifier,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        }
    ) {
        Icon(
            painter = iconPainter,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier,
    contentPadding: Dp = 8.dp,
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

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}