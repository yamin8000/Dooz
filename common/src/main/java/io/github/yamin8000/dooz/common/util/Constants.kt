package io.github.yamin8000.dooz.common.util

import io.github.yamin8000.dooz.common.domain.model.AiDifficulty

object Constants {

    val difficulties = listOf(AiDifficulty.Easy, AiDifficulty.Medium, AiDifficulty.Hard)
    val aiPlayDelayRange = 350L..750L
    val diceRange = 1..6
    
    val gameSizeRange = 3..7

    const val GAME_DEFAULT_SIZE = 3

    object Shapes {
        const val RING_SHAPE = "ringShape"
        const val CIRCLE_SHAPE = "circleShape"
        const val X_SHAPE = "xShape"
        const val TRI_SHAPE = "triangleShape"
        const val RECT_SHAPE = "rectangleShape"
    }
}