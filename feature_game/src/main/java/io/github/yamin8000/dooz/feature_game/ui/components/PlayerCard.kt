package io.github.yamin8000.dooz.feature_game.ui.components

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
import io.github.yamin8000.dooz.common.ui.components.AppText
import io.github.yamin8000.dooz.common.ui.theme.Sizes
import io.github.yamin8000.dooz.common.domain.model.FirstPlayerPolicy
import io.github.yamin8000.dooz.common.domain.model.Player

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
                modifier = Modifier.padding(Sizes.Large),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Sizes.Medium),
                content = {
                    if (firstPlayerPolicy == FirstPlayerPolicy.DiceRolling) {
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
                    AppText(
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