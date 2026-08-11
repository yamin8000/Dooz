package io.github.yamin8000.dooz.feature_game.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.common.R
import io.github.yamin8000.dooz.common.domain.model.AiDifficulty
import io.github.yamin8000.dooz.common.domain.model.GamePlayersType
import io.github.yamin8000.dooz.common.ui.components.AppText
import io.github.yamin8000.dooz.common.ui.components.SingleLinePersianText
import io.github.yamin8000.dooz.common.ui.theme.Sizes

@Composable
internal fun GameInfoCard(
    modifier: Modifier = Modifier,
    playersType: GamePlayersType,
    aiDifficulty: AiDifficulty,
    winnerName: String?,
    isGameDrew: Boolean
) {
    Card(
        modifier = modifier,
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Sizes.Medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(Sizes.Medium)
                    .fillMaxWidth(),
                content = {
                    AppText(
                        text = stringResource(R.string.game_info),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    SingleLinePersianText(stringResource(playersType.persianNameStringResource))

                    if (playersType == GamePlayersType.PvC) {
                        SingleLinePersianText(
                            stringResource(
                                R.string.ai_difficulty_var,
                                stringResource(aiDifficulty.persianNameStringResource)
                            )
                        )
                    } else {
                        Box(
                            modifier = Modifier
                        )
                    }
                    GameResult(
                        winnerName = winnerName,
                        isGameDrew = isGameDrew
                    )
                }
            )
        }
    )
}