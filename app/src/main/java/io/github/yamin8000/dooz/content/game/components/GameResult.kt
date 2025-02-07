package io.github.yamin8000.dooz.content.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.ui.components.PersianText
import io.github.yamin8000.dooz.ui.components.SingleLinePersianText
import io.github.yamin8000.dooz.util.Utility

@Composable
fun GameResult(
    winnerName: String?,
    isGameDrew: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        content = {
            if (Utility.isFontScaleNormal()) {
                PersianText(
                    text = stringResource(R.string.game_result),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (isGameDrew) {
                SingleLinePersianText(stringResource(R.string.game_is_drew))
            }
            if (winnerName != null) {
                SingleLinePersianText(stringResource(R.string.x_is_winner, winnerName))
            }
            if (!isGameDrew && winnerName == null) {
                SingleLinePersianText(stringResource(R.string.undefined))
            }
        }
    )
}