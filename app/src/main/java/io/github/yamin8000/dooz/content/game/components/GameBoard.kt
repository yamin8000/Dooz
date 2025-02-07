package io.github.yamin8000.dooz.content.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.model.DoozCell
import io.github.yamin8000.dooz.model.Player
import io.github.yamin8000.dooz.model.PlayerType

@Composable
internal fun GameBoard(
    gameSize: Int,
    gameCells: List<List<DoozCell>>,
    winnerCells: List<DoozCell>,
    isGameFinished: Boolean,
    currentPlayerType: PlayerType?,
    shapeProvider: (Player?) -> Shape,
    onItemClick: (DoozCell) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val boxPadding = 16.dp
    val boxSize = remember(screenWidth) {
        screenWidth - (2 * boxPadding.value).dp
    }
    val itemMargin = 8.dp
    val boxItemSize = remember(boxSize) {
        ((boxSize.value - itemMargin.value * (gameSize - 1)) / gameSize).dp
    }

    LazyVerticalGrid(
        modifier = Modifier.size(boxSize),
        columns = GridCells.Fixed(gameSize),
        horizontalArrangement = Arrangement.spacedBy(itemMargin),
        verticalArrangement = Arrangement.spacedBy(itemMargin),
        userScrollEnabled = false,
        content = {
            gameCells.forEachIndexed { _, row ->
                itemsIndexed(row) { _, cell ->
                    val colors = if (cell in winnerCells)
                        MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
                    else MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                    DoozItem(
                        clickable = !isGameFinished && currentPlayerType == PlayerType.Human && cell.owner == null,
                        shape = shapeProvider(cell.owner),
                        size = boxItemSize,
                        hasOwner = cell.owner != null,
                        onClick = { onItemClick(cell) },
                        backgroundColor = colors.first,
                        contentColor = colors.second
                    )
                }
            }
        }
    )
}