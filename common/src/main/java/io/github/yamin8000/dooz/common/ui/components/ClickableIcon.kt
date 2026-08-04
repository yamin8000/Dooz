package io.github.yamin8000.dooz.common.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun ClickableIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    IconButton(
        modifier = modifier,
        content = content,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
            onClick()
        }
    )
}

@Composable
fun ClickableIcon(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    ClickableIcon(
        modifier = modifier,
        onClick = onClick,
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
            )
        }
    )
}