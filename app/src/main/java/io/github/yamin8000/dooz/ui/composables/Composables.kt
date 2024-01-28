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
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Ripple(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick,
                onLongClick = onLongClick
            ),
        content = { content() }
    )
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
    icon: @Composable () -> Unit,
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
            Row(
                modifier = modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                if (isFontScaleNormal())
                    icon()
                content()
            }
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
    val internalChecked = rememberSaveable { mutableStateOf(checked) }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clickable(
                role = Role.Switch,
                onClick = {
                    internalChecked.value = !internalChecked.value
                    onCheckedChange(internalChecked.value)
                }
            ),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PersianText(text = caption)
                Switch(
                    checked = checked,
                    onCheckedChange = null
                )
            }
        }
    )
}

@Composable
fun <T> RadioGroup(
    //unstable
    columns: GridCells = GridCells.Fixed(2),
    options: List<T>,
    currentOption: T,
    onOptionChange: (T) -> Unit,
    optionStringProvider: (T) -> String
) {
    LazyVerticalGrid(
        modifier = Modifier.height(100.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = columns,
        content = {
            items(options) { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .selectable(
                            selected = (option == currentOption),
                            onClick = { onOptionChange(option) },
                            role = Role.RadioButton
                        )
                ) {
                    PersianText(
                        text = optionStringProvider(option),
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 2.dp)
                            .weight(5f)
                    )
                    RadioButton(
                        selected = (option == currentOption),
                        onClick = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(1f)
                    )
                }
            }
        }
    )
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    ClickableIcon(
        modifier = modifier,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
            )
        }
    )
}

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    IconButton(
        modifier = modifier,
        content = icon,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        }
    )
}

@Composable
fun AnimatedAppIcon(
    isAnimating: Boolean = true
) {
    var size by remember { mutableStateOf(0.dp) }

    LaunchedEffect(Unit) {
        while (isAnimating) {
            animate(
                typeConverter = Dp.VectorConverter,
                initialValue = 64.dp,
                targetValue = 56.dp,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                block = { value, _ -> size = value }
            )
        }
    }

    Icon(
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = stringResource(R.string.app_name),
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(size)
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTitle(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    onBackClick: () -> Unit,
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        snackbarHost = snackbarHost,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Surface(
                shadowElevation = 8.dp
            ) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        PersianText(
                            text = title,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    actions = {
                        ClickableIcon(
                            imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                            contentDescription = "",
                            onClick = { onBackClick() }
                        )
                    }
                )
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .padding(it)
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 4.dp)
                    .fillMaxHeight(),
                content = { content() }
            )
        }
    )
}

@Composable
fun isFontScaleNormal() = LocalDensity.current.fontScale <= 1.0f