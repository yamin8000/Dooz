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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.ui.theme.PreviewTheme

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
private fun SwitchWithTextPreview() {
    PreviewTheme {
        SwitchWithText(
            caption = "Hello",
            checked = true,
            onCheckedChange = {}
        )
    }
}

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    painter: Painter,
    space: Dp = 8.dp,
    contentDescription: String?,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            content()
            Spacer(modifier = Modifier.width(space))
            Icon(
                painter = painter,
                contentDescription = contentDescription
            )
        }
    )
}

@Composable
fun MySnackbar(
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    dismissAction: @Composable (() -> Unit)? = null,
    actionOnNewLine: Boolean = false,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
    content: @Composable () -> Unit
) {
    Snackbar(
        modifier = modifier
            .padding(vertical = 32.dp, horizontal = 16.dp)
            .padding(WindowInsets.ime.asPaddingValues()),
        action = action,
        dismissAction = dismissAction,
        actionOnNewLine = actionOnNewLine,
        shape = RoundedCornerShape(10.dp),
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor,
        content = content
    )
}

@Composable
fun SwitchWithText(
    caption: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val internalChecked = remember { mutableStateOf(checked) }
    Box(modifier = Modifier
        .clickable(
            role = Role.Switch,
            onClick = {
                internalChecked.value = !internalChecked.value
                onCheckedChange(internalChecked.value)
            }
        )
        .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PersianText(caption)
            Switch(
                checked = checked,
                onCheckedChange = null
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
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
    footer: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = columnModifier.padding(contentPadding),
            horizontalAlignment = horizontalAlignment,
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