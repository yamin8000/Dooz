package io.github.yamin8000.dooz.feature_game.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.common.ui.components.XShape
import io.github.yamin8000.dooz.common.ui.theme.PreviewTheme
import io.github.yamin8000.dooz.common.ui.theme.Sizes
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewTheme {
        Box(
            modifier = Modifier.padding(Sizes.xLarge),
            content = {
                DoozItem(
                    shape = XShape,
                    clickable = Random.nextBoolean(),
                    size = Sizes.xxLarge,
                    hasOwner = Random.nextBoolean(),
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {}
                )
            }
        )
    }
}

@Composable
internal fun DoozItem(
    shape: Shape,
    clickable: Boolean,
    size: Dp,
    hasOwner: Boolean,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(Sizes.Small))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                enabled = clickable,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center,
        content = {
            AnimatedVisibility(
                visible = hasOwner,
                enter = scaleIn(animationSpec = tween(150)),
                exit = scaleOut(animationSpec = tween(150)),
                content = {
                    if (hasOwner) {
                        Box(
                            modifier = Modifier
                                .size((size.value / 2).dp)
                                .clip(shape)
                                .background(contentColor),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                        )
                    }
                }
            )
        }
    )
}