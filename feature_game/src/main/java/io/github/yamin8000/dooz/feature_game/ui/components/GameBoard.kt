package io.github.yamin8000.dooz.feature_game.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import io.github.yamin8000.dooz.common.domain.model.DoozCell
import io.github.yamin8000.dooz.common.domain.model.PlayerType
import io.github.yamin8000.dooz.common.ui.components.toShape
import io.github.yamin8000.dooz.common.ui.theme.Sizes
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GameBoard(
    gameSize: Int,
    gameCells: SnapshotStateList<DoozCell>,
    winnerCells: ImmutableList<DoozCell>,
    isGameFinished: Boolean,
    currentPlayerType: PlayerType?,
    onItemClick: (DoozCell) -> Unit
) {
    val screenWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }
    val boxPadding = Sizes.Large
    val boxSize = remember(screenWidth) {
        screenWidth - (2 * boxPadding.value).dp
    }
    val itemMargin = Sizes.Medium
    val boxItemSize = remember(boxSize) {
        ((boxSize.value - itemMargin.value * (gameSize - 1)) / gameSize).dp
    }

    LazyVerticalGrid(
        modifier = Modifier.size(boxSize),
        columns = GridCells.Fixed(gameSize),
        horizontalArrangement = Arrangement.spacedBy(itemMargin, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(itemMargin, Alignment.CenterVertically),
        content = {
            items(
                items = gameCells,
                key = { "${it.x}-${it.y}" },
                itemContent = { cell ->
                    val colors = if (cell in winnerCells)
                        MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
                    else MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                    DoozItem(
                        clickable = !isGameFinished && currentPlayerType == PlayerType.Human && cell.owner == null,
                        shape = cell.owner?.shape?.toShape() ?: CircleShape,
                        size = boxItemSize,
                        hasOwner = cell.owner != null,
                        backgroundColor = colors.first,
                        contentColor = colors.second,
                        onClick = { onItemClick(cell) }
                    )
                }
            )
        }
    )
}