package io.github.yamin8000.dooz.feature_game.ui

import io.github.yamin8000.dooz.common.domain.model.DoozCell

sealed interface GameAction {
    data object NewGame : GameAction
    data class PlayCell(val cell: DoozCell) : GameAction
    data object Undo : GameAction
}