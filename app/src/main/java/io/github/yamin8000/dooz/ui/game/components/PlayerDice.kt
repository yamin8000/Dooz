package io.github.yamin8000.dooz.ui.game.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import io.github.yamin8000.dooz.R

@Composable
internal fun PlayerDice(
    diceIndex: Int = 1
) {
    val icon = when (diceIndex) {
        1 -> R.drawable.ic_dice_1
        2 -> R.drawable.ic_dice_2
        3 -> R.drawable.ic_dice_3
        4 -> R.drawable.ic_dice_4
        5 -> R.drawable.ic_dice_5
        6 -> R.drawable.ic_dice_6
        else -> R.drawable.ic_dice_1
    }
    Icon(
        painter = painterResource(icon),
        contentDescription = stringResource(R.string.player_turn),
    )
}