package io.github.yamin8000.dooz.content.game.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.game.FirstPlayerPolicy
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.ui.ShapePreview
import io.github.yamin8000.dooz.ui.components.PersianText
import io.github.yamin8000.dooz.ui.toShape
import io.github.yamin8000.dooz.util.Utility

@Composable
internal fun PlayerCard(
    modifier: Modifier = Modifier,
    player: Player,
    firstPlayerPolicy: FirstPlayerPolicy,
    isCurrentPlayer: Boolean = true
) {
    val alpha = remember(isCurrentPlayer) {
        if (isCurrentPlayer) 1f else .38f
    }

    OutlinedCard(
        modifier = modifier.alpha(alpha),
        content = {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    if (firstPlayerPolicy == FirstPlayerPolicy.DiceRolling && Utility.isFontScaleNormal()) {
                        AnimatedContent(
                            targetState = player.diceIndex,
                            label = "",
                            content = { PlayerDice(diceIndex = it) },
                            transitionSpec = {
                                (slideInVertically { it } + fadeIn())
                                    .togetherWith(slideOutVertically { -it } + fadeOut())
                            }
                        )
                    }
                    player.shape?.toShape()?.let { shape -> ShapePreview(shape, 30.dp) }
                    PersianText(
                        text = player.name,
                        modifier = Modifier.weight(2f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    )
}