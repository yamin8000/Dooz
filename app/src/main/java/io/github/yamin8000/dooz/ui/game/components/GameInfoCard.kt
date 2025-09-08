package io.github.yamin8000.dooz.ui.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.yamin8000.dooz.R
import io.github.yamin8000.dooz.domain.model.AiDifficulty
import io.github.yamin8000.dooz.domain.model.GamePlayersType
import io.github.yamin8000.dooz.ui.components.PersianText
import io.github.yamin8000.dooz.ui.components.SingleLinePersianText

@Composable
internal fun GameInfoCard(
    modifier: Modifier = Modifier,
    playersType: GamePlayersType = GamePlayersType.PvP,
    aiDifficulty: AiDifficulty = AiDifficulty.Easy,
    winnerName: String?,
    isGameDrew: Boolean
) {
    Card(
        modifier = modifier,
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                content = {
                    PersianText(
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