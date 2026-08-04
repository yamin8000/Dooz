package io.github.yamin8000.dooz.common.domain.model

import androidx.annotation.StringRes
import io.github.yamin8000.dooz.common.R

enum class FirstPlayerPolicy(
    @field:StringRes val persianNameStringResource: Int
) {
    DiceRolling(R.string.dice_rolling_start), HumanFirst(R.string.human_first_start)
}