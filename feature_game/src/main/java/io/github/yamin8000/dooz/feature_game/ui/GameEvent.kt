package io.github.yamin8000.dooz.feature_game.ui

sealed interface GameEvent {
    data object Vibrate : GameEvent
    data object PlayDrawSound : GameEvent
    data object PlayLoseSound : GameEvent
    data object PlayWinSound : GameEvent
    data object PlayDiceSound : GameEvent
    data object PlayPencilSound : GameEvent
}